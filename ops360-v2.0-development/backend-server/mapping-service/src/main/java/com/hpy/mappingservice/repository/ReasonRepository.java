package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.Reason;

@Repository
public interface ReasonRepository extends JpaRepository<Reason, Integer> {

   
    List<Reason> findByIsActiveTrue();
}