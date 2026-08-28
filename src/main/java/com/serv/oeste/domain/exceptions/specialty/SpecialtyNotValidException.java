package com.serv.oeste.domain.exceptions.specialty;

import com.serv.oeste.domain.exceptions.NotValidException;

import java.util.List;
import java.util.Map;

public class SpecialtyNotValidException extends NotValidException {
    public SpecialtyNotValidException(Map<String, List<String>> fieldErrors) {
        super(fieldErrors);
    }
}