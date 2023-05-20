package com.example.demo.dto;

import com.example.demo.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime dateOfBirth;
    private String role = Role.USER.name();
    private List<User> followers;
    private List<User> followings;
}
