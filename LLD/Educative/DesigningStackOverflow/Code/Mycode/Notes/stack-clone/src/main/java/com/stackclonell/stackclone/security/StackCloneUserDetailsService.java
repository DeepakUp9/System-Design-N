package com.stackclonell.stackclone.security;

import com.stackclonell.stackclone.core.model.User;
import com.stackclonell.stackclone.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Security Component: Loads user-specific data during the authentication process.
 */
@Service
public class StackCloneUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public StackCloneUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds the user in our database and wraps it in a Spring Security UserDetails object.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // We use Spring Security's built-in User implementation for simplicity here.
        // Production: Roles/Authorities must be managed based on reputation/admin status.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash()) // Must be the hashed password
                // Simplified authority: Grant all authenticated users a basic role
                .roles("USER")
                .build();
    }
}