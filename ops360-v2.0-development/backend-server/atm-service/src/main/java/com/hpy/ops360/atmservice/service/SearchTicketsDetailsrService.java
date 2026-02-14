package com.hpy.ops360.atmservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.dto.TicketDetailDto;
import com.hpy.ops360.atmservice.dto.TicketSearchRequestDto;
import com.hpy.ops360.atmservice.dto.TicketsFilterResponseDto;
import com.hpy.ops360.atmservice.entity.TicketDetailEntity;
import com.hpy.ops360.atmservice.entity.TicketEntity;
import com.hpy.ops360.atmservice.repository.SearchTicketsDetailsrRepository;

@Service
public class SearchTicketsDetailsrService {

	@Autowired
	private SearchTicketsDetailsrRepository detailsrRepository;

	@Autowired
	private LoginService loginService;

	public List<TicketDetailDto> getTicketSearchDetails(TicketSearchRequestDto requestDto) {
		String cmUserId = loginService.getLoggedInUser();

		// List<TicketDetailEntity> list =
		// detailsrRepository.getTicketDetailsByCmUserIdAndAtmId(cmUserId, atmId);

		List<TicketDetailEntity> tickets = detailsrRepository.getTicketDetails(cmUserId, requestDto.getSearchText(),
				requestDto.getAtmId(), requestDto.getSort(), requestDto.getCategory(), requestDto.getOwner(),
				requestDto.getSubCallType(), requestDto.getStatus(), requestDto.getVendor(),
				requestDto.getTicketAgingHr(), requestDto.getTicketAgingHrFrom(), requestDto.getTicketAgingHrTo(),
				requestDto.getTicketAgingDay(), requestDto.getTicketAgingDayStart(), requestDto.getTicketAgingDayEnd(),
				requestDto.getCreationDate(), requestDto.getCreationDateFrom(), requestDto.getCreationDateTo()

		);

		return tickets.stream().map(this::toDto).collect(Collectors.toList());

	}

	public TicketDetailDto toDto(TicketDetailEntity entity) {

		TicketDetailDto dto = new TicketDetailDto();
		dto.setSrno(entity.getSrno());
		dto.setCustomer(entity.getCustomer());
		dto.setEquipmentId(entity.getEquipmentid());
		dto.setModel(entity.getModel());
		dto.setAtmCategory(entity.getAtmcategory());
		dto.setAtmClassification(entity.getAtmclassification());
		dto.setCreatedDate(entity.getCreateddate() != null ? entity.getCreateddate().toLocalDateTime() : null);
		dto.setCallType(entity.getCalltype());
		dto.setSubCallType(entity.getSubcalltype());
		dto.setVendor(entity.getVendor());
		dto.setServiceCode(entity.getServicecode());
		dto.setDiagnosis(entity.getDiagnosis());
		dto.setEventCode(entity.getEventcode());
		dto.setHelpdeskname(entity.getHelpdeskname());
		dto.setLastActivity(entity.getLastactivity());
		dto.setStatus(entity.getStatus());
		dto.setSubStatus(entity.getSubstatus());
		dto.setSite(entity.getSite());
		dto.setCity(entity.getCity());
		dto.setState(entity.getState());
		dto.setEtaDatetime(entity.getEtadatetime() != null ? entity.getEtadatetime().toLocalDateTime() : null);
		dto.setOwner(entity.getOwner());
		dto.setCustomerRemark(entity.getCustomer_remark());
		dto.setTicketSource(entity.getTicket_source());
		dto.setAtmStatus(entity.getAtm_status());
		dto.setFormattedDowntime(entity.getFormatted_downtime());
		dto.setAgeingDays(entity.getAgeing_days());
		dto.setCeUserId(entity.getCe_user_id());
		dto.setFlagStatus(entity.getFlag_status());

		return dto;
	}

}
