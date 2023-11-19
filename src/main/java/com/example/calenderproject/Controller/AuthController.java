package com.example.calenderproject.Controller;

import com.example.calenderproject.Dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "login success";
    }
}
