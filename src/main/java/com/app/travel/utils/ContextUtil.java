package com.app.travel.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.app.travel.model.Users;
import com.app.travel.repos.UserRepository;

@Component
public class ContextUtil {

    @Autowired
    private UserRepository userRepository;

    public boolean isAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }

    public Users getCurrentUser() {
        String username = getCurrentUsername();
        if (username != null) {
            return userRepository.findByUsername(username);
        }
        return null;
    }

    public boolean canAccessUser(Users targetUser) {
        if (targetUser == null) {
            return false;
        }
        if (isAdmin()) {
            return true;
        }
        Users currentUser = getCurrentUser();
        return currentUser != null && currentUser.getUserId() == targetUser.getUserId();
    }

    public boolean canAccessUser(Integer targetUserId) {
        if (targetUserId == null) {
            return false;
        }
        if (isAdmin()) {
            return true;
        }
        Users currentUser = getCurrentUser();
        return currentUser != null && currentUser.getUserId() == targetUserId;
    }
}