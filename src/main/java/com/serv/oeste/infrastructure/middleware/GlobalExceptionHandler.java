package com.serv.oeste.infrastructure.middleware;

import com.serv.oeste.domain.exceptions.*;
import com.serv.oeste.domain.exceptions.auth.AuthTokenExpiredException;
import com.serv.oeste.domain.exceptions.auth.AuthTokenNotValidException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AuthTokenExpiredException.class)
    public ProblemDetail handleExpiredToken() {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Token expirado"
        );
    }

    @ExceptionHandler(AuthTokenNotValidException.class)
    public ProblemDetail handleInvalidToken() {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Token inválido"
        );
    }

    @ExceptionHandler(DomainException.class)
    public ProblemDetail exceptionHandler(DomainException exception){
        return toProblemDetail(
                getStatusCode(exception),
                exception.getFieldErrors()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, List<String>> errors = new HashMap<>();

        exception
                .getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors
                        .computeIfAbsent(
                                error.getField(),
                                key -> new ArrayList<>()
                        )
                        .add(error.getDefaultMessage())
                );

        ProblemDetail problemDetail = toProblemDetail(
                exception.getStatusCode(),
                errors
        );

        return new ResponseEntity<>(problemDetail, exception.getStatusCode());
    }

    private HttpStatusCode getStatusCode(DomainException exception) {
        if (exception instanceof NotFoundException)
            return NOT_FOUND;
        if (exception instanceof InternalServerErrorException)
            return INTERNAL_SERVER_ERROR;
        if (exception instanceof ForbiddenException)
            return FORBIDDEN;
        if (exception instanceof ServiceUnavailableException)
            return SERVICE_UNAVAILABLE;
        if (exception instanceof UnauthorizedException)
            return UNAUTHORIZED;
        return BAD_REQUEST;
    }

    private ProblemDetail toProblemDetail(HttpStatusCode status, Map<String, List<String>> errors) {
        return ProblemDetailsUtils.create(status, "A validation error occurred", errors);
    }
}