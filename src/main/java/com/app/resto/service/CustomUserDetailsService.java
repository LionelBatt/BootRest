package com.app.resto.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.app.resto.repos.UserRepository;
import com.app.resto.model.Users;

import java.util.Collections;

@Service
public class CustomUserDetailsService  implements UserDetailsService {
	
    @Autowired
    private UserRepository repos;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repos.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}