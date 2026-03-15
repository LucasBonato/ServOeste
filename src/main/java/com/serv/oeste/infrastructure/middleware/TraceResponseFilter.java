package com.serv.oeste.infrastructure.middleware;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TraceResponseFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        SpanContext spanContext = Span.current().getSpanContext();

        if (spanContext.isValid()) {
            String traceId = spanContext.getTraceId();
            String spanId = spanContext.getSpanId();

            response.setHeader("trace-id", traceId);

            String traceparent = String.format(
                    "00-%s-%s-01",
                    traceId,
                    spanId
            );

            response.setHeader("traceparent", traceparent);
        }
        filterChain.doFilter(request, response);
    }
}
