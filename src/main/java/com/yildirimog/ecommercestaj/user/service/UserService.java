package com.yildirimog.ecommercestaj.user.service;

import com.yildirimog.ecommercestaj.user.dto.UserCreateRequest;
import com.yildirimog.ecommercestaj.user.entity.User;
import com.yildirimog.ecommercestaj.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(UserCreateRequest request){
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .build();

        return userRepository.save(user);
    }
}
