package com.hpy.ops360.ticketing.route;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenTicketsRepository extends JpaRepository<GetOpenTicket, String>{

	
}
