package com.stockbrokerage.user.service;

import com.stockbrokerage.user.model.Account;
import com.stockbrokerage.user.model.AccountType;
import com.stockbrokerage.user.model.User;
import com.stockbrokerage.user.repository.AccountRepository;
import com.stockbrokerage.user.repository.UserRepository;
import com.stockbrokerage.order.exceptions.OrderProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder; // Injected BCrypt encoder

    @Transactional
    public User registerNewUser(String username, String email, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new OrderProcessingException("Username already exists: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        user = userRepository.save(user);

        // Production Detail: Every user needs a default CASH account
        Account defaultAccount = new Account();
        defaultAccount.setUser(user);
        defaultAccount.setType(AccountType.CASH);

        defaultAccount = accountRepository.save(defaultAccount);
        user.getAccounts().add(defaultAccount);

        System.out.println("New User registered and default CASH account created: " + defaultAccount.getAccountNumber());
        return user;
    }

    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }
}