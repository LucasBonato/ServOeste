package com.serv.oeste.domain.exceptions;


import com.serv.oeste.domain.enums.ErrorFields;

import java.util.*;

public class ErrorCollector {

    private final Map<String, List<String>> errors = new HashMap<>();

    public void add(ErrorFields field, String message) {
        errors.computeIfAbsent(field.getFieldName(), string -> new ArrayList<>()).add(message);
    }

    public void addAll(Map<ErrorFields, List<String>> newErrors) {
        newErrors.forEach((field, messages) ->
                messages.forEach(msg -> add(field, msg))
        );
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public Map<String, List<String>> getErrors() {
        return Collections.unmodifiableMap(errors);
    }

    public void throwIfAny() {
        if (hasErrors()) throw new DomainException(errors);
    }

    public void throwIfAny(java.util.function.Function<Map<String, List<String>>, ? extends DomainException> exceptionFactory) {
        if (hasErrors()) throw exceptionFactory.apply(errors);
    }
}
