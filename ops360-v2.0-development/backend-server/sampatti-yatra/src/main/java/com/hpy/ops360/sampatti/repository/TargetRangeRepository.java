package com.hpy.ops360.sampatti.repository;

import com.hpy.ops360.sampatti.entity.TargetRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TargetRangeRepository extends JpaRepository<TargetRange, Integer> {

    Optional<TargetRange> findFirstByOrderBySrnoAsc();
}
