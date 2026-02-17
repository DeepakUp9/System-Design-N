# Library Management System - JWT-Based Role Authentication

A complete, production-ready JWT implementation for role-based access control in a Library Management System using Spring Boot 3.x.

---

## Overview

This implementation demonstrates:

✅ Different roles (`LIBRARIAN`, `MEMBER`)  
✅ Different APIs for each role  
✅ JWT-based authentication and authorization  
✅ Security layer that enforces access rules

---

## ⚙️ Step 1: Basic Concept

You'll have two main users:

* **Librarian** → can manage books (add/remove)
* **Member** → can only search or borrow books

We'll secure these roles using **Spring Security** and **JWT** authentication.

---

## 🧱 Step 2: Add Dependencies

In your `pom.xml`, add the following dependencies:

```xml
<!-- Spring Boot Web + Security -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JJWT (JWT library) -->
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.11.5</version>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-impl</artifactId>
  <version>0.11.5</version>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-jackson</artifactId>
  <version>0.11.5</version>
  <scope>runtime</scope>
</dependency>
```

---

## 🔧 Step 3: Application Properties

`src/main/resources/application.yml`:

```yaml
jwt:
  secret: "replace-with-a-very-strong-secret-key-should-be-64+chars"
  expiration-ms: 3600000  # 1 hour
```

**Note:** In production, store secret in Vault/Env, not in repo.

---

## 🧩 Step 4: JWT Utility (Create + Validate Tokens)

`com.example.security.JwtUtil.java`

```java
package com.example.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration-ms}") long expirationMs) {
        // Secret length should be sufficiently large; convert string to bytes
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        Object roles = claims.get("roles");
        if (roles instanceof List) {
            return (List<String>) roles;
        }
        return Collections.emptyList();
    }
}
```

---

## 👤 Step 5: In-Memory UserDetailsService

`com.example.security.CustomUserDetailsService.java`

```java
package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // demo users
        if ("librarian".equals(username)) {
            return User.builder()
                       .username("librarian")
                       .password(passwordEncoder.encode("lib123"))
                       .roles("LIBRARIAN")
                       .build();
        } else if ("member".equals(username)) {
            return User.builder()
                       .username("member")
                       .password(passwordEncoder.encode("mem123"))
                       .roles("MEMBER")
                       .build();
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
```

---

## 🔒 Step 6: JWT Authentication Filter

This extracts Bearer token, validates, and sets `SecurityContext`.

`com.example.security.JwtAuthenticationFilter.java`

```java
package com.example.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = uds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        List<GrantedAuthority> authorities = jwtUtil.extractRoles(token)
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}
```

---

## ⚙️ Step 7: Security Configuration

Register filter and expose AuthenticationManager.

`com.example.security.SecurityConfig.java`

```java
package com.example.security;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService uds;

    public SecurityConfig(JwtUtil jwtUtil, CustomUserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.uds = uds;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil, uds);

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(passwordEncoder());
        return new org.springframework.security.authentication.ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## 🔐 Step 8: Authentication Controller

Login endpoint to get JWT token.

`com.example.auth.AuthController.java`

```java
package com.example.auth;

import com.example.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    record LoginRequest(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest rq) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(rq.username(), rq.password())
            );

            UserDetails user = (UserDetails) auth.getPrincipal();
            var roles = user.getAuthorities().stream()
                            .map(a -> a.getAuthority().replace("ROLE_", ""))
                            .collect(Collectors.toList());

            String token = jwtUtil.generateToken(user.getUsername(), roles);
            return ResponseEntity.ok().body(new java.util.HashMap<>() {{
                put("token", token);
                put("expiresIn", 3600);
            }});
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
```

---

## 📚 Step 9: Book Controller (Role-Based Access)

`com.example.book.BookController.java`

```java
package com.example.book;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    // simple in-memory store for demo
    private final Map<String,String> books = new HashMap<>();

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/add")
    public Map<String, String> addBook(@RequestParam String title) {
        String id = UUID.randomUUID().toString();
        books.put(id, title);
        return Map.of("id", id, "title", title);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable String id) {
        books.remove(id);
        return "deleted";
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'MEMBER')")
    @GetMapping("/search")
    public List<Map<String, String>> search(@RequestParam Optional<String> q) {
        var res = new ArrayList<Map<String,String>>();
        books.forEach((id, t) -> {
            if (q.isEmpty() || t.toLowerCase().contains(q.get().toLowerCase())) {
                res.add(Map.of("id", id, "title", t));
            }
        });
        return res;
    }
}
```

### 🧠 Notice:

* `@PreAuthorize` controls who can access which API
* `hasRole('LIBRARIAN')` or `hasAnyRole('LIBRARIAN', 'MEMBER')`

---

## 🧪 Step 10: How to Test Manually

### 1. Start the Spring Boot app

### 2. Login to Get JWT Token

```http
POST /auth/login
Content-Type: application/json

{
  "username": "librarian",
  "password": "lib123"
}
```

**Response:**
```json
{
  "token": "<JWT>",
  "expiresIn": 3600
}
```

### 3. Use JWT for Authenticated Requests

**Add a Book (Librarian Only):**

```http
POST /api/books/add?title=SpringBook
Header: Authorization: Bearer <JWT>
```

**Test Results:**

| User | Endpoint | Result |
|------|----------|--------|
| Librarian | `POST /api/books/add` | ✅ Success |
| Member | `POST /api/books/add` | ❌ 403 Forbidden |
| Librarian | `GET /api/books/search` | ✅ Success |
| Member | `GET /api/books/search` | ✅ Success |

### Demo User Credentials

| Username | Password | Role |
|----------|----------|------|
| librarian | lib123 | LIBRARIAN |
| member | mem123 | MEMBER |

---

## 🎯 Why This Design is "Versatile"

You can explain in an LLD interview:

> "The system supports multiple actors by assigning roles. Each API or action is protected through Spring Security using method-level annotations (`@PreAuthorize`). Librarians have full CRUD rights, while members have read-only access. JWT-based authentication ensures stateless, scalable authentication. This design makes the system both secure and versatile."

---

## 🧠 How This Ties Back to LLD (Design Layer)

Your class hierarchy stays the same:

```java
abstract class User { ... }
class Librarian extends User { ... }
class Member extends User { ... }
```

But now, your system behavior (API access) is controlled dynamically using:

* Spring Security roles (`ROLE_LIBRARIAN`, `ROLE_MEMBER`)
* `@PreAuthorize` annotations
* JWT tokens for stateless authentication

That's how **LLD (class design) + security layer** combine to implement real-world versatility.

---

## 📋 Discussion Points for Interviews

### Why JWT?

* **Stateless:** No server-side session storage required
* **Scalable:** Easy horizontal scaling
* **Self-contained:** All information embedded in token

### Drawbacks & Solutions

| Challenge | Solution |
|-----------|----------|
| Token revocation | Implement blacklist or short expiry + refresh tokens |
| Role change propagation | Use short expiry or maintain revocation list |
| Security concerns | Store tokens securely, use HTTPS, avoid localStorage |

### Refresh Tokens

* Use short-lived access token (15 min)
* Long-lived refresh token (7 days) stored securely
* Refresh endpoint to get new access token

### Security Best Practices

* **Secret Management:** Store in Vault/KMS, rotate periodically
* **HTTPS:** Always use in production
* **Cookie Security:** Set `HttpOnly`, `Secure`, `SameSite` flags
* **XSS Protection:** Avoid storing tokens in localStorage
* **Rate Limiting:** Prevent brute force attacks
* **Monitoring:** Track failed auth attempts
* **MFA:** Implement for sensitive operations

### Production Additions

* Rate limiting on `/auth/login`
* Monitoring and alerting for failed authentication attempts
* Multi-factor authentication (MFA) for librarians
* Token refresh mechanism
* Redis-based token blacklist for logout
* Audit logging for sensitive operations

---

## 🚀 Next Steps

1. **Replace in-memory users** with database-backed authentication
2. **Implement refresh tokens** for better security
3. **Add token blacklist** for logout functionality
4. **Implement password reset** flow
5. **Add audit logging** for all operations
6. **Set up monitoring** for security events
