package com.sathwikhbhat.scm.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("${CLOUDINARY_API_KEY}")
    private String apiKey;

    @Value("${CLOUDINARY_API_SECRET}")
    private String apiSecret;

    @Value("${CLOUDINARY_CLOUD_NAME}")
    private String cloudName;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(String.format("cloudinary://%s:%s@%s", apiKey, apiSecret, cloudName));
    }

}