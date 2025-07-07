package com.yildirimog.ecommercestaj.auth.service;

import com.yildirimog.ecommercestaj.auth.dto.AuthenticationResponse;
import com.yildirimog.ecommercestaj.auth.dto.LoginRequest;
import com.yildirimog.ecommercestaj.auth.dto.RegisterRequest;
import com.yildirimog.ecommercestaj.common.enums.Role;
import com.yildirimog.ecommercestaj.user.entity.User;
import com.yildirimog.ecommercestaj.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request){
        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new IllegalArgumentException("Email already exists");
        }
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
    public AuthenticationResponse authenticate(LoginRequest request) {
        // AuthenticationManager ile giriş denetimi
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // Kullanıcıyı bul
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Token oluştur
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }
}
