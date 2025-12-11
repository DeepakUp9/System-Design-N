package com.rms.application.service;

import com.rms.application.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RmsUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public RmsUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Loads the user from the database based on the provided username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}