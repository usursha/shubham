package com.hpy.ops360.sampatti.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.sampatti.entity.GuidelineDetails;


@Repository
public interface GuidelineRepository extends JpaRepository<GuidelineDetails, Long> {

	 Optional<GuidelineDetails> findTopByOrderByCreatedAtDesc();
	 

	
	
	

}