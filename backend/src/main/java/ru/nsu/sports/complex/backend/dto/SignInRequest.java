package ru.nsu.sports.complex.backend.dto;

import lombok.Data;

@Data
public class SignInRequest {

    private String email;

    private String password;
}