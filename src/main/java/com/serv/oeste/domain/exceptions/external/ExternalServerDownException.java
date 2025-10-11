package com.serv.oeste.domain.exceptions.external;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.ServiceUnavailableException;

public class ExternalServerDownException extends ServiceUnavailableException {
    public ExternalServerDownException(ErrorFields field, String message) {
        super(field, message);
    }
}
