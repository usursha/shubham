package com.hpy.mappingservice.repository;

import java.util.List;

import com.hpy.mappingservice.entity.UserMaster;

public interface UserMasterCustomRepository {
	int[] bulkInsertOrUpdate(List<UserMaster> users);
}
