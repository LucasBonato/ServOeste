package com.serv.oeste.application.exceptions;

import com.serv.oeste.domain.enums.Codigo;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExceptionResponse {
    private Integer idError;
    private String message;

    public ExceptionResponse(Codigo codigo, String message) {
        idError = codigo.getI();
        this.message = message;
    }
}
