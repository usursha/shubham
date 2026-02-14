package com.hpy.mappingservice.service.bulkservice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.bulkdto.ATMMappingEntryDTO;
import com.hpy.mappingservice.bulkdto.ATMMappingRequestDTO;
import com.hpy.mappingservice.entity.ResetReason;
import com.hpy.mappingservice.entity.bulkentity.AtmMasterAlt;
import com.hpy.mappingservice.entity.bulkentity.CeToCeMappingAlt;
import com.hpy.mappingservice.entity.bulkentity.LeaveRequestAlt;
import com.hpy.mappingservice.repository.ExtendCEmappingRepository;
import com.hpy.mappingservice.repository.ResetReasonRepository;
import com.hpy.mappingservice.repository.bulkrepo.AtmCeMappingAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.AtmMasterAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.CeToCeMappingAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.LeaveRequestAltRepository;
import com.hpy.mappingservice.request.dto.ATMExtendRequestDTO;
import com.hpy.mappingservice.request.dto.ATMResettingRequestDTO;
import com.hpy.mappingservice.request.dto.ExtendCEmapping;
import com.hpy.mappingservice.service.LoginService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ATMMappingAltService {

	@Autowired
	private AtmMasterAltRepository atmRepo;

	@Autowired
	private AtmCeMappingAltRepository atmMappingRepo;

	@Autowired
	private CeToCeMappingAltRepository ceToCeRepo;

	@Autowired
	private LeaveRequestAltRepository leaveRepo;

	@Autowired
	private ResetReasonRepository resetReasonRepo;

	@Autowired
	private ExtendCEmappingRepository extendCEmappingRepository;

	@Autowired
	private LoginService loginService;

	public String assignMappings(ATMMappingRequestDTO request) {
		log.info("Starting alternate ATM reassignment for leaveId: {}", request.getLeaveId());

		LeaveRequestAlt leave = leaveRepo.findById(request.getLeaveId()).orElseThrow(() -> {
			log.error("Leave request not found for ID: {}", request.getLeaveId());
			return new RuntimeException("Leave not found");
		});

		for (ATMMappingEntryDTO entry : request.getMappings()) {
			String[] atmCodes = entry.getAtmList().split(",");

			for (String code : atmCodes) {
				String trimmedCode = code.trim();
				AtmMasterAlt atm = atmRepo.findByAtmCode(trimmedCode).orElseThrow(() -> {
					log.error("ATM not found for code: {}", trimmedCode);
					return new RuntimeException("ATM not found: " + trimmedCode);
				});

				atmMappingRepo.findByAtmIdAndCeId(atm.getId(), entry.getCeId()).ifPresentOrElse(mapping -> {
					mapping.setTemporaryCeId(String.valueOf(entry.getTempMappedCeId()));
					atmMappingRepo.save(mapping);
					log.info("Updated temporary CE for ATM {} to CE {}", trimmedCode, entry.getTempMappedCeId());
				}, () -> {
					log.warn("Mapping not found for ATM ID {} and CE ID {}", atm.getId(), entry.getCeId());
				});

				boolean exists = ceToCeRepo.existsByAtmIdAndPrimaryCeIdAndMappedCeIdAndActive(atm.getAtmCode(),
						entry.getCeId().intValue(), entry.getTempMappedCeId().intValue(), 1);

				if (!exists) {
					CeToCeMappingAlt newMapping = new CeToCeMappingAlt();
					newMapping.setPrimaryCeId(entry.getCeId().intValue());
					newMapping.setAtmId(atm.getAtmCode());
					newMapping.setMappedCeId(entry.getTempMappedCeId().intValue());
					newMapping.setFromDate(leave.getCustomStartTime());
					newMapping.setToDate(leave.getCustomEndTime());
					newMapping.setActive(0);
					newMapping.setMappedType(request.getMappedType());
					newMapping.setLeaveRequestId(request.getLeaveId());
					ceToCeRepo.save(newMapping);
					log.info("Inserted new CE-to-CE mapping for ATM {} from CE {} to CE {}", trimmedCode,
							entry.getCeId(), entry.getTempMappedCeId());
				} else {
					log.info("Mapping already exists for ATM {} and CE {}", trimmedCode, entry.getTempMappedCeId());
				}
			}
		}

		log.info("ATM reassignment completed successfully for leaveId: {}", request.getLeaveId());
		return "Mapping Successful";
	}

	public String resetMappings(ATMResettingRequestDTO request) {
		log.info("Starting ATM reset for leaveIds: {}", request.getLeaveId());

		List<Long> leaveIds = Arrays.stream(request.getLeaveId().split(",")).map(String::trim).map(Long::parseLong)
				.collect(Collectors.toList());

		for (Long leaveId : leaveIds) {
			LeaveRequestAlt leave = leaveRepo.findById(leaveId).orElseThrow(() -> {
				log.error("Leave request not found for ID: {}", leaveId);
				return new RuntimeException("Leave not found");
			});

			List<CeToCeMappingAlt> mappings = ceToCeRepo.findByLeaveRequestId(leaveId);

			for (CeToCeMappingAlt mapping : mappings) {
				String atmCode = mapping.getAtmId();

				AtmMasterAlt atm = atmRepo.findByAtmCode(atmCode).orElseThrow(() -> {
					log.error("ATM not found for code: {}", atmCode);
					return new RuntimeException("ATM not found: " + atmCode);
				});

				atmMappingRepo.findByAtmIdAndCeId(atm.getId(), (long) mapping.getPrimaryCeId())
						.ifPresent(atmMapping -> {
							atmMapping.setTemporaryCeId(null);
							atmMappingRepo.save(atmMapping);
							log.info("Cleared temporary CE for ATM {}", atmCode);
						});

				mapping.setActive(0);
				ceToCeRepo.save(mapping);
				log.info("Deactivated CE-to-CE mapping for ATM {}", atmCode);

				ResetReason reason = new ResetReason();
				reason.setReason(request.getReason());
				reason.setReasonComment(request.getReasoncomment());
				reason.setAtmId(atm.getId());
				reason.setLeaveRequestId(leaveId);
				reason.setUpdatedBy(loginService.getLoggedInUser());
				reason.setUpdatedAt(LocalDateTime.now());
				resetReasonRepo.save(reason);
				log.info("Saved reset reason for ATM {}", atmCode);
			}

			leave.setStatus("Completed");
			leaveRepo.save(leave);
			log.info("Marked leave ID {} as Completed", leaveId);
		}

		return "ATM mappings reset successfully for all provided leave IDs";
	}

	public String extendMappings(List<ATMExtendRequestDTO> requests) {
		log.info("Starting ATM extension for {} leave requests", requests.size());

		List<ExtendCEmapping> history = new ArrayList<>();

		for (ATMExtendRequestDTO request : requests) {
			log.info("Processing leaveId: {}", request.getLeaveId());

			if (request.getExtendDate() == null || request.getExtendDate().isBefore(LocalDateTime.now())) {
				log.warn("Invalid extend date for leaveId: {}", request.getLeaveId());
				throw new IllegalArgumentException(
						"Extend date must be in the future for leaveId: " + request.getLeaveId());
			}

			try {
				List<CeToCeMappingAlt> mappings = ceToCeRepo.findByLeaveRequestId(request.getLeaveId());

				for (CeToCeMappingAlt mapping : mappings) {
					String atmCode = mapping.getAtmId();

					mapping.setToDate(request.getExtendDate());
					ceToCeRepo.save(mapping);
					log.info("Extended CE-to-CE mapping for ATM {}", atmCode);

					ExtendCEmapping record = new ExtendCEmapping();
					record.setAtmId(atmCode);
					record.setLeaveRequestId(request.getLeaveId());
					record.setUpdatedBy(loginService.getLoggedInUser());
					record.setUpdatedAt(LocalDateTime.now());
					history.add(record);
					log.info("Logged extension history for ATM {}", atmCode);
				}

			} catch (Exception e) {
				log.error("Error while extending mappings for leaveId: {}", request.getLeaveId(), e);
				throw new RuntimeException("Failed to extend mappings for leaveId: " + request.getLeaveId());
			}
		}

		extendCEmappingRepository.saveAll(history);
		return "ATM mappings extended successfully for all provided leave IDs";
	}

}
