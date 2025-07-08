    package com.yildirimog.ecommercestaj.security.config;

    import com.yildirimog.ecommercestaj.security.filter.JwtAuthenticationFilter;
    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration
    @RequiredArgsConstructor
    @EnableWebSecurity
    @EnableMethodSecurity
    public class SecurityConfig {
        private final JwtAuthenticationFilter jwtAuthFilter;
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
            return http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            // Auth işlemleri tamamen açık
                            .requestMatchers("/api/v1/auth/**").permitAll()

                            // GET ürün ve kategori sorguları herkese açık
                            .requestMatchers(HttpMethod.GET, "/api/v1/products/**", "/api/v1/categories/**").permitAll()

                            // Kategori oluşturma, sadece ADMIN olabilir
                            .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").hasRole("ADMIN")

                            // Ürün oluşturma, güncelleme, silme işlemleri sadece ADMIN olabilir
                            .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN")

                            // Sipariş işlemleri hem USER hem ADMIN erişebilir
                            .requestMatchers("/api/v1/orders/**").hasAnyRole("USER", "ADMIN")
                            //Payment işlemleri hem USER hem ADMIN erişebilir
                            .requestMatchers("/api/v1/payments/**").hasAnyRole("USER", "ADMIN")
                            // Diğer tüm istekler authentication gerektirir
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(sess -> sess
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
                throws Exception {
            return config.getAuthenticationManager();
        }
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

    }
