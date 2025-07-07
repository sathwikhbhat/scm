package com.sathwikhbhat.scm.config;

import com.sathwikhbhat.scm.entity.User;
import com.sathwikhbhat.scm.enums.Providers;
import com.sathwikhbhat.scm.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@Transactional
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();

        String registrationId = oauthToken.getAuthorizedClientRegistrationId().toUpperCase();
        Providers provider;

        try {
            provider = Providers.valueOf(registrationId);
        } catch (IllegalArgumentException ex) {
            log.error("Unsupported OAuth provider: {}", registrationId);
            response.sendRedirect("/login?error=unsupported_provider");
            return;
        }

        Map<String, Object> attributes = oauthUser.getAttributes();
        String email = null;
        String name = null;
        String pictureUrl = null;
        String providerId = null;

        if (provider == Providers.GOOGLE) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            pictureUrl = (String) attributes.get("picture");
            providerId = (String) attributes.get("sub");
        }
        if (provider == Providers.GITHUB) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            if (name == null || name.isEmpty())
                name = (String) attributes.get("login");
            pictureUrl = (String) attributes.get("avatar_url");
            providerId = String.valueOf(attributes.get("id"));
        }

        if (email == null || providerId == null) {
            log.error("Missing required user info: email={}, providerId={}", email, providerId);
            response.sendRedirect("/login?error=missing_info");
            return;
        }

        email = email.toLowerCase().trim();

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            if (userRepository.existsByEmailAndProviderNot(email, provider)) {
                log.error("Email {} already exists with a different provider", email);
                response.sendRedirect("/login?error=provider_mismatch&expected=UNKNOWN&used=" + provider.name());
                return;
            }

            User newUser = User.builder()
                    .userId(UUID.randomUUID().toString())
                    .email(email)
                    .name(name)
                    .password(null)
                    .profilePictureUrl(pictureUrl)
                    .provider(provider)
                    .providerId(providerId)
                    .enabled(true)
                    .emailVerified(true)
                    .roles(List.of("USER"))
                    .build();

            userRepository.save(newUser);
            log.info("New user registered via {}: {}", provider.name(), email);
        } else {
            User existingUser = userOpt.get();

            if (!existingUser.getProvider().equals(provider)) {
                String expected = existingUser.getProvider().name();
                String used = provider.name();

                if (existingUser.getProvider() == Providers.SELF)
                    log.warn("OAuth login attempted on SELF-registered user: {}", email);

                response.sendRedirect("/login?error=provider_mismatch&expected=" + expected + "&used=" + used);
                return;
            }

            boolean updated = false;

            if (name != null && !name.equals(existingUser.getName())) {
                existingUser.setName(name);
                updated = true;
            }

            if (pictureUrl != null && !pictureUrl.equals(existingUser.getProfilePictureUrl())) {
                existingUser.setProfilePictureUrl(pictureUrl);
                updated = true;
            }

            if (updated) {
                userRepository.save(existingUser);
                log.info("Updated user info for: {}", email);
            }
        }

        log.info("OAuth login successful for user: {} [{}]", email, provider.name());
        response.sendRedirect("/user/dashboard");
    }
}