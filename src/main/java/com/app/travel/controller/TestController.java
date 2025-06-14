package com.app.travel.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.dto.ApiResponse;

@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {
    @GetMapping("/all")
    public ApiResponse<String> allAccess() {
        return ApiResponse.success("Accès autorisé", "Contenu public disponible");
    }
    
    @GetMapping("/user")
    public ApiResponse<String> userAccess() {
        return ApiResponse.success("Accès utilisateur autorisé", "Contenu utilisateur disponible");
    }
}