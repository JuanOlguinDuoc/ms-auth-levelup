package com.levelup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import com.levelup.model.User;
import com.levelup.repository.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    
    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        List<GrantedAuthority> auths = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())
        );

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            auths
        );
    }
}
