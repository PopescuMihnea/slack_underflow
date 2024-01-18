package com.slackunderflow.slackunderflow;

import com.slackunderflow.slackunderflow.models.Role;
import com.slackunderflow.slackunderflow.models.UserEntity;
import com.slackunderflow.slackunderflow.repositories.RoleRepository;
import com.slackunderflow.slackunderflow.repositories.UserEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class SlackunderflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlackunderflowApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        return arg -> {
            if (roleRepository.findByAuthority("ADMIN").isPresent()) return;
            Role adminRole = roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("USER"));
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            UserEntity admin = UserEntity
                    .builder()
                    .id(0L)
                    .username("atmin")
                    .password(passwordEncoder.encode("1234"))
                    .authorities(roles)
                    .build();

            userEntityRepository.save(admin);
        };
    }

}
