package com.hpy.ops360.dashboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.PortalPrivacyPolicyDetails;



@Repository
public interface PortalPrivacyPolicyRepository extends JpaRepository<PortalPrivacyPolicyDetails,Long> {
	 Optional<PortalPrivacyPolicyDetails> findTopByOrderByCreatedAtDesc();

}
