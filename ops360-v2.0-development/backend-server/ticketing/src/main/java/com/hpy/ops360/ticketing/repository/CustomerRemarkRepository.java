package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.CustomerRemark;

@Repository
public interface CustomerRemarkRepository extends JpaRepository<CustomerRemark, Long> {

	@Query(value = "EXEC Usp_get_customer_remarks :username,:owner_name,:broad_category", nativeQuery = true)
	List<CustomerRemark> getCustomerRemarks(@Param("username") String userId, @Param("owner_name") String owner, @Param("broad_category") String broadCategory);

}
