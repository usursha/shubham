package com.hpy.ops360.ticketing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.AtmDetailFilterResponseDto;
import com.hpy.ops360.ticketing.dto.BankFilterResponseDTO;
import com.hpy.ops360.ticketing.dto.DownTime_FilterResponseDTO;
import com.hpy.ops360.ticketing.dto.ErrorTypeFilterResponseDTO;
import com.hpy.ops360.ticketing.dto.OwnerFilterResponseDTO;
import com.hpy.ops360.ticketing.dto.SiteNameFilterResponseDTO;
import com.hpy.ops360.ticketing.dto.TaskSummaryOfCeResponseFilterDTO;
import com.hpy.ops360.ticketing.dto.VendorFilterResponseDTO;
import com.hpy.ops360.ticketing.login.service.LoginService;

@Service
public class TaskSummaryFilterOfCeService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private LoginService loginService;

	public TaskSummaryOfCeResponseFilterDTO getFilters() {
		String userLoginId = loginService.getLoggedInUser();
		TaskSummaryOfCeResponseFilterDTO filterDTO = new TaskSummaryOfCeResponseFilterDTO();

		filterDTO.setAtmFilters(getAtmFilters(userLoginId));
		filterDTO.setBankFilters(getBankFilters(userLoginId));
		filterDTO.setSiteNameFilters(getSiteNameFilters(userLoginId));
		filterDTO.setVendorFilters(getVendorFilters(userLoginId));
		filterDTO.setOwnerFilters(getOwnerFilters(userLoginId));
		filterDTO.setErrorType(getErrorTypeFilter(userLoginId));
		filterDTO.setDownTime(getDownTimeFilters(userLoginId));

		return filterDTO;
	}

	@SuppressWarnings("deprecation")
	private List<AtmDetailFilterResponseDto> getAtmFilters(String userLoginId) {
		String query = "EXEC USP_Get_ATM_Filter ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			AtmDetailFilterResponseDto dto = new AtmDetailFilterResponseDto();
			dto.setATMID(rs.getString("ATMID"));
			return dto;
		});
	}

	@SuppressWarnings("deprecation")
	private List<BankFilterResponseDTO> getBankFilters(String userLoginId) {
		String query = "EXEC USP_GetBankNameofCE ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			BankFilterResponseDTO dto = new BankFilterResponseDTO();
			dto.setBank(rs.getString("BANK"));
			return dto;
		});
	}

	@SuppressWarnings("deprecation")
	private List<SiteNameFilterResponseDTO> getSiteNameFilters(String userLoginId) {
		String query = "EXEC USP_Get_Site_Name_Filter ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			SiteNameFilterResponseDTO dto = new SiteNameFilterResponseDTO();
			dto.setSiteName(rs.getString("SITE_NAME"));
			return dto;
		});
	}

	@SuppressWarnings("deprecation")
	private List<VendorFilterResponseDTO> getVendorFilters(String userLoginId) {
		String query = "EXEC USP_Get_Vendor_Filter ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			VendorFilterResponseDTO dto = new VendorFilterResponseDTO();
			dto.setVendor(rs.getString("VENDOR"));
			return dto;
		});
	}

	@SuppressWarnings("deprecation")
	private List<OwnerFilterResponseDTO> getOwnerFilters(String userLoginId) {
		String query = "EXEC USP_Get_Owner_Filter ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			OwnerFilterResponseDTO dto = new OwnerFilterResponseDTO();
			dto.setOwner(rs.getString("OWNER"));
			return dto;
		});
	}

	@SuppressWarnings("deprecation")
	private List<DownTime_FilterResponseDTO> getDownTimeFilters(String userLoginId) {
		String query = "EXEC USP_Get_DownTime_Filter ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			DownTime_FilterResponseDTO dto = new DownTime_FilterResponseDTO();
			dto.setDownTime(rs.getString("DOWN_TIME"));
			dto.setMin(rs.getInt("MIN"));
			dto.setMax(rs.getInt("MAX"));
			return dto;
		});
	}

	@SuppressWarnings("deprecation")
	private List<ErrorTypeFilterResponseDTO> getErrorTypeFilter(String userLoginId) {
		String query = "EXEC USP_Get_ErrorType_Filter ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			ErrorTypeFilterResponseDTO dto = new ErrorTypeFilterResponseDTO();
			dto.setErrorTypeCategory(rs.getString("ERROR_TYPE"));
			return dto;
		});
	}

}
