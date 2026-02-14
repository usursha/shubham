package com.hpy.ops360.report_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.report_service.entity.UserMtdEntity;

@Repository
public interface UserMtdRepository extends JpaRepository<UserMtdEntity, Long> {

	@Query(value = "SELECT TOP 1 '1' AS srno, date FROM user_mtd ORDER BY date DESC", nativeQuery = true)
	UserMtdEntity findLastTxnUpdatedDate();
}
