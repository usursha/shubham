package com.hpy.mappingservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.UserMapping;

@Repository
public interface UserRepository extends JpaRepository<UserMapping, Long> {
	
	Optional<UserMapping> findByUserId(Long userId);

}