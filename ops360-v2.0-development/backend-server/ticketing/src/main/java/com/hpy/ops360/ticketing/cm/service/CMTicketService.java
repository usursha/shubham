package com.hpy.ops360.ticketing.cm.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.Documentrequest;
import com.hpy.ops360.ticketing.cm.dto.GetTicketDocumentsResponse;
import com.hpy.ops360.ticketing.cm.dto.TicketDetailsDto;
import com.hpy.ops360.ticketing.cm.entity.DocTicketDetailsEntity;
import com.hpy.ops360.ticketing.cm.entity.DocTicketListEntity;
import com.hpy.ops360.ticketing.cm.repo.DocTicketListRepository;
import com.hpy.ops360.ticketing.cm.repo.DocTicketRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CMTicketService {
	@Autowired
	private DocTicketRepository docticketRepository;

	@Autowired
	private DocTicketListRepository docticketlistRepository;

	// Define date and time formatters
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm:ss a");

	public List<TicketDetailsDto> getTicketDetailsByAtmId(Documentrequest request) {
		log.info("*** Inside Service getTicketDetailsByAtmId ***");

		List<DocTicketDetailsEntity> response = docticketRepository.getTicketDetailsByAtmId(request.getAtm_id());

		log.info("Response Received from ticketRepository: " + response);

		List<TicketDetailsDto> data = response.stream()
				.filter(result -> result.getDocument() != null && !result.getDocument().trim().isEmpty()) // Filter out
																											// empty or
																											// null
																											// documents
				.map(result -> {

					String createdDateTime = result.getCreated_date();

					// Assuming the format of createdDateTime is "yyyy-MM-dd HH:mm:ss.SSS"
					if (createdDateTime != null && createdDateTime.length() > 19) {
						// Remove milliseconds (after the 19th character, which is seconds)
						createdDateTime = createdDateTime.substring(0, 19);
					}

					// Use a DateTimeFormatter without milliseconds
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDateTime modifiedDateTime = LocalDateTime.parse(createdDateTime, formatter);

					String formattedDate = modifiedDateTime.format(DATE_FORMATTER);
					String formattedTime = modifiedDateTime.format(TIME_FORMATTER);

					return new TicketDetailsDto(result.getSr(), result.getAtm_id(), result.getTicket_number(),
							formattedDate, formattedTime, result.getError(), result.getRemark(), result.getDocument());
				}).toList(); // Collect to a list

		return data;
	}

	public List<GetTicketDocumentsResponse> getTicketDetailsListByAtmId(String atmId, String status, String ticketType,
			String dateStr, String startDate, String endDate, String ticketNumber, String lastModifiedDateStr) {
		log.info("*** Inside Service getTicketDetailsByAtmId ***");
		log.info(
				"request parameters: atmId={}, status={}, ticketType={}, dateStr={}, startDate={}, endDate={}, ticketNumber={}, lastModifiedDateStr={}",
				atmId, status, ticketType, dateStr, startDate, endDate, ticketNumber, lastModifiedDateStr);

		// Override startDate and endDate based on dateStr
		if (dateStr != null && !dateStr.isEmpty()) {
			LocalDate today = LocalDate.now();
			switch (dateStr) {
			case "Today":
				startDate = today.toString();
				endDate = today.toString();
				break;
			case "This Week":
				LocalDate monday = today.with(DayOfWeek.MONDAY);
				startDate = monday.toString();
				endDate = today.toString();
				break;
			case "This Month":
				LocalDate firstOfMonth = today.withDayOfMonth(1);
				startDate = firstOfMonth.toString();
				endDate = today.toString();
				break;
			case "Last 90 Days":
				LocalDate ninetyDaysAgo = today.minusDays(90);
				startDate = ninetyDaysAgo.toString();
				endDate = today.toString();
				break;
			default:
				log.warn("Unknown dateStr value: {}", dateStr);
			}
		}

		// after passing date string in request
//	 		log.info("request parameters after passing string: atmId={}, status={}, ticketType={}, etaExpired={}, dateStr={}, startDate={}, endDate={}, ticketNumber={}, lastModifiedDateStr={}",
//					atmId, status, ticketType, etaExpired, dateStr, startDate, endDate, ticketNumber, lastModifiedDateStr);

		List<DocTicketListEntity> response = docticketlistRepository.getTicketDetailsWithDocuments(atmId, status,
				ticketType, startDate, endDate, ticketNumber, lastModifiedDateStr);

		log.info("Response Received from ticketRepository: " + response);
		List<GetTicketDocumentsResponse> data = response.stream().map(result -> {
			GetTicketDocumentsResponse dto = new GetTicketDocumentsResponse();
			dto.setSrNo(result.getSrNo());
			dto.setAtmId(result.getAtmId());
			dto.setTicketNumber(result.getTicketNumber());
			dto.setCreatedDate(result.getCreatedDate());
			dto.setLastModifiedBy(result.getLastModifiedBy());
			dto.setOwner(result.getOwner());
			dto.setVendor(result.getVendor());
			dto.setSubcallType(result.getSubcallType());
			dto.setInternalRemark(result.getInternalRemark());
			dto.setStatus(result.getStatus());
			dto.setTicketType(result.getTicketType());
			dto.setIsFlagged(result.getIsFlagged());
			dto.setHoursPassed(result.getHoursPassed());
			dto.setDocument(result.getDocument());
			dto.setDocumentName(result.getDocumentName());
			dto.setDocument1(result.getDocument1());
			dto.setDocument1Name(result.getDocument1Name());
			dto.setDocument2(result.getDocument2());
			dto.setDocument2Name(result.getDocument2Name());
			dto.setDocument3(result.getDocument3());
			dto.setDocument3Name(result.getDocument3Name());
			dto.setDocument4(result.getDocument4());
			dto.setDocument4Name(result.getDocument4Name());

			return dto;
		}).toList();

		return data;

	}

//	public List<MultiTicketDetailsDto> getTicketDetailsListByAtmId(Documentrequest request, String token) {
//	    log.info("*** Inside Service getTicketDetailsByAtmId ***");
//
//	    List<DocTicketDetailsListEntity> response = docticketlistRepository
//	            .getTicketDetailsListByAtmId(request.getAtm_id());
//
//	    log.info("Response Received from ticketRepository: " + response);
//
//	    List<MultiTicketDetailsDto> data = new ArrayList<>();
//
//	    for (DocTicketDetailsListEntity result : response) {
//	        try {
//	            // Prepare Feign request
//	            AssetMediaRequestDto assetRequest = new AssetMediaRequestDto();
//	            assetRequest.setAtmId(request.getAtm_id());
//	            assetRequest.setTicketId(result.getSrno());
//
//	            log.info("Request Made: {}", assetRequest);
//	            IResponseDtoImplurl urlResponse = assetServiceClient.saveImages(token, assetRequest);
//
//	            // Extract image URLs
//	            List<String> imageUrls = Optional.ofNullable(urlResponse)
//	            	    .map(IResponseDtoImplurl::getData)
//	            	    .map(AssetMediaResponseDto::getImagesUrl)
//	            	    .orElse(Collections.emptyList());
//
//	            if (imageUrls != null && !imageUrls.isEmpty()) {
//	                // Map and collect only if images are present
//	                MultiTicketDetailsDto dto = new MultiTicketDetailsDto();
//	                dto.setSrno(result.getSrno());
//	                dto.setCreatedDate(result.getCreatedDate());
//	                dto.setOwner(result.getOwner());
//	                dto.setVendor(result.getVendor());
//	                dto.setSubcallType(result.getSubcallType());
//	                dto.setUploadedBy(result.getUploadedBy());
//	                dto.setIsClosed(result.getIsClosed());
//	                dto.setIsDown(result.getIsDown());
//	                dto.setIsOpen(result.getIsOpen());
//	                dto.setHoursPassed(result.getHoursPassed());
//	                dto.setImageCount(imageUrls.size());
//	                dto.setImageUrl(imageUrls);
//	                
//
//	                data.add(dto);
//	            }
//
//	        } catch (Exception e) {
//	            log.error("Failed to fetch image URLs for ticket: " + result.getSrno(), e);
//	        }
//	    }
//
//	    log.info("Filtered data with images: {}", data);
//	    return data;
//	}

}
