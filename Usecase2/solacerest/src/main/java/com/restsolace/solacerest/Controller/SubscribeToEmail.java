package com.restsolace.solacerest.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;




import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class SubscribeToEmail {
    
    // ApiServiceImpl apiService;

    
    @PostMapping("/HPI/certUpdate")
    public ResponseEntity<String> saveUser(@Valid @RequestBody String email) {
        // emailService.sendEmail(email);
        return new ResponseEntity<>("Emailposted", HttpStatus.CREATED);
    }
    
}
