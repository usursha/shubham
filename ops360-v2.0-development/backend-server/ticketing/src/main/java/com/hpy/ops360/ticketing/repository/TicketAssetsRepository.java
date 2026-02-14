package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hpy.generic.IGenericRepo;
import com.hpy.ops360.ticketing.entity.TicketAssets;

@Repository
public interface TicketAssetsRepository extends IGenericRepo<TicketAssets> {

	List<TicketAssets> findAllByRemarkId(Long remarkId);

}
