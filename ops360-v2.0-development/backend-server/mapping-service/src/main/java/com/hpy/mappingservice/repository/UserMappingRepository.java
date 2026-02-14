package com.hpy.mappingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.UserMapping;

@Repository
public interface UserMappingRepository extends JpaRepository<UserMapping, Long> {
}
