package com.example.demo.service;

import com.example.demo.dto.User;
import com.example.demo.entity.UserEntity;
import com.example.demo.exceptions.*;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.Validations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        UserEntity userEntity = UserMapper.INSTANCE.userToUserEntity(user);

        String name = userEntity.getName();
        String email = userEntity.getEmail();
        String password = BCrypt.hashpw(userEntity.getPassword(), org.mindrot.jbcrypt.BCrypt.gensalt(10));
        LocalDateTime dateOfBirth = userEntity.getDateOfBirth() == null ? LocalDateTime.now().minusYears(20) : userEntity.getDateOfBirth();

        if (!(Validations.validateEmail(email) &&
                Validations.validatePassword(password) &&
                Validations.validateDateOfBirth(dateOfBirth))) {
            throw new BadRequestException("Invalid input");
        }
        userEntity = userRepository.save(new UserEntity(name, email, password, dateOfBirth, user.getRole()));
        return UserMapper.INSTANCE.userEntityToUser(userEntity);
    }

    public User login(User user) {
        String email = user.getEmail();
        User foundUser = getUserByEmail(email);
        if (foundUser == null) {
            throw new UserNotFoundException("User not found");
        }
        if (!BCrypt.checkpw(user.getPassword(), foundUser.getPassword())) {
            throw new InvalidPasswordException("The password doesn't match the email");
        }
        return foundUser;
    }

    public User updateUser(long id, User updatedUser) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id));

        // Validate name
        if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
            String name = updatedUser.getName().trim();
            if (!name.matches("[A-Za-z ]+")) {
                throw new BadRequestException("Invalid name. Only alphabets and spaces are allowed.");
            }
            user.setName(name);
        }

        // Validate email
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            String email = updatedUser.getEmail().trim().toLowerCase();
            if (!Validations.validateEmail(email)) {
                throw new BadRequestException("Invalid email address.");
            }

            Optional<UserEntity> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent() && !Objects.equals(existingUser.get().getId(), user.getId())) {
                throw new ConflictException("Email address already in use.");
            }
            user.setEmail(email);
        }

        // Validate date of birth
        if (updatedUser.getDateOfBirth() != null) {
            LocalDateTime dob = updatedUser.getDateOfBirth();
            if (dob.isAfter(LocalDateTime.now())) {
                throw new BadRequestException("Date of birth cannot be in the future.");
            }
            if (!Validations.validateDateOfBirth(dob)) {
                throw new IllegalArgumentException("Must be older than 15 years");
            }
            user.setDateOfBirth(dob);
        }

        // Validate and Update password
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            if (!Validations.validatePassword(updatedUser.getPassword())) {
                throw new InvalidPasswordException("Invalid password");
            }
            user.setPassword(updatedUser.getPassword());
        }

        // Validate role
        if (updatedUser.getRole() != null && !updatedUser.getRole().isEmpty()) {
            String role = updatedUser.getRole().trim().toLowerCase();
            if (!role.equals("admin") && !role.equals("user")) {
                throw new BadRequestException("Invalid role. Allowed values: 'admin' or 'user'.");
            }
            user.setRole(role);
        }

        return UserMapper.INSTANCE.userEntityToUser(userRepository.save(user));
    }

    public User getUserByEmail(String email) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        return optionalUser
                .map(UserMapper.INSTANCE::userEntityToUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found " + email));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::userEntityToUser).toList();
    }

    public User getUserById(Long id) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        return optionalUser
                .map(UserMapper.INSTANCE::userEntityToUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findById(Long id) {
        return UserMapper.INSTANCE.userEntityToUser(userRepository.findById(id).get());
    }

    public User find() {
        UserEntity userEntity = userRepository.findByEmail(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
                .orElseThrow();
        return UserMapper.INSTANCE.userEntityToUser(userEntity);
    }
}
