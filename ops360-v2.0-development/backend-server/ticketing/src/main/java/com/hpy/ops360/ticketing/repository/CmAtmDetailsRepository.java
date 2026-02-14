package com.hpy.ops360.ticketing.repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.AtmDetails;

@Repository
public class CmAtmDetailsRepository {//extends JpaRepository<CmAtmDetails, Long> {

//	@Query(value = "EXEC UPS_GetCM_CEAtmDetails :user_id", nativeQuery = true)
//	List<CmAtmDetails> getCmAtmDetails(@Param("user_id") String userId);
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

    public List<AtmDetails> getCmAtmDetails(String userId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("UPS_GetCM_CEAtmDetails")
            .declareParameters(new SqlParameter("user_id", Types.VARCHAR))
            .returningResultSet("resultSet",
                (rs, rowNum) -> {
                	AtmDetails atmDetails = new AtmDetails();
                   // atmDetails.setS(rs.getInt("sr_no"));
                    atmDetails.setSrNo(rs.getLong("sr_no"));
                    atmDetails.setAtmCode(rs.getString("atm_code"));
                    return atmDetails;
                });

        Map<String, Object> result = jdbcCall.execute(Map.of("user_id", userId));

        return (List<AtmDetails>) result.get("resultSet");
    }
}
