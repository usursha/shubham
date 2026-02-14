package com.hpy.ops360.ticketing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.AssignedAtmFilterResponseDto;
import com.hpy.ops360.ticketing.dto.AssignedAtmforCeFilterResponseDto;
import com.hpy.ops360.ticketing.dto.AtmDetailFilterResponseDto;
import com.hpy.ops360.ticketing.dto.BankFilterResponseDTO;
import com.hpy.ops360.ticketing.dto.GetLocationNameofCEFilterResponseDto;
import com.hpy.ops360.ticketing.dto.GradeFilterResponseDTO;
import com.hpy.ops360.ticketing.dto.SiteNameFilterResponseDTO;
import com.hpy.ops360.ticketing.dto.TransactionTrendFilterResponseDTO;
import com.hpy.ops360.ticketing.dto.UptimeTrendFilterResponseDTO;
import com.hpy.ops360.ticketing.login.service.LoginService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AssignedAtmforCeFilterService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private LoginService loginService;

	public AssignedAtmforCeFilterResponseDto getAssignAtmFilters() {
		String userLoginId = loginService.getLoggedInUser();

		AssignedAtmforCeFilterResponseDto filterDTO = new AssignedAtmforCeFilterResponseDto();
		filterDTO.setGradeFilter(getGradeFilters(userLoginId));
		filterDTO.setTransactionTrendFilter(getTransactionTrendFilter(userLoginId));
		filterDTO.setUptimetrendFilter(getUptimetrendFilter(userLoginId));
		filterDTO.setBankFilters(getBankFilters(userLoginId));
		filterDTO.setLocationFilters(getSiteNameFilters(userLoginId));
		return filterDTO;
	}

	@SuppressWarnings({ "deprecation" })
	private List<GradeFilterResponseDTO> getGradeFilters(String userLoginId) {
		String query = "EXEC USP_Get_Grade_Filter ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			GradeFilterResponseDTO dto = new GradeFilterResponseDTO();
			dto.setGrade(rs.getString("grade"));
			return dto;
		});
	}

	@SuppressWarnings({ "deprecation" })
	private List<TransactionTrendFilterResponseDTO> getTransactionTrendFilter(String userLoginId) {
		String query = "EXEC USP_Get_Trend_Filter ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			TransactionTrendFilterResponseDTO dto = new TransactionTrendFilterResponseDTO();
			dto.setValue(rs.getString("value"));
			dto.setMin(rs.getInt("min"));
			dto.setMax(rs.getInt("max"));
			return dto;
		});
	}

	@SuppressWarnings("deprecation")
	private List<UptimeTrendFilterResponseDTO> getUptimetrendFilter(String userLoginId) {
		String query = "EXEC USP_Get_Trend_Filter ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			UptimeTrendFilterResponseDTO dto = new UptimeTrendFilterResponseDTO();
			dto.setValue(rs.getString("value"));
			dto.setMin(rs.getInt("min"));
			dto.setMax(rs.getInt("max"));
			return dto;
		});

	}

	@SuppressWarnings("deprecation")
	private List<BankFilterResponseDTO> getBankFilters(String userLoginId) {
		String query = "EXEC USP_Get_Bank_Filter ?";
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

	// --------------------------Second part//
	// --------------------------------------------------

	public AssignedAtmFilterResponseDto getAssignAtmFiltersforCe() {
		String userLoginId = loginService.getLoggedInUser();

		AssignedAtmFilterResponseDto filterDTO = new AssignedAtmFilterResponseDto();
		filterDTO.setAtmFilters(getAtmListFilters(userLoginId));
		filterDTO.setBankFilters(getBankFiltersForAssignAtm(userLoginId));
		filterDTO.setLocationName(getLocationNameFiltersforCe(userLoginId));
		return filterDTO;
	}

	@SuppressWarnings({ "deprecation" })
	private List<AtmDetailFilterResponseDto> getAtmListFilters(String userLoginId) {
		String query = "EXEC USP_Get_ATM_Filter ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			AtmDetailFilterResponseDto dto = new AtmDetailFilterResponseDto();
			dto.setATMID(rs.getString("ATMID"));
			return dto;
		});
	}


	@SuppressWarnings("deprecation")
	private List<BankFilterResponseDTO> getBankFiltersForAssignAtm(String userLoginId) {
		String query = "EXEC USP_GetBankNameofCE ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			BankFilterResponseDTO dto = new BankFilterResponseDTO();
			dto.setBank(rs.getString("BANK"));
			return dto;
		});
	}

	@SuppressWarnings("deprecation")
	private List<GetLocationNameofCEFilterResponseDto> getLocationNameFiltersforCe(String userLoginId) {
		String query = "EXEC USP_GetLocationNameofCE ?";
		return jdbcTemplate.query(query, new Object[] { userLoginId }, (rs, rowNum) -> {
			GetLocationNameofCEFilterResponseDto dto = new GetLocationNameofCEFilterResponseDto();
			dto.setLocationName(rs.getString("location_name"));
			return dto;
		});
	}

}
