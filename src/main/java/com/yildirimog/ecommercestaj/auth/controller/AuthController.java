    package com.yildirimog.ecommercestaj.auth.controller;

    import com.yildirimog.ecommercestaj.auth.dto.AuthenticationResponse;
    import com.yildirimog.ecommercestaj.auth.dto.LoginRequest;
    import com.yildirimog.ecommercestaj.auth.dto.RegisterRequest;
    import com.yildirimog.ecommercestaj.auth.service.JwtService;
    import com.yildirimog.ecommercestaj.common.enums.Role;
    import com.yildirimog.ecommercestaj.user.entity.User;
    import com.yildirimog.ecommercestaj.user.repository.UserRepository;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/api/v1/auth")
    @RequiredArgsConstructor
    public class AuthController {
        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;
        private final UserDetailsService userDetailsService;
        private final PasswordEncoder passwordEncoder;
        private final UserRepository userRepository;
        @PostMapping("/register")
        public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
            // Burada kullanıcı kayıt işlemi yapılır (validasyon, password encode, user kaydetme)
            User user = new User();
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user.setEmail(request.email());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRole(Role.USER);  // default rol

            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);



            return ResponseEntity.ok(new AuthenticationResponse(
                    jwtToken,
                    user.getId(),
                    user.getEmail(),
                    user.getRole().name()
            ));
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@Valid @RequestBody LoginRequest authRequest) {
            try {
                // Kullanıcıyı doğrula
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.email(), authRequest.password())
                );

                // UserDetails al
                User user = (User) authentication.getPrincipal();

                // Token üret
                String token = jwtService.generateToken(user);

                // Response gönder
                return ResponseEntity.ok(new AuthenticationResponse(
                        token,
                        user.getId(),
                        user.getEmail(),
                        user.getRole().name()
                ));

            } catch (BadCredentialsException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz kullanıcı adı veya şifre");
            }
        }
    }
