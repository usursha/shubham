package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hpy.generic.IGenericRepo;
import com.hpy.ops360.ticketing.entity.RemarkAssets;

@Repository
public interface RemarkAssetsRepository extends IGenericRepo<RemarkAssets> {

	List<RemarkAssets> findAllByRemarkId(Long remarkId);

}
