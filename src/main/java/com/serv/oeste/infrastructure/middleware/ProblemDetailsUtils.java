package com.serv.oeste.infrastructure.middleware;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

public class ProblemDetailsUtils {
    public static ProblemDetail create(HttpStatusCode status, String detailMessage, Object error) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("error", error);
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, detailMessage);
        detail.setType(getTypeByStatus(status));
        detail.setProperties(properties);
        return detail;
    }

    private static URI getTypeByStatus(HttpStatusCode status) {
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
