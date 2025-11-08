package com.serv.oeste.domain.exceptions;

import com.serv.oeste.domain.enums.ErrorFields;
import jakarta.persistence.CollectionTable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DomainException extends RuntimeException {
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

    public Set<String> getKeys() {
        return fieldErrors.keySet();
    }

    public Collection<List<String>> getMessages() {
        return fieldErrors.values();
    }

    public Map<String, List<String>> getFieldErrors() {
        return this.fieldErrors;
    }
}