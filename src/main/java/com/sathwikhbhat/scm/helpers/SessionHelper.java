package com.sathwikhbhat.scm.helpers;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Slf4j
@Component
public class SessionHelper {

    public static void removeMessage() {
        try {
            HttpSession session = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                    .getRequest()
                    .getSession();
            session.removeAttribute("message");
            log.info("Message removed from session");
        } catch (Exception e) {
            log.error("Error removing message from session: {}", e.getMessage());
        }
    }
}