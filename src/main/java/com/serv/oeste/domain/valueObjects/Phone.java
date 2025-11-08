package com.serv.oeste.domain.valueObjects;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.ErrorCollector;
import com.serv.oeste.domain.exceptions.valueObjects.PhoneNotValidException;

public class Phone {
    private final String value;

    private Phone(String value) {
        this.value = sanitize(value);
        validate();
    }

    public static Phone of(String value) {
        return new Phone(value);
    }

    public String getPhone() {
        return this.value;
    }

    public boolean isPhoneBlank() {
        return this.value.isBlank();
    }

    private String sanitize(String raw) {
        if (raw == null) return null;

        return raw.replaceAll("[^0-9]", "");
    }

    private void validate() {
        ErrorCollector errors = new ErrorCollector();

        if (value == null || value.isBlank()) {
            errors.add(ErrorFields.TELEFONES, "O telefone não pode ser vazio!");
        }
        else if (!value.matches("\\d{10,11}")) {
            errors.add(ErrorFields.TELEFONES, "Telefone deve conter 10 ou 11 dígitos numéricos válidos!");
        }

        errors.throwIfAny(PhoneNotValidException::new);
    }
}
