package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.AbsenceSlot;

@Repository
public interface AbsenceSlotRepository extends JpaRepository<AbsenceSlot, Integer> {

    /**
     * Finds all AbsenceSlot entities where the isActive flag is true.
     * This is a custom query method provided by Spring Data JPA.
     * @return a list of active AbsenceSlot objects.
     */
    List<AbsenceSlot> findByIsActiveTrue();
}