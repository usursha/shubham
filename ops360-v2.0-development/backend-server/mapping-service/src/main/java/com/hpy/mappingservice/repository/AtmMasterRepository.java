package com.hpy.mappingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.AtmMaster;

@Repository
public interface AtmMasterRepository extends JpaRepository<AtmMaster, Long> {


}
