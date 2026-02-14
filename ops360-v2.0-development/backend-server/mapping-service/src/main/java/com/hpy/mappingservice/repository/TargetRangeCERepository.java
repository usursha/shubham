package com.hpy.mappingservice.repository;

import com.hpy.mappingservice.entity.TargetRangeCE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TargetRangeCERepository extends JpaRepository<TargetRangeCE, Integer> {

    Optional<TargetRangeCE> findFirstByOrderBySrnoAsc();
}
