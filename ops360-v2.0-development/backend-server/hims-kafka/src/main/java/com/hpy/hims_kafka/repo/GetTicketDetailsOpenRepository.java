package com.hpy.hims_kafka.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.hims_kafka.entity.GetTicketDetailsOpen;

@Repository
public interface GetTicketDetailsOpenRepository extends JpaRepository<GetTicketDetailsOpen, String> {}

