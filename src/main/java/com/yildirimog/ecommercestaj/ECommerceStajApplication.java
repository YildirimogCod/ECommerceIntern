package com.yildirimog.ecommercestaj;

import com.yildirimog.ecommercestaj.common.enums.Role;
import com.yildirimog.ecommercestaj.user.entity.User;
import com.yildirimog.ecommercestaj.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ECommerceStajApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceStajApplication.class, args);
    }
    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (!userRepository.existsByEmail("admin@ecommerce.com")) {
                User admin = new User();
                admin.setFirstName("Admin");
                admin.setLastName("Admin");
                admin.setEmail("admin@ecommerce.com");
                admin.setRole(Role.ADMIN);
                admin.setPassword(encoder.encode("admin123"));

                userRepository.save(admin);
            }
        };
    }


}
