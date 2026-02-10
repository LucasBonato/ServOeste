package com.serv.oeste.domain.exceptions.external;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.InternalServerErrorException;

public class ExternalNetworkException extends InternalServerErrorException {
    public ExternalNetworkException(ErrorFields field, String message) {
        super(field, message);
    }
}
