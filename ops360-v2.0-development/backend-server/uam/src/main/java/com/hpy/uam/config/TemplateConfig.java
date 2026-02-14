package com.hpy.uam.config;


import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "templates")
@Data
public class TemplateConfig {

    private Map<String, EmailTemplate> email;
    private Map<String, SmsTemplate> sms;

    @Data
    public static class EmailTemplate {
        private String path;
        private String subject;
    }

    @Data
    public static class SmsTemplate {
        private String message;
        private String dltTemplateId;
    }
}
