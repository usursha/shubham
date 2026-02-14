package com.hpy.mappingservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hpy.mappingservice.entity.ResetReasonDropdown;

@Repository
public interface ResetReasonDropdownRepository extends JpaRepository<ResetReasonDropdown, Long> {
    List<ResetReasonDropdown> findByIsActiveTrue();
}
