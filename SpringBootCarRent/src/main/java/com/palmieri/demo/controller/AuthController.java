package com.palmieri.demo.controller;

import com.palmieri.demo.config.security.jwt.JwtUtils;
import com.palmieri.demo.entities.User;
import com.palmieri.demo.jwt.request.LoginRequest;
import com.palmieri.demo.jwt.response.JwtResponse;
import com.palmieri.demo.service.CustomUserDetailsService;
import com.palmieri.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping(value = "/signin", produces = "application/json")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {

        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        final UserDetails userDetails = customUserDetailsService
                .loadUserByUsername(loginRequest.getUsername());
        User user = userService.readByUsername(userDetails.getUsername());
        final String token = jwtUtils.generateToken(userDetails);




        return ResponseEntity.ok(new JwtResponse(token,"Bearer", user));
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

