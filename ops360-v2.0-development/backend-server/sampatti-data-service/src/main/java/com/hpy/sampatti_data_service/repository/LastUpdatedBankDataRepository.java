package com.hpy.sampatti_data_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hpy.sampatti_data_service.entity.LastUpdatedBankData;

@Repository
public interface LastUpdatedBankDataRepository extends JpaRepository<LastUpdatedBankData, Integer> {

	@Query(value="EXEC usp_sampatti_last_updated_bank_data",nativeQuery = true)
	public List<LastUpdatedBankData> getBankUpdatedDataFromSp();
}
