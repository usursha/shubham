package com.hpy.languageconversionservice.service;

import java.util.Locale;
import java.util.Properties;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class ExposedResourceMessageBundleSource extends ReloadableResourceBundleMessageSource {
    public Properties getMessages(Locale locale) {
        return getMergedProperties(locale).getProperties();
    }
}