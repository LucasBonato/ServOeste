package com.serv.oeste.exception;

import com.serv.oeste.models.enums.Codigo;
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
