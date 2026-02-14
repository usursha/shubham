package com.hpy.mappingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.AtmEntity;

@Repository
public interface AtmRepository extends JpaRepository<AtmEntity, Long> {
	
	@Query(value = "EXEC usp_find_atmid :atmids",nativeQuery = true)
	public AtmEntity findAtmById(@Param("atmids") String atmIds);

}
