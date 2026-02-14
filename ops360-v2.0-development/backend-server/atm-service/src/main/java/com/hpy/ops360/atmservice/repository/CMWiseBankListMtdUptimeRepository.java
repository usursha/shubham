package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.dto.CMWiseBankListMtdUptimeReqDto;
import com.hpy.ops360.atmservice.entity.CMWiseBankListMtdUptimeEntity;
import com.hpy.ops360.atmservice.entity.CeBankWiseMTDEntity;


@Repository
public interface CMWiseBankListMtdUptimeRepository extends JpaRepository<CMWiseBankListMtdUptimeEntity, Long>{
	@Query(value ="EXEC ops_cm_bank_list_with_mtd_uptime :cm_user_id ", nativeQuery = true)
    List<CMWiseBankListMtdUptimeEntity> getBankListDirect(@Param("cm_user_id") String cmUserId);
    
}