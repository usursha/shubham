package com.hpy.mappingservice.repository.bulkrepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.bulkentity.LeaveRequestAlt;

@Repository
public interface LeaveRequestAltRepository extends JpaRepository<LeaveRequestAlt, Long> {
    Optional<LeaveRequestAlt> findById(Long id);
}
