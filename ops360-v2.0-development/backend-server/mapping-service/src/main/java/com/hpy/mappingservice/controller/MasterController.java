package com.hpy.mappingservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hpy.mappingservice.request.dto.ATMReassignmentRequest;
import com.hpy.mappingservice.response.dto.ATMReassignmentResponse;
import com.hpy.mappingservice.response.dto.AtmMasterDto;
import com.hpy.mappingservice.response.dto.CityMasterDTO;
import com.hpy.mappingservice.response.dto.StateMasterDTO;
import com.hpy.mappingservice.response.dto.UserMasterDto;
import com.hpy.mappingservice.response.dto.ZoneMasterDTO;
import com.hpy.mappingservice.service.ATMAssignmentService_ManualAssignUsingOLA;
import com.hpy.mappingservice.service.ATMAssignmentService_UsingOLA;
import com.hpy.mappingservice.service.MasterService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/master")
@Slf4j
public class MasterController {

	private MasterService service;

	private RestUtils restUtils;

	@Autowired
	private ATMAssignmentService_UsingOLA assignmentService_UsingOLA;

	@Autowired
	private ATMAssignmentService_ManualAssignUsingOLA service_ManualAssign;

	public MasterController(MasterService service, RestUtils restUtils) {
		super();
		this.service = service;
		this.restUtils = restUtils;
	}

	@GetMapping("/get-all-atm-details")
	public ResponseEntity<IResponseDto> getAllAtms() {
		return ResponseEntity.ok(restUtils.wrapResponse(service.findAll(), "All atm details fetched successfully."));
	}

	@GetMapping("/atm-details/{id}")
	public ResponseEntity<IResponseDto> getAtmById(@PathVariable Long id) {

		return ResponseEntity.ok(restUtils.wrapResponse(service.findById(id), "Fetched atm details by id."));

	}

	@PostMapping("/save-atm-details")
	public ResponseEntity<IResponseDto> createAtm(@RequestBody AtmMasterDto atmMaster) {
		return ResponseEntity.ok(restUtils.wrapResponse(service.save(atmMaster), "Atm details saved successfully !!!"));
	}

	@PostMapping("/upload-atm-details")
	public ResponseEntity<IResponseDto> uploadFile(@RequestParam("file") MultipartFile file)
			throws InterruptedException {

		return ResponseEntity.ok(restUtils.wrapResponse(service.save(file), "Atm Details uploaded successfully."));

	}

	@PostMapping("/upload-user-details")
	public ResponseEntity<IResponseDto> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
		return ResponseEntity
				.ok(restUtils.wrapResponse(service.saveAllUsers(file), "User data uploaded successfully!"));
	}

	@PostMapping("/save-user-details")
	public ResponseEntity<IResponseDto> saveUserDetails(@RequestBody UserMasterDto userDetails) {
		return ResponseEntity.ok(restUtils.wrapResponse(service.createUser(userDetails), "User saved successfully!!!"));
	}

	@GetMapping("/get-all-user-details")
	public ResponseEntity<IResponseDto> getAllUserDetails() {
		return ResponseEntity
				.ok(restUtils.wrapResponse(service.getAllUsers(), "User details list fetched successfully"));
	}

	@PostMapping("/save-zone")
	public ResponseEntity<IResponseDto> saveZones(@RequestBody ZoneMasterDTO zoneMasterDTO) {
		return ResponseEntity.ok(restUtils.wrapResponse(service.saveZone(zoneMasterDTO), "zone saved successfully"));
	}

	@PostMapping("/save-state")
	public ResponseEntity<IResponseDto> saveState(@RequestBody StateMasterDTO stateMasterDTO) {
		return ResponseEntity.ok(restUtils.wrapResponse(service.saveState(stateMasterDTO), "state saved successfully"));
	}

	@PostMapping("/save-city")
	public ResponseEntity<IResponseDto> saveCity(@RequestBody CityMasterDTO cityMasterDTO) {
		return ResponseEntity.ok(restUtils.wrapResponse(service.saveCity(cityMasterDTO), "city saved successfully"));
	}

	@GetMapping("/get-all-zones")
	public ResponseEntity<IResponseDto> getAllZones() {
		return ResponseEntity.ok(restUtils.wrapResponse(service.getAllZones(), "zone list returned!!"));
	}

	@GetMapping("/get-all-states")
	public ResponseEntity<IResponseDto> getAllStates() {
		return ResponseEntity.ok(restUtils.wrapResponse(service.getAllStates(), "state list returned!!"));
	}

	@GetMapping("/get-all-city")
	public ResponseEntity<IResponseDto> getAllCities() {
		return ResponseEntity.ok(restUtils.wrapResponse(service.getAllCities(), "city list returned!!"));
	}

	@PostMapping("/upload-zone-state-city-mapping")
	public ResponseEntity<IResponseDto> uploadAndSaveZoneStateCityMapping(@RequestParam("file") MultipartFile file)
			throws IOException {
		return ResponseEntity
				.ok(restUtils.wrapResponse(service.processExcelFileForZoneStateCityMapping(file), "success"));
	}

	@PostMapping("/upload-user-handled-locations-mapping")
	public ResponseEntity<IResponseDto> uploadAndSaveUserHandledLocationsMapping(
			@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
		return ResponseEntity.ok(restUtils.wrapResponse(service.processUserLocationExcel(file), "success"));
	}

	@PostMapping("/upload-eventcodes")
	public ResponseEntity<IResponseDto> uploadEventcodeFile(@RequestParam("file") MultipartFile file)
			throws IOException, Exception {
		int rows = service.uploadEventcodeExcel(file.getInputStream());
		return ResponseEntity.ok(restUtils.wrapResponse("Upload successful! Rows inserted:" + rows, "success"));
	}

	@PostMapping("/reassign")
	public ResponseEntity<List<ATMReassignmentResponse>> reassignATMs(@RequestBody ATMReassignmentRequest request) {

		log.info("Processing ATM reassignment request for leaving CE: {} using OLA Maps", request.getLeavingCeUserId());

		try {
			List<ATMReassignmentResponse> reassignments = assignmentService_UsingOLA.reassignATMs(request);

			log.info("Successfully processed reassignment for {} ATMs using OLA Maps service", reassignments.size());

			return ResponseEntity.ok(reassignments);

		} catch (RuntimeException e) {
			log.error("Business error during ATM reassignment using OLA Maps: {}", e.getMessage());
			return ResponseEntity.badRequest().build();

		} catch (Exception e) {
			log.error("Unexpected error during ATM reassignment using OLA Maps: {}", e.getMessage(), e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/manual-reassignATM/{atmId}")
	public ResponseEntity<List<ATMReassignmentResponse>> manualReassignATM(@PathVariable String atmId) {

		log.info("Processing manual reassignment for ATM: {} using OLA Maps", atmId);

		try {
			List<ATMReassignmentResponse> reassignments = service_ManualAssign.manualReassignSingleATM(atmId);

			log.info("Successfully processed manual reassignment for ATM: {} using OLA Maps service. Found {} options",
					atmId, reassignments.size());

			return ResponseEntity.ok(reassignments);

		} catch (IllegalArgumentException e) {
			log.error("Invalid input for manual ATM reassignment - ATM {}: {}", atmId, e.getMessage());
			return ResponseEntity.badRequest().build();

		} catch (RuntimeException e) {
			log.error("Business error during manual ATM reassignment for ATM {} using OLA Maps: {}", atmId,
					e.getMessage());
			return ResponseEntity.badRequest().build();

		} catch (Exception e) {
			log.error("Unexpected error during manual ATM reassignment for ATM {} using OLA Maps: {}", atmId,
					e.getMessage(), e);
			return ResponseEntity.internalServerError().build();
		}
	}
}
