package com.serv.oeste.infrastructure.middleware;

import com.serv.oeste.domain.exceptions.*;
import com.serv.oeste.domain.exceptions.auth.AuthTokenExpiredException;
import com.serv.oeste.domain.exceptions.auth.AuthTokenNotValidException;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthTokenExpiredException.class)
    public ProblemDetail handleExpiredToken() {
        logDomainException("auth.token-expired", UNAUTHORIZED);
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Token expirado"
        );
    }

    @ExceptionHandler(AuthTokenNotValidException.class)
    public ProblemDetail handleInvalidToken() {
        logDomainException("auth.token-invalid", UNAUTHORIZED);
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Token inválido"
        );
    }

    @ExceptionHandler(DomainException.class)
    public ProblemDetail exceptionHandler(DomainException exception){
        HttpStatusCode statusCode = getStatusCode(exception);
        logDomainException(
                "domain.exception",
                statusCode,
                exception.getClass().getSimpleName(),
                exception.getFieldErrors()
        );

        return toProblemDetail(
                statusCode,
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

        logDomainException(
                "validation.exception",
                exception.getStatusCode(),
                exception.getClass().getSimpleName(),
                errors
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

    private void logDomainException(String code, HttpStatusCode status) {
        logDomainException(code, status, null, null);
    }

    private void logDomainException(
            String code,
            HttpStatusCode status,
            String exceptionType,
            Map<String, List<String>> errors
    ) {
        SpanContext spanContext = Span.current().getSpanContext();
        String traceId = spanContext.isValid() ? spanContext.getTraceId() : "N/A";

        LOGGER.warn(
                "exception.code={} status={} exceptionType={} traceId={} errors={}",
                code,
                status.value(),
                exceptionType,
                traceId,
                errors
        );
    }
}