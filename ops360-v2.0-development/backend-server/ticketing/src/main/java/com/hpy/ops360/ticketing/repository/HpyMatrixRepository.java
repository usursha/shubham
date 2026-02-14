package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.HpyMatrix;

@Repository
public interface HpyMatrixRepository extends JpaRepository<HpyMatrix, Long> {

	@Query(value = "EXEC USP_GetHpyMatrix :atm_id,:user_id", nativeQuery = true)
	List<HpyMatrix> getHpyMatrixDetails(@Param("atm_id") String atmdId, @Param("user_id") String userId);
}
