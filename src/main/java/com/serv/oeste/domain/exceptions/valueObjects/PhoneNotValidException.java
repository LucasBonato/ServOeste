package com.serv.oeste.domain.exceptions.valueObjects;

import com.serv.oeste.domain.exceptions.NotValidException;

import java.util.List;
import java.util.Map;

public class PhoneNotValidException extends NotValidException {
    public PhoneNotValidException(Map<String, List<String>> fieldErrors) {
        super(fieldErrors);
    }
}
