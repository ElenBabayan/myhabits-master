package com.example.demo.controller;

import com.example.demo.dto.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.token.TokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class AuthenticationController {
    private final UserService userService;
    private final TokenProvider jwsGenerator = new TokenProvider();

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody User user) {
        User newUser = userService.registerUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User loggedUser = userService.login(user);

        // Generate the JWT token for the user
        String token = jwsGenerator.generateJws(UserMapper.INSTANCE.userToUserEntity(loggedUser));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        return ResponseEntity.ok().headers(headers).body(loggedUser);
    }
}
