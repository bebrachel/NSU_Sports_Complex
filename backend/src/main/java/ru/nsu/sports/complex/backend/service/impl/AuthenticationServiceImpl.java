package ru.nsu.sports.complex.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.sports.complex.backend.dto.JwtAuthenticationResponse;
import ru.nsu.sports.complex.backend.dto.SignInRequest;
import ru.nsu.sports.complex.backend.dto.SignUpRequest;
import ru.nsu.sports.complex.backend.model.User;
import ru.nsu.sports.complex.backend.service.JwtService;
import ru.nsu.sports.complex.backend.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = new User(request.getName(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        userService.createUser(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getEmail());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}