package com.hpy.ops360.atmservice.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.CategoryOption;
import com.hpy.ops360.atmservice.dto.CreationDateOption;
import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.dto.OwnerOption;
import com.hpy.ops360.atmservice.dto.SortOption;
import com.hpy.ops360.atmservice.dto.StatusOption;
import com.hpy.ops360.atmservice.dto.SubCallTypeOption;
import com.hpy.ops360.atmservice.dto.TicketAgingDayOption;
import com.hpy.ops360.atmservice.dto.TicketAgingHourOption;
import com.hpy.ops360.atmservice.dto.TicketAgingOption;
import com.hpy.ops360.atmservice.dto.TicketFilterOptionsResponse;
import com.hpy.ops360.atmservice.dto.TicketTypeOption;
import com.hpy.ops360.atmservice.dto.VendorOption;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndexOFAtmTicketFilterService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private SimpleJdbcCall simpleJdbcCall;

	@Autowired
	private LoginService loginService;

	public IndexOFAtmTicketFilterService(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);

		// Initialize SimpleJdbcCall
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource)
				.withProcedureName("usp_get_ticket_filter_options_with_counts")
				// Declare all result sets in the exact order they are returned by the SP
				.returningResultSet("SortOptions", new SortOptionRowMapper())
				.returningResultSet("TicketTypeData", new TicketTypeOptionRowMapper())
				.returningResultSet("CategoryCounts", new CategoryOptionRowMapper()) // Changed to CategoryCounts to
																						// match SP's CTE name
				.returningResultSet("SubCallTypeOptions", new SubCallTypeOptionRowMapper())
				.returningResultSet("StatusData", new StatusOptionRowMapper())
				.returningResultSet("AgingDataHr", new TicketAgingHrOptionRowMapper()) // Renamed to AgingDataHr
				.returningResultSet("AgingDataDays", new TicketAgingDayOptionRowMapper()) // Renamed to AgingDataDays
				.returningResultSet("OwnerOptions", new OwnerOptionRowMapper())
				.returningResultSet("VendorOptions", new VendorOptionRowMapper())
				.returningResultSet("DateData", new CreationDateOptionRowMapper()); // Renamed to DateData

		log.info("Initializing SimpleJdbcCall for stored procedure: usp_get_ticket_filter_options_with_counts");
		log.info("SimpleJdbcCall initialized successfully with multiple result sets.");
	}

	public TicketFilterOptionsResponse getTicketFilterOptionsWithCounts(String atmId) {
		// You'll need to retrieve the actual cmUserId, perhaps from security context
		// For demonstration, let's use a placeholder.
		String cmUserId = loginService.getLoggedInUser(); // Replace with actual user ID retrieval logic

		log.info("Fetching ticket filter options for cmUserId: {} and atmId: {}", cmUserId, atmId);

		MapSqlParameterSource in = new MapSqlParameterSource().addValue("cm_user_id", cmUserId).addValue("Atmid",
				atmId);

		try {
			log.debug("Executing stored procedure with parameters: {}", in.getValues());
			Map<String, Object> out = simpleJdbcCall.execute(in);

			// Extract lists from the returned map. The keys are the names given in
			// returningResultSet.
			List<SortOption> sortOptions = (List<SortOption>) out.get("SortOptions");
			List<TicketTypeOption> ticketTypeOptions = (List<TicketTypeOption>) out.get("TicketTypeData");
			List<CategoryOption> categoryOptions = (List<CategoryOption>) out.get("CategoryCounts");
			List<SubCallTypeOption> subCallTypeOptions = (List<SubCallTypeOption>) out.get("SubCallTypeOptions");
			List<StatusOption> statusOptions = (List<StatusOption>) out.get("StatusData");
			List<TicketAgingHourOption> ticketAgingOptionsHr = (List<TicketAgingHourOption>) out.get("AgingDataHr");
			List<TicketAgingDayOption> ticketAgingOptionsDays = (List<TicketAgingDayOption>) out.get("AgingDataDays");
			List<OwnerOption> ownerOptions = (List<OwnerOption>) out.get("OwnerOptions");
			List<VendorOption> vendorOptions = (List<VendorOption>) out.get("VendorOptions");
			List<CreationDateOption> creationDateOptions = (List<CreationDateOption>) out.get("DateData");

			return new TicketFilterOptionsResponse(null,sortOptions != null ? sortOptions : Collections.emptyList(),
					ticketTypeOptions != null ? ticketTypeOptions : Collections.emptyList(),
					categoryOptions != null ? categoryOptions : Collections.emptyList(),
					subCallTypeOptions != null ? subCallTypeOptions : Collections.emptyList(),
					statusOptions != null ? statusOptions : Collections.emptyList(),
					ticketAgingOptionsHr != null ? ticketAgingOptionsHr : Collections.emptyList(),
					ticketAgingOptionsDays != null ? ticketAgingOptionsDays : Collections.emptyList(),
					ownerOptions != null ? ownerOptions : Collections.emptyList(),
					vendorOptions != null ? vendorOptions : Collections.emptyList(),
					creationDateOptions != null ? creationDateOptions : Collections.emptyList());

		} catch (Exception e) {
			log.error("Error fetching ticket filter options for cmUserId: {} and atmId: {}. Error: {}", cmUserId, atmId,
					e.getMessage(), e);
			throw e; // Re-throw the exception to be handled by the controller
		}
	}

	// --- Row Mappers for each DTO ---

	private static class SortOptionRowMapper implements RowMapper<SortOption> {
		@Override
		public SortOption mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			return new SortOption(rs.getInt("Sort_id"), rs.getString("Sort_name"));
		}
	}

	private static class TicketTypeOptionRowMapper implements RowMapper<TicketTypeOption> {
		@Override
		public TicketTypeOption mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			return new TicketTypeOption(rs.getInt("TicketType_id"), rs.getString("TicketType_name"),
					rs.getInt("TicketType_count"));
		}
	}

	private static class CategoryOptionRowMapper implements RowMapper<CategoryOption> {
		@Override
		public CategoryOption mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			return new CategoryOption(rs.getInt("Category_id"), rs.getString("Category_name"),
					rs.getInt("Category_count"));
		}
	}

	private static class SubCallTypeOptionRowMapper implements RowMapper<SubCallTypeOption> {
		@Override
		public SubCallTypeOption mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			return new SubCallTypeOption(rs.getInt("SubCallType_id"), rs.getString("SubCallType_name"),
					rs.getInt("SubCallType_count"));
		}
	}

	private static class StatusOptionRowMapper implements RowMapper<StatusOption> {
		@Override
		public StatusOption mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			return new StatusOption(rs.getInt("Status_id"), rs.getString("Status_name"), rs.getInt("Status_count"));
		}
	}

	private static class TicketAgingHrOptionRowMapper implements RowMapper<TicketAgingHourOption> {
		@Override
		public TicketAgingHourOption mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			return new TicketAgingHourOption(rs.getInt("TicketAging_hr_id"), rs.getString("TicketAging_hr_name"),
					rs.getInt("TicketAging_hr_count"));
		}
	}

	private static class TicketAgingDayOptionRowMapper implements RowMapper<TicketAgingDayOption> {
		@Override
		public TicketAgingDayOption mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			return new TicketAgingDayOption(rs.getInt("TicketAging_days_id"), rs.getString("TicketAging_days_name"),
					rs.getInt("TicketAging_days_count"));
		}
	}

	private static class OwnerOptionRowMapper implements RowMapper<OwnerOption> {
		@Override
		public OwnerOption mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			// Corrected to match the column alias in the stored procedure: owner_id
			return new OwnerOption(rs.getInt("owner_id"), rs.getString("owner_name"), rs.getInt("owner_count"));
		}
	}

	private static class VendorOptionRowMapper implements RowMapper<VendorOption> {
		@Override
		public VendorOption mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			return new VendorOption(rs.getInt("Vendor_id"), rs.getString("Vendor_name"), rs.getInt("Vendor_count"));
		}
	}

	private static class CreationDateOptionRowMapper implements RowMapper<CreationDateOption> {
		@Override
		public CreationDateOption mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			return new CreationDateOption(rs.getInt("CreationDate_id"), rs.getString("CreationDate_name"),
					rs.getInt("CreationDate_count"));
		}
	}
}
