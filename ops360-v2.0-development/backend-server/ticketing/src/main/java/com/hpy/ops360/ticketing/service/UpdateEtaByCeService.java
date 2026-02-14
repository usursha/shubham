package com.hpy.ops360.ticketing.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.ImageData;
import com.hpy.ops360.ticketing.cm.dto.ImageSaveRequest;
import com.hpy.ops360.ticketing.cm.dto.ImageSaveResponse;
import com.hpy.ops360.ticketing.config.AssetServiceClient;
import com.hpy.ops360.ticketing.dto.EtaDto2;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.UpdateEtaDto;
import com.hpy.ops360.ticketing.entity.ParentChildTicketNo;
import com.hpy.ops360.ticketing.feignclient.request.dto.UpdateSubcallTypeDto;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.EtaRepository;
import com.hpy.ops360.ticketing.repository.ParentChildTicketNoRepository;
import com.hpy.ops360.ticketing.request.HimsUpdateFollowUpDto;
import com.hpy.ops360.ticketing.response.HimsResponse;
import com.hpy.ops360.ticketing.response.SynergyResponse;
import com.hpy.ops360.ticketing.util.CommonDateFormat;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UpdateEtaByCeService {

	@Autowired
    private HimsService himsService;
    
    @Autowired
    private SynergyService synergyService;
    
    @Autowired
    private ParentChildTicketNoRepository parentChildTicketNoRepository;
    
    @Autowired
    private EtaRepository etaRepository;
    
    @Autowired
    private LoginService loginService;
    
	@Autowired
	private AssetServiceClient assetServiceClient;
    
    @Autowired
    private CommonDateFormat commonDateFormat;
    
    public GenericResponseDto addEta(EtaDto2 etaDto, String token) {
        log.info("EnhancedEtaService|addEta: {}", etaDto);
        
        try {
            // Get parent-child tickets from repository
            List<ParentChildTicketNo> parentChildTicketList = parentChildTicketNoRepository.getParentChildTicketNoHims(
                loginService.getLoggedInUser(), etaDto.getAtmid(), etaDto.getTicketNumber());
            log.info("parentChildTicketList: {}", parentChildTicketList);
            // Process each ticket
            for (ParentChildTicketNo parentChildTicketNo : parentChildTicketList) {
                log.info("Processing ticket number: {}", parentChildTicketNo.getTicketid());
                
                // Determine system source (Synergy or HIMS)
                String systemSource = determineSystemSource(parentChildTicketNo.getTicketid(), etaDto.getAtmid());
                log.info("System source for ticket {}: {}", parentChildTicketNo.getTicketid(), systemSource);
                
                // Update ticket based on system source
                boolean updateSuccess = false;
                
                if ("synergy".equalsIgnoreCase(systemSource)) {
                    updateSuccess = updateSynergyTicket(parentChildTicketNo, etaDto);
                } else if ("hims".equalsIgnoreCase(systemSource)) {
                    updateSuccess = updateHimsTicket(parentChildTicketNo, etaDto);
                } else {
                    log.warn("Unknown system source: {}, defaulting to Synergy", systemSource);
                    updateSuccess = updateSynergyTicket(parentChildTicketNo, etaDto);
                }
                log.info("updateSuccess:{}",updateSuccess);
                
                if (!updateSuccess) {
                    return new GenericResponseDto("Failure", 
                        "Failed to update ticket in " + systemSource + " system");
                }
                // Save to local repository
                saveToLocalRepository(parentChildTicketNo, etaDto);

            }
            
            log.info("ETA Updated Successfully for all tickets");
            return new GenericResponseDto("Success", "ETA Updated Successfully");
            
        } catch (Exception e) {
            log.error("Error occurred while updating ETA: ", e);
            return new GenericResponseDto("Failure", "An error occurred while updating ETA");
        }
    }
    
    
    
    public GenericResponseDto addEtahims(EtaDto2 etaDto, String token) {
        log.info("EnhancedEtaService|addEta: {}", etaDto);
        
        try {
            // Get parent-child tickets from repository
            List<ParentChildTicketNo> parentChildTicketList = parentChildTicketNoRepository.getParentChildTicketNoHims(
                loginService.getLoggedInUser(), etaDto.getAtmid(), etaDto.getTicketNumber());
            log.info("parentChildTicketList: {}", parentChildTicketList);
            // Process each ticket
            for (ParentChildTicketNo parentChildTicketNo : parentChildTicketList) {
                log.info("Processing ticket number: {}", parentChildTicketNo.getTicketid());
                
                // Determine system source (Synergy or HIMS)
                String systemSource = determineSystemSource(parentChildTicketNo.getTicketid(), etaDto.getAtmid());
                log.info("System source for ticket {}: {}", parentChildTicketNo.getTicketid(), systemSource);
                
                // Update ticket based on system source
                boolean updateSuccess = false;
                
                if ("synergy".equalsIgnoreCase(systemSource)) {
                    updateSuccess = updateSynergyTicket(parentChildTicketNo, etaDto);
                } else if ("hims".equalsIgnoreCase(systemSource)) {
                    updateSuccess = updateHimsTicket(parentChildTicketNo, etaDto);
                } else {
                    log.warn("Unknown system source: {}, defaulting to Synergy", systemSource);
                    updateSuccess = updateSynergyTicket(parentChildTicketNo, etaDto);
                }
                log.info("updateSuccess:{}",updateSuccess);
                
                if (!updateSuccess) {
                    return new GenericResponseDto("Failure", 
                        "Failed to update ticket in " + systemSource + " system");
                }
                // Upload ETA documents and get URLs
                
                List<String> imageUrls = uploadEtaDocumentsAndGetUrls(etaDto, token);
                
                // Save to local repository
                saveToLocalRepository(parentChildTicketNo, etaDto, imageUrls);

            }
            
            log.info("ETA Updated Successfully for all tickets");
            return new GenericResponseDto("Success", "ETA Updated Successfully");
            
        } catch (Exception e) {
            log.error("Error occurred while updating ETA: ", e);
            return new GenericResponseDto("Failure", "An error occurred while updating ETA");
        }
    }
    
    
    
    private String determineSystemSource(String ticketId, String atmId) {
        // Query the views to determine the source system
        try {
            // Check in all_open_ticket_views first
            String source = parentChildTicketNoRepository.getTicketSource(atmId);
            if (source != null) {
                return source;
            }
            
            // Check in all_close_ticket_views if not found in open tickets
            source = parentChildTicketNoRepository.getClosedTicketSource(ticketId, atmId);
            return source != null ? source : "synergy"; // Default to synergy
            
        } catch (Exception e) {
            log.error("Error determining system source, defaulting to synergy", e);
            return "synergy";
        }
    }
    

    
    private boolean updateSynergyTicket(ParentChildTicketNo parentChildTicketNo, EtaDto2 etaDto) {
        try {
            log.info("Updating Synergy ticket: {}", parentChildTicketNo.getTicketid());

            // Update subcall type
            SynergyResponse response = synergyService.updateSubcallType(
                UpdateSubcallTypeDto.builder()
                    .atmId(etaDto.getAtmid())
                    .ticketNo(parentChildTicketNo.getTicketid())
                    .subcallType(etaDto.getSubcallType())
                    .comments(etaDto.getInternalRemark())
                    .updatedBy(loginService.getLoggedInUser())
                    .build()
            );

            log.info("Synergy subcalltype response: {}", response);

            if (response == null || !"Success".equals(response.getStatus())) {
                log.error("Failed to update subcalltype in Synergy: {}", 
                    response != null ? response.getStatus() : "No response");
                return false;
            }

            // Update ETA if provided
            if (etaDto.getEtaDateTime() != null && !etaDto.getEtaDateTime().isEmpty()) {
                String formattedEtaDateTime = validateAndFormatEtaDateTime(etaDto.getEtaDateTime());
                
                if (formattedEtaDateTime == null) {
                    log.error("Invalid ETA DateTime format: {}. Expected format: dd/MM/yyyy HH:mm", 
                        etaDto.getEtaDateTime());
                    return false;
                }

                UpdateEtaDto updateEtaDto = new UpdateEtaDto(
                    parentChildTicketNo.getTicketid(),
                    etaDto.getAtmid(),
                    formattedEtaDateTime, // Use formatted date
                    etaDto.getCreatedBy(),
                    "ETA updated by CE"
                );

                SynergyResponse etaResponse = synergyService.updateEta(updateEtaDto);
                log.info("Synergy ETA response: {}", etaResponse);

                if (etaResponse == null || !"Success".equals(etaResponse.getStatus())) {
                    log.error("Failed to update ETA in Synergy: {}", 
                        etaResponse != null ? etaResponse.getStatus() : "No response");
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            log.error("Error updating Synergy ticket", e);
            return false;
        }
    }

    /**
     * Validates and formats the ETA DateTime to the required format: dd/MM/yyyy HH:mm
     * @param etaDateTime The input ETA DateTime string
     * @return Formatted date string or null if invalid
     */
    private String validateAndFormatEtaDateTime(String etaDateTime) {
        if (etaDateTime == null || etaDateTime.trim().isEmpty()) {
            return null;
        }

        // Define the expected output format
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        // Common input formats to try
        DateTimeFormatter[] inputFormatters = {
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),     // Already correct format
            DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"),       // 2-digit year format
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),  // ISO format with seconds
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),     // ISO format without seconds
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"),     // Alternative dash format
            DateTimeFormatter.ofPattern("dd-MM-yy HH:mm"),       // 2-digit year dash format
            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"),     // US format
            DateTimeFormatter.ofPattern("MM/dd/yy HH:mm"),       // US format with 2-digit year
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")      // Alternative slash format
        };

        String trimmedDateTime = etaDateTime.trim();
        
        // Try to parse with each formatter
        for (DateTimeFormatter formatter : inputFormatters) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(trimmedDateTime, formatter);
                return dateTime.format(outputFormatter);
            } catch (DateTimeParseException e) {
                // Continue to next formatter
                continue;
            }
        }

        // If no formatter worked, log the issue and return null
        log.warn("Unable to parse ETA DateTime: {}. Supported formats: dd/MM/yyyy HH:mm, dd/MM/yy HH:mm, yyyy-MM-dd HH:mm:ss, yyyy-MM-dd HH:mm, dd-MM-yyyy HH:mm, dd-MM-yy HH:mm, MM/dd/yyyy HH:mm, MM/dd/yy HH:mm, yyyy/MM/dd HH:mm", 
            etaDateTime);
        return null;
    }
    
    private boolean updateHimsTicket(ParentChildTicketNo parentChildTicketNo, EtaDto2 etaDto) {
        try {
            log.info("Updating HIMS ticket: {}", parentChildTicketNo.getTicketid());
            
            // Create HIMS update request
            HimsUpdateFollowUpDto himsUpdateDto = HimsUpdateFollowUpDto.builder()
                .ticketid(parentChildTicketNo.getTicketid())
                .atmid(etaDto.getAtmid())
                .etadatetime(etaDto.getEtaDateTime())
                .owner(etaDto.getOwner())
                .subcalltype(etaDto.getSubcallType())
                .customerRemark(etaDto.getCustomerRemark())
                .updatedby(loginService.getLoggedInUser())
                .comment(etaDto.getInternalRemark())
                .build();
            
            HimsResponse response = himsService.updateFollowUpDetails(himsUpdateDto);
            log.info("HIMS response: {}", response);
            
            if (response == null || !"Success".equals(response.getStatus())) {
                log.error("Failed to update HIMS ticket: {}", 
                    response != null ? response.getStatus() : "No response");
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            log.error("Error updating HIMS ticket", e);
            return false;
        }
    }
    
    
    
    private void saveToLocalRepository(ParentChildTicketNo parentChildTicketNo, EtaDto2 etaDto) {
        try {
            etaRepository.addEta(
                etaDto.getAtmid(),
                etaDto.getCustomerRemark(),
                etaDto.getEtaDocumentDto().getDocument(),
                etaDto.getEtaDocumentDto().getDocumentName(),
                commonDateFormat.getLocalDateTimeFormat(etaDto.getEtaDateTime()),
                etaDto.getInternalRemark(),
                etaDto.getOwner(),
                etaDto.getSubcallType(),
                parentChildTicketNo.getTicketid(),
                loginService.getLoggedInUser(),
                etaDto.getEtaDocumentDto().getDocument2(),
                etaDto.getEtaDocumentDto().getDocument1Name(),
                etaDto.getEtaDocumentDto().getDocument2(),
                etaDto.getEtaDocumentDto().getDocument2Name(),
                etaDto.getEtaDocumentDto().getDocument3(),
                etaDto.getEtaDocumentDto().getDocument3Name(),
                etaDto.getEtaDocumentDto().getDocument4(),
                etaDto.getEtaDocumentDto().getDocument4Name()
            );
            
            log.info("Saved ETA to local repository for ticket: {}", parentChildTicketNo.getTicketid());
            
        } catch (Exception e) {
            log.error("Error saving to local repository", e);
        }
    }
    
    
    private void saveToLocalRepository(ParentChildTicketNo parentChildTicketNo, EtaDto2 etaDto, List<String> imageUrls) {
        String ticketId = parentChildTicketNo.getTicketid();
        String atmId = etaDto.getAtmid();

        log.info("🗂️ Initiating save of ETA data to local repository for Ticket: {}, ATM ID: {}", ticketId, atmId);

        try {
            // Extract image URLs safely
            String doc1 = imageUrls.size() > 0 ? imageUrls.get(0) : "";
            String doc2 = imageUrls.size() > 1 ? imageUrls.get(1) : "";
            String doc3 = imageUrls.size() > 2 ? imageUrls.get(2) : "";
            String doc4 = imageUrls.size() > 3 ? imageUrls.get(3) : "";
            String doc5 = imageUrls.size() > 4 ? imageUrls.get(4) : "";

            log.debug("📎 Document URLs prepared for Ticket {}: [doc1={}, doc2={}, doc3={}, doc4={}, doc5={}]",
                      ticketId, doc1, doc2, doc3, doc4, doc5);

            log.debug("📝 ETA metadata for Ticket {}: [CustomerRemark={}, InternalRemark={}, Owner={}, SubcallType={}, ETA DateTime={}]",
                      ticketId,
                      etaDto.getCustomerRemark(),
                      etaDto.getInternalRemark(),
                      etaDto.getOwner(),
                      etaDto.getSubcallType(),
                      etaDto.getEtaDateTime());

            etaRepository.addEta(
                atmId,
                etaDto.getCustomerRemark(),
                doc1,
                etaDto.getEtaDocumentDto().getDocumentName(),
                commonDateFormat.getLocalDateTimeFormat(etaDto.getEtaDateTime()),
                etaDto.getInternalRemark(),
                etaDto.getOwner(),
                etaDto.getSubcallType(),
                ticketId,
                loginService.getLoggedInUser(),
                doc2,
                etaDto.getEtaDocumentDto().getDocument1Name(),
                doc3,
                etaDto.getEtaDocumentDto().getDocument2Name(),
                doc4,
                etaDto.getEtaDocumentDto().getDocument3Name(),
                doc5,
                etaDto.getEtaDocumentDto().getDocument4Name()
            );

            log.info("✅ Successfully saved ETA data to local repository for Ticket: {}", ticketId);

        } catch (Exception e) {
            log.error("❌ Failed to save ETA data to local repository for Ticket: {}. Exception: {}", ticketId, e.getMessage(), e);
        }
    }


    
    
    private List<String> uploadEtaDocumentsAndGetUrls(EtaDto2 etaDto, String token) {
        String ticketNumber = etaDto.getTicketNumber();
        String atmId = etaDto.getAtmid();

        log.info("📤 Starting ETA document upload for Ticket: {}, ATM ID: {}", ticketNumber, atmId);

        List<ImageData> mediaFiles = Stream.of(
                new ImageData(etaDto.getEtaDocumentDto().getDocumentName(), etaDto.getEtaDocumentDto().getDocument()),
                new ImageData(etaDto.getEtaDocumentDto().getDocument1Name(), etaDto.getEtaDocumentDto().getDocument1()),
                new ImageData(etaDto.getEtaDocumentDto().getDocument2Name(), etaDto.getEtaDocumentDto().getDocument2()),
                new ImageData(etaDto.getEtaDocumentDto().getDocument3Name(), etaDto.getEtaDocumentDto().getDocument3()),
                new ImageData(etaDto.getEtaDocumentDto().getDocument4Name(), etaDto.getEtaDocumentDto().getDocument4())
            )
            .filter(dto -> dto.getBase64Content() != null && !dto.getBase64Content().isBlank())
            .collect(Collectors.toList());

        log.info("📝 Prepared {} image(s) for upload for Ticket: {}", mediaFiles.size(), ticketNumber);

        ImageSaveRequest assetRequest = new ImageSaveRequest();
        assetRequest.setAtmId(atmId);
        assetRequest.setTicketNo(ticketNumber);
        assetRequest.setImages(mediaFiles);

        log.debug("📦 ImageSaveRequest payload: {}", assetRequest);

        try {
            log.info("🚀 Sending image upload request to Asset Service for Ticket: {}", ticketNumber);
            ImageSaveResponse response = assetServiceClient.saveImages(token, assetRequest);

            if (response != null && response.getImageUrls() != null && !response.getImageUrls().isEmpty()) {
                log.info("✅ Successfully uploaded images for Ticket: {}. Received {} image URL(s).",
                         ticketNumber, response.getImageUrls().size());
                return response.getImageUrls();
            } else {
                log.warn("⚠️ Image upload completed but no URLs returned for Ticket: {}", ticketNumber);
            }
        } catch (Exception ex) {
            log.error("❌ Failed to upload ETA documents for Ticket: {}. Exception: {}", ticketNumber, ex.getMessage(), ex);
        }

        log.info("📭 Returning empty image URL list for Ticket: {}", ticketNumber);
        return Collections.emptyList();
    }


}
