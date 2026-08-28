package com.serv.oeste.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SpecialtyRequest(
        @NotBlank(message = "A especialidade precisa de um nome!")
        @Size(min = 2, message = "A especialidade precisa ter no mínimo {min} caracteres!")
        String conhecimento
) {}