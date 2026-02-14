package com.hpy.ops360.dashboard.repository;

import com.hpy.ops360.dashboard.entity.AppConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfigEntity, Integer> {
    // You can add custom query methods if needed
}