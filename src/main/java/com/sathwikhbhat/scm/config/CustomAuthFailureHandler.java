package com.sathwikhbhat.scm.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        if (exception instanceof BadCredentialsException) {
            handleBadCredentials(response, exception.getMessage());
            return;
        }

        Throwable cause = exception.getCause();
        if (cause instanceof BadCredentialsException) {
            handleBadCredentials(response, cause.getMessage());
            return;
        }

        response.sendRedirect("/login?error=invalid");
    }

    private void handleBadCredentials(HttpServletResponse response, String message) throws IOException {
        if (message.contains("GOOGLE")) {
            response.sendRedirect("/login?error=provider_mismatch&expected=GOOGLE&used=SELF");
        } else if (message.contains("GITHUB")) {
            response.sendRedirect("/login?error=provider_mismatch&expected=GITHUB&used=SELF");
        } else {
            response.sendRedirect("/login?error=invalid");
        }
    }
}