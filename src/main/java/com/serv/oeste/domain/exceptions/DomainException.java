package com.serv.oeste.domain.exceptions;

import com.serv.oeste.domain.enums.ErrorFields;

import java.util.List;
import java.util.Map;

public class DomainException extends RuntimeException{
    private final Map<String, List<String>> fieldErrors;

    public DomainException(ErrorFields field, List<String> messages) {
        this.fieldErrors = Map.of(field.getFieldName(), messages);
    }

    public DomainException(ErrorFields field, String message) {
        this.fieldErrors = Map.of(field.getFieldName(), List.of(message));
    }

    public DomainException(Map<String, List<String>> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public static DomainException of(ErrorFields field, String message) {
        return new DomainException(Map.of(field.getFieldName(), List.of(message)));
    }

    @Override
    public String getMessage() {
        return fieldErrors.toString();
    }

    public Map<String, List<String>> getFieldErrors() {
        return this.fieldErrors;
    }
}