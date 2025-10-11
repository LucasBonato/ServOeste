package com.serv.oeste.infrastructure.middleware;

import com.serv.oeste.domain.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DomainException.class)
    public ProblemDetail exceptionHandler(DomainException exception){
        return toProblemDetail(
                getStatusCode(exception),
                exception.getFieldErrors(),
                exception.getMessage()
        );
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
                errors,
                exception.getMessage()
        );

        return new ResponseEntity<>(problemDetail, exception.getStatusCode());
    }

    private ProblemDetail toProblemDetail(HttpStatusCode status, Map<String, List<String>> errors, String message) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("error", errors);

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, message);
        detail.setType(getTypeByStatus(status));
        detail.setProperties(properties);
        return detail;
    }

    private URI getTypeByStatus(HttpStatusCode status) {
        try {
            return new URI(switch (status) {
                case BAD_REQUEST -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.1";
                case UNAUTHORIZED -> "https://datatracker.ietf.org/doc/html/rfc7235#section-3.1";
                case FORBIDDEN -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.3";
                case NOT_FOUND -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.4";
                case METHOD_NOT_ALLOWED -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.5";
                case NOT_ACCEPTABLE -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.6";
                case REQUEST_TIMEOUT -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.7";
                case CONFLICT -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.8";
                case GONE -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.9";
                case PAYLOAD_TOO_LARGE -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.11";
                case UNSUPPORTED_MEDIA_TYPE -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.13";
                case INTERNAL_SERVER_ERROR -> "https://tools.ietf.org/html/rfc7231#section-6.6.1";
                case NOT_IMPLEMENTED -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.6.2";
                case BAD_GATEWAY -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.6.3";
                case SERVICE_UNAVAILABLE -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.6.4";
                case GATEWAY_TIMEOUT -> "https://datatracker.ietf.org/doc/html/rfc7231#section-6.6.5";
                default -> "about:blank";
            });
        }
        catch (URISyntaxException e) {
            return URI.create("about:blank");
        }
    }
}
