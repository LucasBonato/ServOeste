package com.serv.oeste.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExceptionResponse {
    private Integer idError;
    private String message;
}
