package com.hpy.ops360.ticketing.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpy.generic.impl.GenericService;
import com.hpy.ops360.ticketing.config.DisableSslClass;
import com.hpy.ops360.ticketing.dto.EtaDto;
import com.hpy.ops360.ticketing.dto.EtaDto2;
import com.hpy.ops360.ticketing.dto.EtaRequestDtoForCm;
import com.hpy.ops360.ticketing.dto.FollowUp;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.RemarkHistoryResponseDto;
import com.hpy.ops360.ticketing.dto.RemarksDto;
import com.hpy.ops360.ticketing.dto.RemarksDtoForCm;
import com.hpy.ops360.ticketing.dto.RemarksrequestDto;
import com.hpy.ops360.ticketing.dto.UpdateEtaDto;
import com.hpy.ops360.ticketing.entity.Eta;
import com.hpy.ops360.ticketing.entity.OwnerBySubcall;
import com.hpy.ops360.ticketing.entity.ParentChildTicketNo;
import com.hpy.ops360.ticketing.entity.RemarksHistory;
import com.hpy.ops360.ticketing.entity.RemarksHistoryforCm;
import com.hpy.ops360.ticketing.feignclient.request.dto.UpdateSubcallTypeDto;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.EtaRepository;
import com.hpy.ops360.ticketing.repository.OwnerBySubcallRepository;
import com.hpy.ops360.ticketing.repository.ParentChildTicketNoRepository;
import com.hpy.ops360.ticketing.repository.RemarksHistoryForCmRepository;
import com.hpy.ops360.ticketing.repository.RemarksHistoryRepository;
import com.hpy.ops360.ticketing.response.SynergyResponse;
import com.hpy.ops360.ticketing.ticket.dto.RemarksResponseDto;
import com.hpy.ops360.ticketing.util.CommonDateFormat;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor.FormatStyle;
import com.hpy.ops360.ticketing.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EtaService extends GenericService<EtaDto, Eta> {

	@Autowired
	private SynergyService synergyService;

	@Autowired
	private LoginService loginService;

	@Autowired
	private EtaRepository etaRepository;

	@Autowired
	private CommonDateFormat commonDateFormat;

	@Autowired
	private RemarksHistoryRepository remarksHistoryRepository;

	@Autowired
	private ParentChildTicketNoRepository parentChildTicketNoRepository;

	@Autowired
	private RemarksHistoryForCmRepository remarksHistoryForCmRepository;

	@Autowired
	private OwnerBySubcallRepository ownerBySubcallRepository;

	@Value("${ops360.ticketing.eta.documents.n}")
	private int nEtaDocuments;

//	@Autowired
//	private AssetServiceClient assetServiceClient;

//	public GenericResponseDto addEta(EtaDto2 etaDto, String token) {
//		log.info("EtaService|addEta:{}", etaDto);
//
//		SynergyResponse response = null;
//
//		try {
//			// Get parent-child tickets from repository
//			List<ParentChildTicketNo> parentChildTicketList = parentChildTicketNoRepository.getParentChildTicketNo(
//					loginService.getLoggedInUser(), etaDto.getAtmid(), etaDto.getTicketNumber());
//
//			// Update ETA and comments for parent and child tickets
//			for (ParentChildTicketNo parentChildTicketNo : parentChildTicketList) {
//				log.info("Updating ticket number:{}", parentChildTicketNo.getTicketid());
//
//				response = synergyService.updateSubcallType(UpdateSubcallTypeDto.builder().atmId(etaDto.getAtmid())
//						.ticketNo(parentChildTicketNo.getTicketid()).subcallType(etaDto.getSubcallType())
//						.comments(etaDto.getInternalRemark()).updatedBy(loginService.getLoggedInUser()).build());
//				log.info("update subcalltype response: {}", response);
//
//				if (response == null || !response.getStatus().equals("Success")) {
//					return new GenericResponseDto("Failure",
//							response != null ? response.getStatus() : "No response from synergy service");
//				}
//
//				if (etaDto.getEtaDateTime() != null && !etaDto.getEtaDateTime().isEmpty()) {
//					UpdateEtaDto updateEtaDto = new UpdateEtaDto(parentChildTicketNo.getTicketid(), etaDto.getAtmid(),
//							etaDto.getEtaDateTime(), etaDto.getCreatedBy(), "ETA updated by CE");
//					log.info("updateEtaDto {}", updateEtaDto);
//					SynergyResponse synergyResponse = synergyService.updateEta(updateEtaDto);
//					log.info("SynergyResponse: {}", synergyResponse);
//				}
//
//				log.info("Starting ETA document upload for ticket: {}", etaDto.getTicketNumber());
//
//				List<MediaFilesDto> mediaFiles = Stream
//						.of(new MediaFilesDto(etaDto.getEtaDocumentDto().getDocumentName(),
//								etaDto.getEtaDocumentDto().getDocument()),
//								new MediaFilesDto(etaDto.getEtaDocumentDto().getDocument1Name(),
//										etaDto.getEtaDocumentDto().getDocument1()),
//								new MediaFilesDto(etaDto.getEtaDocumentDto().getDocument2Name(),
//										etaDto.getEtaDocumentDto().getDocument2()),
//								new MediaFilesDto(etaDto.getEtaDocumentDto().getDocument3Name(),
//										etaDto.getEtaDocumentDto().getDocument3()),
//								new MediaFilesDto(etaDto.getEtaDocumentDto().getDocument4Name(),
//										etaDto.getEtaDocumentDto().getDocument4()))
//						.filter(dto -> dto.getBase64() != null && !dto.getBase64().isBlank())
//						.collect(Collectors.toList());
//
//				DtoAssetRequestWithMultipleFiles assetRequest = new DtoAssetRequestWithMultipleFiles();
//				assetRequest.setAtmId(etaDto.getAtmid());
//				assetRequest.setParentId(etaDto.getTicketNumber());
//				assetRequest.setParentType("ticket");
//				assetRequest.setMediaType("image");
//				assetRequest.setMediafiles(mediaFiles);
//
//				log.info("Request Made:- " + assetRequest);
//				MediaListDto mediaListDto = new MediaListDto();
//				mediaListDto.setMediaList(List.of(assetRequest));
//
//				IResponseDtoImpl data = assetServiceClient.convertFileAndSave(token, mediaListDto);
//				log.info("data fetched:- " + data);
//				log.info("Successfully uploaded ETA documents for ticket: {}", etaDto.getTicketNumber());
//
//				
//				List<String> imageUrls = Collections.emptyList();
//
//				try {
//				    if (data != null && data.getData() != null && data.getData().getImageurls() != null) {
//				        imageUrls = data.getData().getImageurls();
//				    } else {
//				        log.warn("Image URLs are missing or null in the response.");
//				    }
//				} catch (Exception ex) {
//				    log.error("Exception while retrieving image URLs", ex);
//				}
//
//				String doc1 = (imageUrls.size() > 0) ? imageUrls.get(0) : "";
//				String doc2 = (imageUrls.size() > 1) ? imageUrls.get(1) : "";
//				String doc3 = (imageUrls.size() > 2) ? imageUrls.get(2) : "";
//				String doc4 = (imageUrls.size() > 3) ? imageUrls.get(3) : "";
//				String doc5 = (imageUrls.size() > 4) ? imageUrls.get(4) : "";
//
//
//
//				etaRepository.addEta(etaDto.getAtmid(), etaDto.getCustomerRemark(), doc1,
//						etaDto.getEtaDocumentDto().getDocumentName(),
//						commonDateFormat.getLocalDateTimeFormat(etaDto.getEtaDateTime()), etaDto.getInternalRemark(),
//						etaDto.getOwner(), etaDto.getSubcallType(), parentChildTicketNo.getTicketid(),
//						loginService.getLoggedInUser(), doc2, etaDto.getEtaDocumentDto().getDocument1Name(), doc3,
//						etaDto.getEtaDocumentDto().getDocument2Name(), doc4,
//						etaDto.getEtaDocumentDto().getDocument3Name(), doc5,
//						etaDto.getEtaDocumentDto().getDocument4Name());
//
//			}
//
//			log.info("Eta Updated Successfully");
//			return new GenericResponseDto("Success", "Eta Updated Successfully");
//		} catch (Exception e) {
//			log.info("Error occurred while updating ETA: ", e);
//			return new GenericResponseDto("Failure", "An error occurred while updating ETA");
//		}
//	}
	
	
	public GenericResponseDto addEta(EtaDto2 etaDto, String token) {
		log.info("EtaService|addEta:{}", etaDto);

		SynergyResponse response = null;

		try {
			// Get parent-child tickets from repository
			List<ParentChildTicketNo> parentChildTicketList = parentChildTicketNoRepository.getParentChildTicketNo(
					loginService.getLoggedInUser(), etaDto.getAtmid(), etaDto.getTicketNumber());

			// Update ETA and comments for parent and child tickets
			for (ParentChildTicketNo parentChildTicketNo : parentChildTicketList) {
				log.info("Updating ticket number:{}", parentChildTicketNo.getTicketid());

				response = synergyService.updateSubcallType(UpdateSubcallTypeDto.builder().atmId(etaDto.getAtmid())
						.ticketNo(parentChildTicketNo.getTicketid()).subcallType(etaDto.getSubcallType())
						.comments(etaDto.getInternalRemark()).updatedBy(loginService.getLoggedInUser()).build());
				log.info("update subcalltype response: {}", response);

				if (response == null || !response.getStatus().equalsIgnoreCase("Success")) {
					return new GenericResponseDto("Failure",
							response != null ? response.getStatus() : "No response from synergy service");
				}

				if (etaDto.getEtaDateTime() != null && !etaDto.getEtaDateTime().trim().isEmpty()) {
					UpdateEtaDto updateEtaDto = new UpdateEtaDto(parentChildTicketNo.getTicketid(), etaDto.getAtmid(),
							CustomDateFormattor.convert(etaDto.getEtaDateTime(), FormatStyle.SYNERGY), etaDto.getCreatedBy(), "ETA updated by CE");
					log.info("updateEtaDto {}", updateEtaDto);
					SynergyResponse synergyResponse = synergyService.updateEta(updateEtaDto);
					log.info("SynergyResponse: {}", synergyResponse);
					if (!synergyResponse.getStatus().equalsIgnoreCase("Success")) {
						return new GenericResponseDto("Failure",
								response != null ? response.getStatus() : "No response from synergy service");
					}
				}

				etaRepository.addEta(etaDto.getAtmid(), etaDto.getCustomerRemark(), etaDto.getEtaDocumentDto().getDocument(),
						etaDto.getEtaDocumentDto().getDocumentName(),
						commonDateFormat.getLocalDateTimeFormat(etaDto.getEtaDateTime()), etaDto.getInternalRemark(),
						etaDto.getOwner(), etaDto.getSubcallType(), parentChildTicketNo.getTicketid(),
						loginService.getLoggedInUser(), etaDto.getEtaDocumentDto().getDocument2(), etaDto.getEtaDocumentDto().getDocument1Name(), etaDto.getEtaDocumentDto().getDocument2(),
						etaDto.getEtaDocumentDto().getDocument2Name(), etaDto.getEtaDocumentDto().getDocument3(),
						etaDto.getEtaDocumentDto().getDocument3Name(), etaDto.getEtaDocumentDto().getDocument4(),
						etaDto.getEtaDocumentDto().getDocument4Name());
			}

			log.info("Eta Updated Successfully");
			return new GenericResponseDto("Success", "Eta Updated Successfully");
		} catch (Exception e) {
			log.info("Error occurred while updating ETA: ", e);
			return new GenericResponseDto("Failure", "An error occurred while updating ETA");
		}
	}

//	public GenericResponseDto addEtaFromCm(EtaDto2 etaDto, String token) {
//		log.info("EtaService|addEta:{}", etaDto);
//
//		SynergyResponse response = null;
//
//		try {
//			// Get parent-child tickets from repository
//			List<ParentChildTicketNo> parentChildTicketList = parentChildTicketNoRepository.getParentChildTicketNo(
//					loginService.getLoggedInUser(), etaDto.getAtmid(), etaDto.getTicketNumber());
//
//			// Update ETA and comments for parent and child tickets
//			for (ParentChildTicketNo parentChildTicketNo : parentChildTicketList) {
//				log.info("Updating ticket number:{}", parentChildTicketNo.getTicketid());
//
//				response = synergyService.updateSubcallType(UpdateSubcallTypeDto.builder().atmId(etaDto.getAtmid())
//						.ticketNo(parentChildTicketNo.getTicketid()).subcallType(etaDto.getSubcallType())
//						.comments(etaDto.getInternalRemark()).updatedBy(loginService.getLoggedInUser()).build());
//				log.info("update subcalltype response: {}", response);
//
//				if (response == null || !response.getStatus().equals("Success")) {
//					return new GenericResponseDto("Failure",
//							response != null ? response.getStatus() : "No response from synergy service");
//				}
//
//				log.info("update subcalltype response: {}", response);
//
//				if (etaDto.getEtaDateTime() != null && !etaDto.getEtaDateTime().isEmpty()) {
//					try {
//						// Parse the date before using it
//						LocalDateTime parsedDateTime = commonDateFormat
//								.getLocalDateTimeFormatCm(etaDto.getEtaDateTime());
//
//						UpdateEtaDto updateEtaDto = new UpdateEtaDto(parentChildTicketNo.getTicketid(),
//								etaDto.getAtmid(), etaDto.getEtaDateTime(), etaDto.getCreatedBy(), "ETA updated by CM");
//
//						log.info("updateEtaDto {}", updateEtaDto);
//						SynergyResponse synergyResponse = synergyService.updateEta(updateEtaDto);
//						log.info("SynergyResponse: {}", synergyResponse);
//
//						log.info("Starting ETA document upload for ticket: {}", etaDto.getTicketNumber());
//
//						List<MediaFilesDto> mediaFiles = Stream
//								.of(new MediaFilesDto(etaDto.getEtaDocumentDto().getDocumentName(),
//										etaDto.getEtaDocumentDto().getDocument()),
//										new MediaFilesDto(etaDto.getEtaDocumentDto().getDocument1Name(),
//												etaDto.getEtaDocumentDto().getDocument1()),
//										new MediaFilesDto(etaDto.getEtaDocumentDto().getDocument2Name(),
//												etaDto.getEtaDocumentDto().getDocument2()),
//										new MediaFilesDto(etaDto.getEtaDocumentDto().getDocument3Name(),
//												etaDto.getEtaDocumentDto().getDocument3()),
//										new MediaFilesDto(etaDto.getEtaDocumentDto().getDocument4Name(),
//												etaDto.getEtaDocumentDto().getDocument4()))
//								.filter(dto -> dto.getBase64() != null && !dto.getBase64().isBlank())
//								.collect(Collectors.toList());
//
//						DtoAssetRequestWithMultipleFiles assetRequest = new DtoAssetRequestWithMultipleFiles();
//						assetRequest.setAtmId(etaDto.getAtmid());
//						assetRequest.setParentId(etaDto.getTicketNumber());
//						assetRequest.setParentType("ticket");
//						assetRequest.setMediaType("image");
//						assetRequest.setMediafiles(mediaFiles);
//
//						log.info("Request Made:- " + assetRequest);
//						MediaListDto mediaListDto = new MediaListDto();
//						mediaListDto.setMediaList(List.of(assetRequest));
//
//						IResponseDtoImpl data = assetServiceClient.convertFileAndSave(token, mediaListDto);
//						log.info("data fetched:- " + data);
//						log.info("Successfully uploaded ETA documents for ticket: {}", etaDto.getTicketNumber());
//
//						List<String> imageUrls = data.getData().getImageurls();
//						String doc1 = (imageUrls.size() > 0) ? imageUrls.get(0) : "";
//						String doc2 = (imageUrls.size() > 1) ? imageUrls.get(1) : "";
//						String doc3 = (imageUrls.size() > 2) ? imageUrls.get(2) : "";
//						String doc4 = (imageUrls.size() > 3) ? imageUrls.get(3) : "";
//						String doc5 = (imageUrls.size() > 4) ? imageUrls.get(4) : "";
//
//						etaRepository.addEta(etaDto.getAtmid(), etaDto.getCustomerRemark(), doc1,
//								etaDto.getEtaDocumentDto().getDocumentName(), parsedDateTime,
//								etaDto.getInternalRemark(), etaDto.getOwner(), etaDto.getSubcallType(),
//								parentChildTicketNo.getTicketid(), loginService.getLoggedInUser(), doc2,
//								etaDto.getEtaDocumentDto().getDocument1Name(), doc3,
//								etaDto.getEtaDocumentDto().getDocument2Name(), doc4,
//								etaDto.getEtaDocumentDto().getDocument3Name(), doc5,
//								etaDto.getEtaDocumentDto().getDocument4Name());
//
//					} catch (DateTimeParseException e) {
//						log.error("Invalid date format: {}", e.getMessage());
//						return new GenericResponseDto("Failure",
//								"Invalid date format. Please use format: dd/MM/yy HH:mm");
//					}
//				}
//			}
//
//			log.info("Eta Updated Successfully");
//			return new GenericResponseDto("Success", "Eta Updated Successfully");
//
//		} catch (Exception e) {
//			log.error("Error occurred while updating ETA: ", e);
//			return new GenericResponseDto("Failure", "An error occurred while updating ETA");
//		}
//	}
	
	
	public GenericResponseDto addEtaFromCm(EtaDto2 etaDto, String token) {
		log.info("EtaService|addEta:{}", etaDto);

		SynergyResponse response = null;

		try {
			// Get parent-child tickets from repository
			List<ParentChildTicketNo> parentChildTicketList = parentChildTicketNoRepository.getParentChildTicketNo(
					loginService.getLoggedInUser(), etaDto.getAtmid(), etaDto.getTicketNumber());

			// Update ETA and comments for parent and child tickets
			for (ParentChildTicketNo parentChildTicketNo : parentChildTicketList) {
				log.info("Updating ticket number:{}", parentChildTicketNo.getTicketid());

				response = synergyService.updateSubcallType(UpdateSubcallTypeDto.builder().atmId(etaDto.getAtmid())
						.ticketNo(parentChildTicketNo.getTicketid()).subcallType(etaDto.getSubcallType())
						.comments(etaDto.getInternalRemark()).updatedBy(loginService.getLoggedInUser()).build());
				log.info("update subcalltype response: {}", response);

				if (response == null || !response.getStatus().equals("Success")) {
					return new GenericResponseDto("Failure",
							response != null ? response.getStatus() : "No response from synergy service");
				}

				log.info("update subcalltype portal response: {}", response);

				if (etaDto.getEtaDateTime() != null && !etaDto.getEtaDateTime().trim().isEmpty()) {
					try {
						// Parse the date before using it

//						LocalDateTime parsedDateTime = commonDateFormat
//								.getLocalDateTimeFormatCm(etaDto.getEtaDateTime());
//						LocalDateTime parsedDateTime = commonDateFormat
//								.getLocalDateTimeFormatCm(etaDto.getEtaDateTime());

						LocalDateTime parsedDateTime = CommonDateFormat
								.parseToLocalDateTimeCm(etaDto.getEtaDateTime());


						UpdateEtaDto updateEtaDto = new UpdateEtaDto(parentChildTicketNo.getTicketid(),
								etaDto.getAtmid(), CustomDateFormattor.convert(etaDto.getEtaDateTime(), FormatStyle.SYNERGY), etaDto.getCreatedBy(), "ETA updated by CM");

						log.info("updateEtaDto {}", updateEtaDto);
						SynergyResponse synergyResponse = synergyService.updateEta(updateEtaDto);
						log.info("SynergyResponse Portal: {}", synergyResponse);
						
						if (response == null || !response.getStatus().equalsIgnoreCase("Success")) {
							return new GenericResponseDto("Failure",
									response != null ? response.getStatus() : "No response from synergy service");
						}

						etaRepository.addEta(etaDto.getAtmid(), etaDto.getCustomerRemark(), etaDto.getEtaDocumentDto().getDocument(),
								etaDto.getEtaDocumentDto().getDocumentName(), parsedDateTime,
								etaDto.getInternalRemark(), etaDto.getOwner(), etaDto.getSubcallType(),
								parentChildTicketNo.getTicketid(), loginService.getLoggedInUser(), etaDto.getEtaDocumentDto().getDocument1(),
								etaDto.getEtaDocumentDto().getDocument1Name(), etaDto.getEtaDocumentDto().getDocument2(),
								etaDto.getEtaDocumentDto().getDocument2Name(), etaDto.getEtaDocumentDto().getDocument3(),
								etaDto.getEtaDocumentDto().getDocument3Name(), etaDto.getEtaDocumentDto().getDocument4(),
								etaDto.getEtaDocumentDto().getDocument4Name());

					} catch (DateTimeParseException e) {
						log.error("Invalid date format: {}", e.getMessage());
						return new GenericResponseDto("Failure",
								"Invalid date format. Please use format: dd/MM/yy HH:mm");
					}
				}
			}

			log.info("Eta Updated Successfully");
			return new GenericResponseDto("Success", "Eta Updated Successfully");

		} catch (Exception e) {
			log.error("Error occurred while updating ETA: ", e);
			return new GenericResponseDto("Failure", "An error occurred while updating ETA");
		}
	}

	public GenericResponseDto addEtaByCm(EtaRequestDtoForCm etaDto, String token) {
		try {
			log.info("Starting addEtaByCm process for ATMID: {}, TicketNumber: {}", etaDto.getAtmid(),
					etaDto.getTicketNumber());

			// Initialize response outside the loop
			SynergyResponse response = null;
			LocalDateTime etaDateTime = null;

			// Initialize document variables
			String document = null, documentName = null;
			String document1 = null, document1Name = null;
			String document2 = null, document2Name = null;
			String document3 = null, document3Name = null;
			String document4 = null, document4Name = null;

			// Get logged in user
			String loggedInUser = loginService.getLoggedInUser();
			log.info("Logged in user: {}", loggedInUser);

			// Fetch parent-child ticket list
			List<ParentChildTicketNo> parentChildTicketList = parentChildTicketNoRepository
					.getParentChildTicketNo(loggedInUser, etaDto.getAtmid(), etaDto.getTicketNumber());

			if (parentChildTicketList == null || parentChildTicketList.isEmpty()) {
				log.error("No parent-child tickets found for ATMID: {}, TicketNumber: {}", etaDto.getAtmid(),
						etaDto.getTicketNumber());
				return new GenericResponseDto("Failure", "No tickets found to update");
			}

			log.info("Found {} parent-child tickets to process", parentChildTicketList.size());

			for (ParentChildTicketNo parentChildTicketNo : parentChildTicketList) {
				log.info("Processing ticket: {}", parentChildTicketNo.getTicketid());

				// Add ETA
				int result = etaRepository.addEta(etaDto.getAtmid(), etaDto.getCustomerRemark(), document, documentName,
						etaDateTime, etaDto.getInternalRemark(), etaDto.getOwner(), etaDto.getSubcallType(),
						parentChildTicketNo.getTicketid(), loggedInUser, document1, document1Name, document2,
						document2Name, document3, document3Name, document4, document4Name);

				log.info("ETA update result: {}", result);

				if (result != 0) {
					log.error("Failed to add ETA for ticket: {}", parentChildTicketNo.getTicketid());
					return new GenericResponseDto("Failure",
							"Failed to add ETA for ticket: " + parentChildTicketNo.getTicketid());
				}

				// Update subcall type
				UpdateSubcallTypeDto updateDto = UpdateSubcallTypeDto.builder().atmId(etaDto.getAtmid())
						.ticketNo(parentChildTicketNo.getTicketid()).subcallType(etaDto.getSubcallType())
						.comments(etaDto.getInternalRemark()).updatedBy(loggedInUser).build();

				log.info("Calling synergy service to update subcall type with dto: {}", updateDto);

				response = synergyService.updateSubcallType(updateDto);

				if (response == null) {
					log.error("Null response received from synergy service for ticket: {}",
							parentChildTicketNo.getTicketid());
					return new GenericResponseDto("Failure",
							"Failed to update subcall type: Null response from service");
				}
			}

			if (!"Success".equalsIgnoreCase(response.getStatus())) {
				log.error("Failed to update subcall type. Status: {}", response.getStatus());
				return new GenericResponseDto("Failure",
						"Failed to update subcall type. Status: " + response.getStatus());
			}

			log.info("Successfully updated ETA and subcall type for all tickets");
			return new GenericResponseDto("Success", "ETA Updated Successfully");

		} catch (Exception e) {
			log.error("Error in addEtaByCm: ", e);
			return new GenericResponseDto("Failure", "An error occurred: " + e.getMessage());
		}
	}

	
	/*
	 * 
	 * get remark history work start
	 * 
	*/
	public List<RemarksDto> getRemarksHistory(RemarksrequestDto requestsdto) {
		// NEED TO ADD OWNER FROM SYNERGY AFTER THEIR DEVELOPEMENT
		log.info("EtaService | getRemarksHistory:{}", requestsdto);
		RemarksResponseDto remarks = synergyService.getRemarksHistory(requestsdto);
		List<RemarksDto> remarkList = new ArrayList<>();
		if (remarks.getFollowUp() != null && !remarks.getFollowUp().isEmpty()) {
			for (FollowUp followUp : remarks.getFollowUp()) {
				if (followUp == null) {
					continue; // Skip the current iteration if followUp is null
				}
				remarkList.add(new RemarksDto(followUp.getUsername(), followUp.getComments(), followUp.getDate(), "",
						followUp.getSubcalltype()));
			}
			return remarkList;
		}
		log.info("EtaService | getRemarksHistory from synergy:{}", remarkList);
		return Collections.emptyList();

	}

	public List<RemarksDto> getRemarksHistoryFromSp(RemarksrequestDto requestsdto) {
		log.info("EtaService | getRemarksHistoryFromSp:{}", requestsdto);
		List<RemarksHistory> remarksHistories = remarksHistoryRepository
				.getRemarksHistory(loginService.getLoggedInUser(), requestsdto.getTicketno(), requestsdto.getAtmid());

		if (!remarksHistories.isEmpty()) {
			return remarksHistories.stream().map(remarks -> new RemarksDto(remarks.getCreatedBy(),
					remarks.getInternalRemark(), remarks.getCreatedDate(), remarks.getOwner(), remarks.getSubcall()))
					.toList();

		}
		return Collections.emptyList();
	}

	public List<RemarksDto> getMergedRemarksHistory(RemarksrequestDto requestsdto) {
		DisableSslClass.disableSSLVerification();
		log.info("getMergedRemarksHistory|requestsdto:{}", requestsdto);
		List<RemarksDto> remarksFromSynergy = getRemarksHistory(requestsdto);

		if (remarksFromSynergy.isEmpty()) {
			return Collections.emptyList();
		}

		// Format dates using DateUtil
		remarksFromSynergy.forEach(remark -> remark.setDate(DateUtil.formatDateStringCe(remark.getDate())));
		log.info("remarks from synergy date filtered:{}", remarksFromSynergy);

		setOwnerBYSubcallType(remarksFromSynergy);

		remarksFromSynergy.sort((r1, r2) -> LocalDateTime.parse(r2.getDate(), DateUtil.outputFormatter)
				.compareTo(LocalDateTime.parse(r1.getDate(), DateUtil.outputFormatter)));

		return remarksFromSynergy;
	}

	/**
	 * @param remarksFromSynergy
	 */
	private void setOwnerBYSubcallType(List<RemarksDto> remarksFromSynergy) {
		for (RemarksDto synergyRemark : remarksFromSynergy) {
			LocalDateTime synergyDate = LocalDateTime.parse(synergyRemark.getDate(), DateUtil.outputFormatter);
			log.info("Comment Synergy:{}", synergyRemark.getComment());
			log.info("synergyDate:{}", synergyDate);
			log.info("synergy subcalltype:{}", synergyRemark.getSubcallType());
			Optional<OwnerBySubcall> ownerBySubcall = ownerBySubcallRepository
					.getOwnerBySubcallType(synergyRemark.getSubcallType());
			if (ownerBySubcall.isPresent()) {
				log.info("owner from sp:{}", ownerBySubcall.get().getOwner());
				synergyRemark.setOwner(ownerBySubcall.get().getOwner());
			}
		}
	}

	/*
	 * Old
	 * API++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * +++
	 */

	public List<RemarksDtoForCm> getRemarksHistoryForCm(RemarksrequestDto requestsdto) {
		log.info("EtaService | getRemarksHistory:{}", requestsdto);
		RemarksResponseDto remarks = synergyService.getRemarksHistory(requestsdto);
		List<RemarksDtoForCm> remarkList = new ArrayList<>();

		if (remarks.getFollowUp() != null && !remarks.getFollowUp().isEmpty()) {
			for (FollowUp followUp : remarks.getFollowUp()) {
				if (followUp == null) {
					continue;
				}
				remarkList.add(new RemarksDtoForCm(null, // id
						followUp.getUsername(), // username
						followUp.getComments(), // comment
						followUp.getDate(), // date
						"", // owner (empty as per original code)
						followUp.getSubcalltype(), // subcallType
						"" // etaTime - assuming this exists in FollowUp, else pass null or ""
				));
			}
			return remarkList;
		}
		log.info("EtaService | getRemarksHistory from synergy:{}", remarkList);
		return Collections.emptyList();
	}

	public List<RemarksDtoForCm> getRemarksHistoryFromSp_for_Cm(RemarksrequestDto requestsdto) {
		log.info("EtaService | getRemarksHistoryFromSp:{}", requestsdto);
		List<RemarksHistoryforCm> remarksHistories = remarksHistoryForCmRepository.getRemarksHistoryforCm(
				loginService.getLoggedInUser(), requestsdto.getTicketno(), requestsdto.getAtmid());

		if (!remarksHistories.isEmpty()) {
			return remarksHistories.stream().map(remarks -> new RemarksDtoForCm(remarks.getSrNo(), // id
					remarks.getCreatedBy(), // username
					remarks.getInternalRemark(), // comment
					remarks.getCreatedDate(), // date
					remarks.getOwner(), // owner
					remarks.getSubcall(), // subcallType
					remarks.getEtaTime() // etaTime
			)).toList();
		}
		return Collections.emptyList();
	}

	public List<RemarkHistoryResponseDto> getMergedRemarksHistoryForCm(RemarksrequestDto requestsdto)
			throws ParseException {
		DisableSslClass.disableSSLVerification();
		log.info("getMergedRemarksHistory|requestsdto:{}", requestsdto);
		List<RemarksDtoForCm> remarksFromSynergy = getRemarksHistoryForCm(requestsdto);
		List<RemarksDtoForCm> remarksFromSp = getRemarksHistoryFromSp_for_Cm(requestsdto);

		if (remarksFromSp.isEmpty() && remarksFromSynergy.isEmpty()) {
			return Collections.emptyList();
		}

		// Format dates using DateUtil
		remarksFromSynergy.forEach(remark -> remark.setDate(DateUtil.formatDateStringCm(remark.getDate())));
		log.info("remarks from synergy date filtered:{}", remarksFromSynergy);
		remarksFromSp.forEach(remark -> remark.setDate(DateUtil.formatDateStringCm(remark.getDate())));
		if (remarksFromSp.isEmpty()) {
			setOwnerBYSubcallTypeCm(remarksFromSynergy, remarksFromSp);
			remarksFromSynergy.sort((r1, r2) -> LocalDateTime.parse(r2.getDate(), DateUtil.outputFormatterCm)
					.compareTo(LocalDateTime.parse(r1.getDate(), DateUtil.outputFormatterCm)));
			return transformRemarks(remarksFromSynergy);
		}

		setOwnerBYSubcallTypeCm(remarksFromSynergy, remarksFromSp);

		remarksFromSynergy.sort((r1, r2) -> LocalDateTime.parse(r2.getDate(), DateUtil.outputFormatterCm)
				.compareTo(LocalDateTime.parse(r1.getDate(), DateUtil.outputFormatterCm)));

		List<RemarkHistoryResponseDto> groupedRemarks = transformRemarks(remarksFromSynergy);

		return groupedRemarks;
	}
	
	public List<RemarkHistoryResponseDto> getMergedRemarksHistoryForCmHims(RemarksrequestDto requestsdto)
			throws ParseException {
		DisableSslClass.disableSSLVerification();
		log.info("getMergedRemarksHistory|requestsdto:{}", requestsdto);
		List<RemarksDtoForCm> remarksFromSynergy = getRemarksHistoryForCm(requestsdto);
		List<RemarksDtoForCm> remarksFromSp = getRemarksHistoryFromSp_for_Cm(requestsdto);

		if (remarksFromSp.isEmpty() && remarksFromSynergy.isEmpty()) {
			return Collections.emptyList();
		}

		// Format dates using DateUtil
		remarksFromSynergy.forEach(remark -> remark.setDate(DateUtil.formatDateStringCm(remark.getDate())));
		log.info("remarks from synergy date filtered:{}", remarksFromSynergy);
		remarksFromSp.forEach(remark -> remark.setDate(DateUtil.formatDateStringCm(remark.getDate())));
		if (remarksFromSp.isEmpty()) {
			setOwnerBYSubcallTypeCm(remarksFromSynergy, remarksFromSp);
			remarksFromSynergy.sort((r1, r2) -> LocalDateTime.parse(r2.getDate(), DateUtil.outputFormatterCm)
					.compareTo(LocalDateTime.parse(r1.getDate(), DateUtil.outputFormatterCm)));
			return transformRemarks(remarksFromSynergy);
		}

		setOwnerBYSubcallTypeCm(remarksFromSynergy, remarksFromSp);

		remarksFromSynergy.sort((r1, r2) -> LocalDateTime.parse(r2.getDate(), DateUtil.outputFormatterCm)
				.compareTo(LocalDateTime.parse(r1.getDate(), DateUtil.outputFormatterCm)));

		List<RemarkHistoryResponseDto> groupedRemarks = transformRemarks(remarksFromSynergy);

		return groupedRemarks;
	}
	private void setOwnerBYSubcallTypeCm(List<RemarksDtoForCm> remarksFromSynergy,
			List<RemarksDtoForCm> remarksFromSp) {
		for (RemarksDtoForCm synergyRemark : remarksFromSynergy) {
			LocalDateTime synergyDate = LocalDateTime.parse(synergyRemark.getDate(), DateUtil.outputFormatterCm);
			log.info("Comment Synergy:{}", synergyRemark.getComment());
			log.info("synergyDate:{}", synergyDate);
			log.info("synergy subcalltype:{}", synergyRemark.getSubcallType());
			Optional<OwnerBySubcall> ownerBySubcall = ownerBySubcallRepository
					.getOwnerBySubcallType(synergyRemark.getSubcallType());
			if (ownerBySubcall.isPresent()) {
				log.info("owner from sp:{}", ownerBySubcall.get().getOwner());
				synergyRemark.setOwner(ownerBySubcall.get().getOwner());
			}
			for (RemarksDtoForCm spRemark : remarksFromSp) {
				LocalDateTime spDate = LocalDateTime.parse(spRemark.getDate(), DateUtil.outputFormatterCm);
				log.info("CommentSp:{}", spRemark.getComment());
				log.info("spDate:{}", spDate);

				if (!synergyDate.isAfter(spDate.plusMinutes(3)) && !synergyDate.isBefore(spDate.minusMinutes(3))
						&& synergyRemark.getComment().trim().equals(spRemark.getComment().trim())) {
					synergyRemark.setEtaTime(spRemark.getEtaTime()); // Added this line to copy eta_time
					break;
				}
			}
		}

	}

	private static List<RemarkHistoryResponseDto> transformRemarks(List<RemarksDtoForCm> remarks)
			throws ParseException {
		// Define date formats
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MMM ''yy, hh:mm a", Locale.ENGLISH);
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM ''yy", Locale.ENGLISH);

		// Get today's date for comparison
		Date today = new Date();
		String todayString = outputDateFormat.format(today);

		// Group remarks by date
		Map<String, List<RemarksDtoForCm>> grouped = remarks.stream().collect(Collectors.groupingBy(remark -> {
			try {
				Date remarkDate = inputDateFormat.parse(remark.getDate());
				String remarkDateString = outputDateFormat.format(remarkDate);
				return todayString.equals(remarkDateString) ? "Today | " + todayString : remarkDateString;
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}, LinkedHashMap::new, Collectors.toList()));

		// Transform grouped remarks into a list of RemarkHistoryResponseDto
		return grouped.entrySet().stream().map(entry -> new RemarkHistoryResponseDto(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}

	/*
	 * // -------------------------for cm getRemark
	 * history-------------------------------
	 */
	public List<RemarksDtoForCm> getRemarksHistoryForCm_newResponse(RemarksrequestDto requestsdto) {
		log.info("EtaService | getRemarksHistory:{}", requestsdto);
		RemarksResponseDto remarks = synergyService.getRemarksHistory(requestsdto);
		List<RemarksDtoForCm> remarkList = new ArrayList<>();

		if (remarks.getFollowUp() != null && !remarks.getFollowUp().isEmpty()) {
			for (FollowUp followUp : remarks.getFollowUp()) {
				if (followUp == null) {
					continue;
				}

				// Format the etaTime to match SP format
				String etaTime = null;
				try {
					// First parse the date to LocalDateTime
					LocalDateTime dateTime = LocalDateTime.parse(followUp.getDate(),
							DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

					// Then format it to required format
					etaTime = dateTime.format(DateTimeFormatter.ofPattern("dd MMM, yyyy, hh:mm a"));
				} catch (Exception e) {
					log.error("Error formatting synergy etaTime: {}", e.getMessage());
					etaTime = followUp.getDate(); // Fallback to original date if parsing fails
				}

				remarkList.add(new RemarksDtoForCm(null, // id
						followUp.getUsername(), // username
						followUp.getComments(), // comment
						followUp.getDate(), // date
						"", // owner (empty as per original code)
						followUp.getSubcalltype(), // subcallType
						etaTime // formatted etaTime
				));
			}
			return remarkList;
		}
		log.info("EtaService | getRemarksHistory from synergy:{}", remarkList);
		return Collections.emptyList();
	}

	public List<RemarksDtoForCm> getRemarksHistoryFromSp_for_Cm_newResponse(RemarksrequestDto requestsdto) {
		log.info("EtaService | getRemarksHistoryFromSp:{}", requestsdto);
		List<RemarksHistoryforCm> remarksHistories = remarksHistoryForCmRepository.getRemarksHistoryforCm(
				loginService.getLoggedInUser(), requestsdto.getTicketno(), requestsdto.getAtmid());

		if (!remarksHistories.isEmpty()) {
			return remarksHistories.stream().map(remarks -> {
				// Use the date format directly from SP without modification
				return new RemarksDtoForCm(remarks.getSrNo(), remarks.getCreatedBy(), remarks.getInternalRemark(),
						remarks.getCreatedDate(), // Keep original SP date format
						remarks.getOwner(), remarks.getSubcall(), remarks.getEtaTime() // Keep original SP eta_time
																						// format
				);
			}).toList();
		}
		return Collections.emptyList();
	}

	public List<RemarkHistoryResponseDto> getMergedRemarksHistoryForCm_newResponse(RemarksrequestDto requestsdto) {
		DisableSslClass.disableSSLVerification();
		log.info("getMergedRemarksHistory|requestsdto:{}", requestsdto);

		List<RemarksDtoForCm> remarksFromSynergy = getRemarksHistoryForCm_newResponse(requestsdto);
		List<RemarksDtoForCm> remarksFromSp = getRemarksHistoryFromSp_for_Cm_newResponse(requestsdto);

		if (remarksFromSp.isEmpty() && remarksFromSynergy.isEmpty()) {
			return Collections.emptyList();
		}

		// Format dates for synergy remarks only
		remarksFromSynergy.forEach(remark -> remark.setDate(DateUtil.formatDateStringforRemark(remark.getDate())));

		// Keep SP dates in their original format
		remarksFromSp.forEach(remark -> log.info("SP remark date: {}", remark.getDate()));

		// Merge and process remarks
		List<RemarksDtoForCm> mergedRemarks = processMergedRemarks(remarksFromSynergy, remarksFromSp);

		Map<String, List<RemarksDtoForCm>> groupedRemarks = mergedRemarks.stream() // .sorted((c1, c2) ->
																					// c1.getDate().compareTo(c2.getDate()))
				.collect(Collectors.groupingBy(remark -> {
					String date = remark.getDate().replaceAll("'", "");
					// Extract only the date part (before the comma and time)
					String datePart = date.split(",")[0];
					String parsedDate = DateUtil.prefixDate(datePart);
					log.debug("Parsed Date for %s with prefix is : %s", datePart, parsedDate);
					return parsedDate;
				}));
		List<RemarkHistoryResponseDto> result = groupedRemarks.entrySet().stream()
				.map(entry -> new RemarkHistoryResponseDto(entry.getKey(), // Remove the sorting prefix when displaying
						entry.getValue()))
				.collect(Collectors.toList());

		Comparator<RemarkHistoryResponseDto> comparator = (d2, d1) -> DateUtil.getLocalDate(d1.getDate())
				.compareTo(DateUtil.getLocalDate(d2.getDate()));
		return result.stream().sorted(comparator).toList();

	}

	private List<RemarksDtoForCm> processMergedRemarks(List<RemarksDtoForCm> remarksFromSynergy,
			List<RemarksDtoForCm> remarksFromSp) {
		if (remarksFromSp.isEmpty()) {
			return sortRemarksByDate(remarksFromSynergy);
		}

		for (RemarksDtoForCm synergyRemark : remarksFromSynergy) {
			for (RemarksDtoForCm spRemark : remarksFromSp) {
				String synergyDateStr = synergyRemark.getDate().replaceAll("'", "");
				String spDateStr = spRemark.getDate().replaceAll("'", "");

				// Compare dates by components instead of parsing
				try {
					if (datesAreWithinMinutes(synergyDateStr, spDateStr, 3)
							&& synergyRemark.getComment().trim().equals(spRemark.getComment().trim())) {
						synergyRemark.setOwner(spRemark.getOwner());
						synergyRemark.setSubcallType(spRemark.getSubcallType());
						synergyRemark.setEtaTime(spRemark.getEtaTime());
						break;
					}
				} catch (Exception e) {
					log.error("Error comparing dates: {} and {}", synergyDateStr, spDateStr, e);
				}
			}
		}

		return sortRemarksByDate(remarksFromSynergy);
	}

	private boolean datesAreWithinMinutes(String date1, String date2, int minutes) {
		try {
			// Extract components
			String[] parts1 = date1.split(",");
			String[] parts2 = date2.split(",");

			// Compare date parts
			if (!parts1[0].trim().equals(parts2[0].trim())) {
				return false;
			}

			// Extract and compare times
			String time1 = parts1[1].trim();
			String time2 = parts2[1].trim();

			String[] timeParts1 = time1.split(" ")[0].split(":");
			String[] timeParts2 = time2.split(" ")[0].split(":");

			int hours1 = Integer.parseInt(timeParts1[0]);
			int hours2 = Integer.parseInt(timeParts2[0]);
			int mins1 = Integer.parseInt(timeParts1[1]);
			int mins2 = Integer.parseInt(timeParts2[1]);

			// Adjust for AM/PM
			if (time1.toLowerCase().contains("pm") && hours1 < 12)
				hours1 += 12;
			if (time2.toLowerCase().contains("pm") && hours2 < 12)
				hours2 += 12;
			if (time1.toLowerCase().contains("am") && hours1 == 12)
				hours1 = 0;
			if (time2.toLowerCase().contains("am") && hours2 == 12)
				hours2 = 0;

			int totalMinutes1 = hours1 * 60 + mins1;
			int totalMinutes2 = hours2 * 60 + mins2;

			return Math.abs(totalMinutes1 - totalMinutes2) <= minutes;

		} catch (Exception e) {
			log.error("Error comparing dates: {} and {}", date1, date2, e);
			return false;
		}
	}

	private List<RemarksDtoForCm> sortRemarksByDate(List<RemarksDtoForCm> remarks) {
		Collections.sort(remarks, (r1, r2) -> {
			try {
				String date1 = r1.getDate().replaceAll("'", "");
				String date2 = r2.getDate().replaceAll("'", "");

				// Compare date components
				String[] parts1 = date1.split(",");
				String[] parts2 = date2.split(",");

				// Compare dates
				int dateCompare = parts2[0].trim().compareTo(parts1[0].trim());
				if (dateCompare != 0)
					return dateCompare;

				// Compare times
				return parts2[1].trim().compareTo(parts1[1].trim());
			} catch (Exception e) {
				log.error("Error sorting dates: {} and {}", r1.getDate(), r2.getDate(), e);
				return 0;
			}
		});
		return remarks;
	}

}