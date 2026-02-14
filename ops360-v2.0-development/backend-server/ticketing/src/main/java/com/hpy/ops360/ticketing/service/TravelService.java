package com.hpy.ops360.ticketing.service;

import org.springframework.stereotype.Service;

import com.hpy.generic.impl.GenericService;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.IsTravellingDto;
import com.hpy.ops360.ticketing.dto.TravelDto;
import com.hpy.ops360.ticketing.dto.TravelModeMessageDto;
import com.hpy.ops360.ticketing.entity.Travel;
import com.hpy.ops360.ticketing.entity.TravelModeMessage;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.TravelModeMessageRepository;
import com.hpy.ops360.ticketing.repository.TravelRepository;
import com.hpy.ops360.ticketing.request.UpdateWorkModeReq;
import com.hpy.ops360.ticketing.response.ResponseMessage;
import com.hpy.ops360.ticketing.util.CommonDateFormat;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class TravelService extends GenericService<TravelDto, Travel> {

	private final TravelRepository travelRepository;

	private final LoginService loginService;

	private final CommonDateFormat commonDateFormat;

	private final TravelModeMessageRepository travelModeMessageRepository;

	public GenericResponseDto createTravelPlan(TravelDto travelDto) {
		log.info("travelDto {}", travelDto);
		log.info("loginService.getLoggedInUser() {}", loginService.getLoggedInUser());

		travelRepository.addTravelPlan(travelDto.getAtSite(), travelDto.getAtmId(), travelDto.getTicketNo(),
				commonDateFormat.getLocalDateTimeFormat(travelDto.getTravelEtaDateTime()), travelDto.getTravelMode(),
				travelDto.getTravellingToSite(), travelDto.getWorkMode(), loginService.getLoggedInUser());
		return new GenericResponseDto("Success", "Success");
	}

	// currently not using this instead using getTravellingModeMessage()
	public IsTravellingDto getIsTravellingStatus() {
		log.info("logged in user:{}", loginService.getLoggedInUser());

		return new IsTravellingDto(travelRepository.getIsTravellingStatus(loginService.getLoggedInUser()));
	}

	public TravelModeMessageDto getTravellingModeMessage() {
		log.info("logged in user:{}", loginService.getLoggedInUser());
		TravelModeMessage travelModeMessage = travelModeMessageRepository
				.getTravelModeMessage(loginService.getLoggedInUser());

		return TravelModeMessageDto.builder().srNo(travelModeMessage.getSrNo()).message(travelModeMessage.getMessage())
				.colorCodeHex(travelModeMessage.getColorCodeHex()).textColourHex(travelModeMessage.getTextColourHex())
				.atmId(travelModeMessage.getAtmId()).ticketNumber(travelModeMessage.getTicketNumber())
				.travelEtaDatetime(travelModeMessage.getTravelEtaDatetime() == null ? ""
						: travelModeMessage.getTravelEtaDatetime())
				.lastModifiedTime(
						travelModeMessage.getLastModifiedTime() == null ? "" : travelModeMessage.getLastModifiedTime())
				.travelDuration(travelModeMessage.getTravelDuration())
				.isDurationCrossed(travelModeMessage.getIsDurationCrossed())
				.isTravelling(travelModeMessage.getIsTravelling()).build();
	}
	
	public ResponseMessage updateWorkMode(UpdateWorkModeReq request)
	{	
		
		Integer flag= travelRepository.updateWorkMode(request.getAtmId(), request.getTicketNo(), request.getWorkMode(), loginService.getLoggedInUser());
		if (flag==null) {
			return ResponseMessage.builder().message("Error in sp update").build();
		}
		return ResponseMessage.builder().message("Work mode updated").build();
	}
}
