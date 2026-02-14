package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.VendorMatrix;

@Repository
public interface VendorMatrixRepository extends JpaRepository<VendorMatrix, Long> {

	@Query(value = "EXEC USP_GetVendorMatrix :vendor_id,:atmId", nativeQuery = true)
	List<VendorMatrix> getVendorMatrixDetails(@Param("vendor_id") String vendor, @Param("atmId") String atmId);

}
