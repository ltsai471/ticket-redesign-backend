package com.ticket.ticket_system.aspect;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class ApiMonitoringAspect {

    private final MeterRegistry registry;

    public ApiMonitoringAspect(MeterRegistry registry) {
        this.registry = registry;
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController) || @annotation(org.springframework.web.bind.annotation.RestController)")
    public Object monitorApi(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String endpoint = request.getMethod() + " " + request.getRequestURI();
        
        Timer timer = Timer.builder("api.request.duration")
                .tag("endpoint", endpoint)
                .description("API request duration")
                .register(registry);

        Counter successCounter = Counter.builder("api.request.success")
                .tag("endpoint", endpoint)
                .description("Successful API requests")
                .register(registry);

        Counter failureCounter = Counter.builder("api.request.failure")
                .tag("endpoint", endpoint)
                .description("Failed API requests")
                .register(registry);

        try {
            Object result = timer.record(() -> {
                try {
                    return joinPoint.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });
            successCounter.increment();
            return result;
        } catch (Exception e) {
            failureCounter.increment();
            Counter errorCounter = Counter.builder("api.request.error")
                    .tag("endpoint", endpoint)
                    .tag("error", e.getClass().getSimpleName())
                    .description("API request errors by type")
                    .register(registry);
            errorCounter.increment();
            throw e;
        }
    }
} 