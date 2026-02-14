package com.hpy.languageconversionservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.languageconversionservice.entity.LanguageListEntity;


@Repository
public interface LanguageCategoryRepository extends JpaRepository<LanguageListEntity, Long> {
    List<LanguageListEntity> findByActivated(int activated);
}