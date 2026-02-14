package com.hpy.languageconversionservice.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;

public class LanguageCategoryResponse extends GenericDto{
    private List<LanguageCategoryDTO> suggestedLanguage;
    private List<LanguageCategoryDTO> all;

    // Getters and Setters
    public List<LanguageCategoryDTO> getSuggestedLanguage() {
        return suggestedLanguage;
    }

    public void setSuggestedLanguage(List<LanguageCategoryDTO> suggestedLanguage) {
        this.suggestedLanguage = suggestedLanguage;
    }

    public List<LanguageCategoryDTO> getAll() {
        return all;
    }

    public void setAll(List<LanguageCategoryDTO> all) {
        this.all = all;
    }
}