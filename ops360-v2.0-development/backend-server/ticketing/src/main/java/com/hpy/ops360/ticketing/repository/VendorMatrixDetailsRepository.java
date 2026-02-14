package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.VendorMatrixDetails;

@Repository
public interface VendorMatrixDetailsRepository extends JpaRepository<VendorMatrixDetails, Long> {
	
	@Query(value="EXEC USP_GetVendorMatrixDetails :vendor_id,:atmId,:vender_name",nativeQuery = true)
	VendorMatrixDetails getVendorContactDetails(@Param("vendor_id") String vendor,@Param("atmId") String atmId,@Param("vender_name") String vendorName);

}
