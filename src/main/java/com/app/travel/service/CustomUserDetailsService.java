package com.app.travel.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.travel.model.Users;
import com.app.travel.repos.UserRepository;

@Service
public class CustomUserDetailsService  implements UserDetailsService {
	
    @Autowired
    private UserRepository repos;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repos.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        
        List<GrantedAuthority> authorities;
        if (user.getRole() != null) {
            authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        } else {
            // Rôle par défaut si pas de rôle défini
            authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}