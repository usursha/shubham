package com.hpy.mappingservice.repository;

import java.util.List;

import com.hpy.mappingservice.entity.AtmMaster;

public interface AtmMasterCustomRepository {
	
	void bulkInsertOrUpdate(List<AtmMaster> atmMasters);

}
