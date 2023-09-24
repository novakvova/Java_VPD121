package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.configuration.security.JwtService;
import org.example.dto.account.AuthResponseDto;
import org.example.dto.account.LoginDto;
import org.example.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponseDto login(LoginDto request) {
        var user = userRepository.findByEmail(request.getEmail());
        if(user.isEmpty())
            throw new UsernameNotFoundException("Користувача не знайдено");

        var userItem = user.get();
        var isValid = passwordEncoder.matches(request.getPassword(), userItem.getPassword());
        if(!isValid) {
            throw new UsernameNotFoundException("Користувача не знайдено");
        }
        var jwtToken = jwtService.generateAccessToken(userItem);
        return AuthResponseDto.builder()
                .token(jwtToken)
                .build();
    }
}
