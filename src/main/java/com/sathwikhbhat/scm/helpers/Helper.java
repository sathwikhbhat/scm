package com.sathwikhbhat.scm.helpers;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.security.Principal;
import java.util.Map;

public class Helper {

    public static String getEmailOfLoggedInUser(Principal principal) {

        if (principal instanceof OAuth2AuthenticationToken authToken) {
            OAuth2User oauth2User = authToken.getPrincipal();
            Map<String, Object> attributes = oauth2User.getAttributes();

            if (attributes.containsKey("email"))
                return (String) attributes.get("email");

            if (attributes.containsKey("login"))
                return attributes.get("login") + "@github.com";

            if (attributes.containsKey("name"))
                return (String) attributes.get("name");
        }

        if (principal instanceof UserDetails userDetails)
            return userDetails.getUsername();

        return principal.getName();
    }
}