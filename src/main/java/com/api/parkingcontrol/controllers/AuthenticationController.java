package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.form.LoginForm;
import com.api.parkingcontrol.dto.TokenDto;
import com.api.parkingcontrol.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/loggin")
public class AuthenticationController {

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<?> authenticationUser(@RequestBody @Valid LoginForm form) {
        UsernamePasswordAuthenticationToken loginData = form.convert();

        try {
            Authentication authentication = authenticationManager.authenticate(loginData);
            String token = tokenService.createToken(authentication);
            return ResponseEntity.ok(new TokenDto(token, "Bearer"));

        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("senha errada");
        }
    }
}
