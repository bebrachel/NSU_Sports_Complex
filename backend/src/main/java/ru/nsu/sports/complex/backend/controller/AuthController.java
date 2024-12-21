package ru.nsu.sports.complex.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.sports.complex.backend.dto.JwtAuthenticationResponse;
import ru.nsu.sports.complex.backend.dto.SignInRequest;
import ru.nsu.sports.complex.backend.dto.SignUpRequest;
import ru.nsu.sports.complex.backend.service.impl.AuthenticationServiceImpl;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationServiceImpl authenticationService;

    @Operation(summary = "Зарегистрировать нового пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токен", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = JwtAuthenticationResponse.class))
            })
    })
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токен", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = JwtAuthenticationResponse.class))
            })
    })
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}