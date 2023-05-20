package com.example.demo.migration;

import com.example.demo.Role;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataMigrationComponent {
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        Optional<UserEntity> adminOpt = userRepository.findByEmail("admin@admin.com");
        if (adminOpt.isEmpty()) {
            UserEntity user = new UserEntity();
            user.setName("Admin");
            user.setRole(Role.ADMIN.name());
            user.setEmail("admin@admin.com");
            user.setPassword(BCrypt.hashpw("admin123", org.mindrot.jbcrypt.BCrypt.gensalt(10)));
            user.setDateOfBirth(LocalDateTime.now());
            userRepository.save(user);
        }
    }
}
