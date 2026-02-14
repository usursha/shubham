//package com.hpy.mappingservice.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Row;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.hpy.mappingservice.entity.AtmMaster;
//import com.hpy.mappingservice.entity.CityMaster;
//import com.hpy.mappingservice.entity.EventCode;
//import com.hpy.mappingservice.entity.StateMaster;
//import com.hpy.mappingservice.entity.UserMaster;
//import com.hpy.mappingservice.entity.ZoneMaster;
//import com.hpy.mappingservice.repository.AtmMasterRepository;
//import com.hpy.mappingservice.repository.CityMasterRepository;
//import com.hpy.mappingservice.repository.EventCodeRepository;
//import com.hpy.mappingservice.repository.StateMasterRepository;
//import com.hpy.mappingservice.repository.UserLocationHandlingRepository;
//import com.hpy.mappingservice.repository.UserMasterRepository;
//import com.hpy.mappingservice.repository.ZoneMasterRepository;
//import com.hpy.mappingservice.response.dto.AtmMasterDto;
//import com.hpy.mappingservice.response.dto.CityMasterDTO;
//import com.hpy.mappingservice.response.dto.StateMasterDTO;
//import com.hpy.mappingservice.response.dto.UserMasterDto;
//import com.hpy.mappingservice.response.dto.ZoneMasterDTO;
//import com.hpy.rest.dto.IResponseDto;
//
//@ExtendWith(MockitoExtension.class)
//class MasterServiceTest {
//	@Mock
//	private AtmMasterRepository repository;
//
//	@InjectMocks
//	private MasterService service;
//
//	@Mock
//	private UserMasterRepository userMasterRepository;
//
//	@Mock
//	private ZoneMasterRepository zoneMasterRepository;
//
//	@Mock
//	private StateMasterRepository stateMasterRepository;
//
//	@Mock
//	private CityMasterRepository cityMasterRepository;
//
//	@Mock
//	private UserLocationHandlingRepository userLocationHandlingRepository;
//
//	@Mock
//	private MultipartFile mockFile;
//
//	private ZoneMaster zoneMaster;
//	private StateMaster stateMaster;
//	private CityMaster cityMaster;
//
//	private UserMaster userMaster1;
//	private UserMaster userMaster2;
//	private UserMasterDto userMasterDto;
//
//	private AtmMaster atmMaster1;
//	private AtmMaster atmMaster2;
//	private AtmMasterDto atmMasterDto;
//	
//	@Mock 
//	private EventCodeRepository eventcodeRepository;
//	private EventCode eventcode;
//	private IResponseDto iresponsedto;
//
//	@BeforeEach
//	void setUp() {
//		atmMaster1 = new AtmMaster();
//		atmMaster1.setId(1L);
//		atmMaster1.setAtmCode("ATM001");
//		atmMaster1.setBankName("Bank A");
//		atmMaster1.setGrade("A");
//		atmMaster1.setCity("City A");
//		atmMaster1.setState("State A");
//		atmMaster1.setAddress("Address A");
//
//		atmMaster2 = new AtmMaster();
//		atmMaster2.setId(2L);
//		atmMaster2.setAtmCode("ATM002");
//		atmMaster2.setBankName("Bank B");
//		atmMaster2.setGrade("B");
//		atmMaster2.setCity("City B");
//		atmMaster2.setState("State B");
//		atmMaster2.setAddress("Address B");
//
//		userMaster1 = new UserMaster();
//		userMaster1.setId(1L);
//		userMaster1.setUsername("mahesh.patil");
//		userMaster1.setDesignation("Channel Executive");
//		userMaster1.setFullName("Mahesh Patil");
//		userMaster1.setMobileno("9977890917");
//		userMaster1.setEmailId("abc@gmail.com");
//		userMaster1.setEmployeeCode("HI1234");
//		userMaster1.setCity("Mumbai");
//		userMaster1.setState("Maharashtra");
//		userMaster1.setZone("West");
//
//		userMaster2 = new UserMaster();
//		userMaster2.setId(1L);
//		userMaster2.setUsername("rishabh.jain");
//		userMaster2.setDesignation("Channel Executive");
//		userMaster2.setFullName("Rishabh Jain");
//		userMaster2.setMobileno("9977890234");
//		userMaster2.setEmailId("abcd@gmail.com");
//		userMaster2.setEmployeeCode("HI1256");
//		userMaster2.setCity("Mumbai");
//		userMaster2.setState("Maharashtra");
//		userMaster2.setZone("West");
//
//		atmMasterDto = AtmMasterDto.builder().atmId(1L).atmCode("ATM001").bankName("Bank A").grade("A").city("City A")
//				.state("State A").address("Address A").build();
//
//		userMasterDto = new UserMasterDto();
//		userMasterDto.setId1(1L);
//		userMasterDto.setUsername("mahesh.patil");
//		userMasterDto.setDesignation("Channel Executive");
//		userMasterDto.setFullName("Mahesh Patil");
//		userMasterDto.setMobileno("9977890917");
//		userMasterDto.setEmailId("abc@gmail.com");
//		userMasterDto.setEmployeeCode("HI1234");
//		userMasterDto.setCity("Mumbai");
//		userMasterDto.setState("Maharashtra");
//		userMasterDto.setZone("West");
//
//		zoneMaster = new ZoneMaster();
//		zoneMaster.setZoneId(1L);
//		zoneMaster.setZoneName("Zone 1");
//
//		stateMaster = new StateMaster();
//		stateMaster.setStateId(1L);
//		stateMaster.setStateName("State 1");
//		stateMaster.setZoneMaster(zoneMaster);
//
//		cityMaster = new CityMaster();
//		cityMaster.setCityId(1L);
//		cityMaster.setCityName("City 1");
//		cityMaster.setStateMaster(stateMaster);
//	}
//
//	@BeforeEach
//	public void setup() {
//		MockitoAnnotations.openMocks(this);
//	}
//
//	@Test
//	void testFindAll_ReturnsListOfAtmMasterDtos() {
//		when(repository.findAll()).thenReturn(Arrays.asList(atmMaster1, atmMaster2));
//
//		List<AtmMasterDto> result = service.findAll();
//
//		assertNotNull(result);
//		assertEquals(2, result.size());
//		assertEquals("ATM001", result.get(0).getAtmCode());
//		assertEquals("ATM002", result.get(1).getAtmCode());
//	}
//
//	@Test
//	void testFindAll_ReturnsEmptyList() {
//		when(repository.findAll()).thenReturn(Collections.emptyList());
//
//		List<AtmMasterDto> result = service.findAll();
//
//		assertNotNull(result);
//		assertTrue(result.isEmpty());
//	}
//
//	@Test
//	void testFindById_ReturnsAtmMasterDto() {
//		when(repository.findById(1L)).thenReturn(Optional.of(atmMaster1));
//
//		AtmMasterDto result = service.findById(1L);
//
//		assertNotNull(result);
//		assertEquals(1L, result.getAtmId());
//		assertEquals("ATM001", result.getAtmCode());
//		assertEquals("Bank A", result.getBankName());
//		assertEquals("A", result.getGrade());
//		assertEquals("City A", result.getCity());
//		assertEquals("State A", result.getState());
//		assertEquals("Address A", result.getAddress());
//	}
//
//	@Test
//	void testFindById_ReturnsEmptyAtmMasterDto() {
//		when(repository.findById(1L)).thenReturn(Optional.empty());
//
//		AtmMasterDto result = service.findById(1L);
//
//		assertNotNull(result);
//		assertEquals(0L, result.getAtmId());
//		assertEquals("", result.getAtmCode());
//		assertEquals("", result.getBankName());
//		assertEquals("", result.getGrade());
//		assertEquals("", result.getCity());
//		assertEquals("", result.getState());
//		assertEquals("", result.getAddress());
//	}
//
//	@Test
//	void testSave_ReturnsSavedAtmMasterDto() {
//		when(repository.save(any(AtmMaster.class))).thenReturn(atmMaster1);
//
//		AtmMasterDto result = service.save(atmMasterDto);
//
//		assertNotNull(result);
//		assertEquals(1L, result.getAtmId());
//		assertEquals("ATM001", result.getAtmCode());
//		assertEquals("Bank A", result.getBankName());
//		assertEquals("A", result.getGrade());
//		assertEquals("City A", result.getCity());
//		assertEquals("State A", result.getState());
//		assertEquals("Address A", result.getAddress());
//	}
//
//	@Test
//	void testSave_ReturnsSavedUserMasterDto() {
//		when(userMasterRepository.save(any(UserMaster.class))).thenReturn(userMaster1);
//		UserMasterDto userDto = service.createUser(userMasterDto);
//		assertNotNull(userDto);
//		assertEquals(1L, userDto.getId1());
//		assertEquals("mahesh.patil", userDto.getUsername());
//		assertEquals("Channel Executive", userDto.getDesignation());
//		assertEquals("Mahesh Patil", userDto.getFullName());
//		assertEquals("9977890917", userDto.getMobileno());
//		assertEquals("abc@gmail.com", userDto.getEmailId());
//		assertEquals("HI1234", userDto.getEmployeeCode());
//		assertEquals("Mumbai", userDto.getCity());
//		assertEquals("Maharashtra", userDto.getState());
//		assertEquals("West", userDto.getZone());
//
//	}
//
//	@Test
//	void testGetAllUsers_ReturnsUserList() {
//		// Arrange: Mock the repository's findAll method to return a list of UserMaster
//		// entities
//		when(userMasterRepository.findAll()).thenReturn(Arrays.asList(userMaster1, userMaster2));
//
//		// Act: Call the getAllUsers method
//		List<UserMasterDto> result = service.getAllUsers();
//
//		// Assert: Verify that the result contains the expected DTOs
//		assertNotNull(result);
//		assertEquals(2, result.size());
//		assertEquals("mahesh.patil", result.get(0).getUsername());
//		assertEquals("rishabh.jain", result.get(1).getUsername());
//
//		// Verify that findAll was called exactly once
//		verify(userMasterRepository, times(1)).findAll();
//	}
//
//	@Test
//	void testGetAllUsers_ReturnsEmptyList() {
//		// Arrange: Mock the repository's findAll method to return an empty list
//		when(userMasterRepository.findAll()).thenReturn(Collections.emptyList());
//
//		// Act: Call the getAllUsers method
//		List<UserMasterDto> result = service.getAllUsers();
//
//		// Assert: Validate that an empty list is returned
//		assertNotNull(result);
//		assertTrue(result.isEmpty());
//
//		// Verify that findAll was called exactly once
//		verify(userMasterRepository, times(1)).findAll();
//	}
//
//	@Test
//	void testGetUserById_ReturnsUser() {
//		// Arrange: Mock the repository's findById method to return an Optional
//		// containing userMaster1
//		when(userMasterRepository.findById(anyLong())).thenReturn(Optional.of(userMaster1));
//
//		// Act: Call the getUserById method
//		Optional<UserMasterDto> result = service.getUserById(1L);
//
//		// Assert: Validate the returned UserMasterDto is as expected
//		assertTrue(result.isPresent());
//		assertEquals("mahesh.patil", result.get().getUsername());
//
//		// Verify that findById was called with the correct ID
//		verify(userMasterRepository, times(1)).findById(1L);
//	}
//
//	@Test
//	void testGetUserById_ReturnsEmptyOptional() {
//		// Arrange: Mock the repository's findById method to return an empty Optional
//		when(userMasterRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//		// Act: Call the getUserById method
//		Optional<UserMasterDto> result = service.getUserById(1L);
//
//		// Assert: Validate that an empty Optional is returned
//		assertFalse(result.isPresent());
//
//		// Verify that findById was called with the correct ID
//		verify(userMasterRepository, times(1)).findById(1L);
//	}
//
//	@Test
//	void testSaveZone() {
//		when(zoneMasterRepository.save(any(ZoneMaster.class))).thenReturn(zoneMaster);
//
//		ZoneMasterDTO zoneMasterDTO = new ZoneMasterDTO();
//		zoneMasterDTO.setZoneName("Zone 1");
//
//		ZoneMasterDTO savedZone = service.saveZone(zoneMasterDTO);
//
//		assertNotNull(savedZone);
//		assertEquals("Zone 1", savedZone.getZoneName());
//		verify(zoneMasterRepository, times(1)).save(any(ZoneMaster.class));
//	}
//
//	@Test
//	void testSaveState() {
//		when(stateMasterRepository.save(any(StateMaster.class))).thenReturn(stateMaster);
//
//		StateMasterDTO stateMasterDTO = new StateMasterDTO();
//		stateMasterDTO.setStateName("State 1");
//		stateMasterDTO.setZoneId(1L);
//
//		StateMasterDTO savedState = service.saveState(stateMasterDTO);
//
//		assertNotNull(savedState);
//		assertEquals("State 1", savedState.getStateName());
//		assertEquals(1L, savedState.getZoneId());
//		verify(stateMasterRepository, times(1)).save(any(StateMaster.class));
//	}
//
//	@Test
//	void testSaveCity() {
//		when(cityMasterRepository.save(any(CityMaster.class))).thenReturn(cityMaster);
//
//		CityMasterDTO cityMasterDTO = new CityMasterDTO();
//		cityMasterDTO.setCityName("City 1");
//		cityMasterDTO.setStateId(1L);
//
//		CityMasterDTO savedCity = service.saveCity(cityMasterDTO);
//
//		assertNotNull(savedCity);
//		assertEquals("City 1", savedCity.getCityName());
//		assertEquals(1L, savedCity.getStateId());
//		verify(cityMasterRepository, times(1)).save(any(CityMaster.class));
//	}
//
//	@Test
//	void testGetAllZones() {
//		when(zoneMasterRepository.findAll()).thenReturn(Arrays.asList(zoneMaster));
//
//		List<ZoneMasterDTO> zones = service.getAllZones();
//
//		assertNotNull(zones);
//		assertEquals(1, zones.size());
//		assertEquals("Zone 1", zones.get(0).getZoneName());
//	}
//
//	@Test
//	void testGetAllStates() {
//		when(stateMasterRepository.findAll()).thenReturn(Arrays.asList(stateMaster));
//
//		List<StateMasterDTO> states = service.getAllStates();
//
//		assertNotNull(states);
//		assertEquals(1, states.size());
//		assertEquals("State 1", states.get(0).getStateName());
//	}
//
//	@Test
//	void testGetAllCities() {
//		when(cityMasterRepository.findAll()).thenReturn(Arrays.asList(cityMaster));
//
//		List<CityMasterDTO> cities = service.getAllCities();
//
//		assertNotNull(cities);
//		assertEquals(1, cities.size());
//		assertEquals("City 1", cities.get(0).getCityName());
//	}
//
//	// Negative Test Case for retrieval methods
//	@Test
//	void testGetAllZones_WhenNoZonesExist() {
//		when(zoneMasterRepository.findAll()).thenReturn(Collections.emptyList());
//
//		List<ZoneMasterDTO> zones = service.getAllZones();
//
//		assertNotNull(zones);
//		assertTrue(zones.isEmpty());
//	}
//
//	@Test
//	void testGetAllStates_WhenNoStatesExist() {
//		when(stateMasterRepository.findAll()).thenReturn(Collections.emptyList());
//
//		List<StateMasterDTO> states = service.getAllStates();
//
//		assertNotNull(states);
//		assertTrue(states.isEmpty());
//	}
//
//	@Test
//	void testGetAllCities_WhenNoCitiesExist() {
//		when(cityMasterRepository.findAll()).thenReturn(Collections.emptyList());
//
//		List<CityMasterDTO> cities = service.getAllCities();
//
//		assertNotNull(cities);
//		assertTrue(cities.isEmpty());
//	}
//
//	// Negative Test Case for saveZone
//	@Test
//	void testSaveZone_WhenRepositoryFails() {
//		ZoneMasterDTO zoneMasterDTO = new ZoneMasterDTO();
//		zoneMasterDTO.setZoneName("Zone 1");
//
//		when(zoneMasterRepository.save(any(ZoneMaster.class))).thenThrow(new RuntimeException("DB error"));
//
//		Exception exception = assertThrows(RuntimeException.class, () -> {
//			service.saveZone(zoneMasterDTO);
//		});
//
//		assertEquals("DB error", exception.getMessage());
//		verify(zoneMasterRepository, times(1)).save(any(ZoneMaster.class));
//	}
//
//	private MultipartFile createValidExcelFile() throws IOException {
//		XSSFWorkbook workbook = new XSSFWorkbook();
//		var sheet = workbook.createSheet("Sheet1");
//
//		var header = sheet.createRow(0);
//		header.createCell(0).setCellValue("Zone");
//		header.createCell(1).setCellValue("State");
//		header.createCell(2).setCellValue("City");
//
//		var dataRow = sheet.createRow(1);
//		dataRow.createCell(0).setCellValue("North Zone");
//		dataRow.createCell(1).setCellValue("Haryana");
//		dataRow.createCell(2).setCellValue("Gurgaon");
//
//		var out = new java.io.ByteArrayOutputStream();
//		workbook.write(out);
//		workbook.close();
//
//		return new MockMultipartFile("file", "zones.xlsx",
//				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
//				new ByteArrayInputStream(out.toByteArray()));
//	}
//
//	// Positive Test Case
//	@Test
//	public void testProcessExcelFileForZoneStateCityMapping_Success() throws IOException {
//		MultipartFile mockFile = createValidExcelFile();
//
//		when(zoneMasterRepository.save(any(ZoneMaster.class))).thenAnswer(invocation -> {
//			ZoneMaster zone = invocation.getArgument(0);
//			zone.setZoneId(1L);
//			return zone;
//		});
//
//		when(stateMasterRepository.save(any(StateMaster.class))).thenAnswer(invocation -> {
//			StateMaster state = invocation.getArgument(0);
//			state.setStateId(1L);
//			return state;
//		});
//
//		when(cityMasterRepository.save(any(CityMaster.class))).thenAnswer(invocation -> {
//			CityMaster city = invocation.getArgument(0);
//			city.setCityId(1L);
//			return city;
//		});
//
//		String response = service.processExcelFileForZoneStateCityMapping(mockFile);
//		assert response.equals("File processed successfully!");
//	}
//
//	// Negative Test Case: Unsupported Format
//	@Test
//	public void testProcessExcelFileForZoneStateCityMapping_UnsupportedFormat() throws IOException {
//		MultipartFile mockFile = new MockMultipartFile("file", "zones.txt", "text/plain", "Invalid Content".getBytes());
//		String response = service.processExcelFileForZoneStateCityMapping(mockFile);
//		assert response.equals("Unsupported file format!");
//	}
//
//	// Negative Test Case: Exception in processing
//	@Test
//	public void testProcessExcelFileForZoneStateCityMapping_ExceptionInRowProcessing() throws IOException {
//		MultipartFile mockFile = createValidExcelFile();
//
//		when(zoneMasterRepository.save(any(ZoneMaster.class))).thenThrow(new RuntimeException("Database error"));
//
//		String response = service.processExcelFileForZoneStateCityMapping(mockFile);
//		assert response.equals("File processed successfully!");
//	}
//
//	// Utility method to create an Excel file in memory
//	private InputStream createValidExcelFileName() throws IOException {
//		Workbook workbook = new XSSFWorkbook();
//		Sheet sheet = workbook.createSheet("Test Sheet");
//
//		// Create a row and fill in some sample data
//		Row row = sheet.createRow(0);
//		row.createCell(0).setCellValue("username");
//		row.createCell(1).setCellValue("zoneName");
//		row.createCell(2).setCellValue("stateName");
//		row.createCell(3).setCellValue("cityName");
//
//		// Create another row with sample data
//		row = sheet.createRow(1);
//		row.createCell(0).setCellValue("user1");
//		row.createCell(1).setCellValue("zone1");
//		row.createCell(2).setCellValue("state1");
//		row.createCell(3).setCellValue("city1");
//
//		// Write to a ByteArrayOutputStream and return the InputStream
//		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//		workbook.write(byteArrayOutputStream);
//		workbook.close();
//
//		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
//	}
//
//	@Test
//	void testProcessUserLocationExcel_Success() throws IOException, InterruptedException {
//		// Given
//		String fileName = "validFile.xlsx";
//		InputStream validExcelInputStream = createValidExcelFileName(); // Create a valid Excel InputStream
//		when(mockFile.getOriginalFilename()).thenReturn(fileName);
//		when(mockFile.getInputStream()).thenReturn(validExcelInputStream);
//
//		// Mock repository methods
//		when(userMasterRepository.findUserIdByUsername(anyString())).thenReturn(Optional.of(1L));
//		when(zoneMasterRepository.findZoneIdByName(anyString())).thenReturn(Optional.of(1L));
//		when(stateMasterRepository.findStateIdByName(anyString(), anyLong())).thenReturn(Optional.of(1L));
//		when(cityMasterRepository.findCityIdByName(anyString(), anyLong())).thenReturn(Optional.of(1L));
//
//		// Call the service method
//		String result = service.processUserLocationExcel(mockFile);
//
//		// Then
//		assertEquals("User Location data processed successfully!", result);
//
//		// Verify repository calls
//		verify(userMasterRepository, times(1)).findUserIdByUsername(anyString());
//		verify(zoneMasterRepository, times(1)).findZoneIdByName(anyString());
//		verify(stateMasterRepository, times(1)).findStateIdByName(anyString(), anyLong());
//		verify(cityMasterRepository, times(1)).findCityIdByName(anyString(), anyLong());
//	}
//
//	@Test
//	void testProcessUserLocationExcel_FileNameNull() {
//		when(mockFile.getOriginalFilename()).thenReturn(null);
//
//		// Expect exception due to invalid filename
//		assertThrows(IllegalArgumentException.class, () -> {
//			service.processUserLocationExcel(mockFile);
//		});
//	}
//
//	@Test
//	void testProcessUserLocationExcel_DatabaseError() throws IOException, InterruptedException {
//		// Create a real in-memory Excel file using Apache POI
//		XSSFWorkbook workbook = new XSSFWorkbook();
//		Sheet sheet = workbook.createSheet("Sheet1");
//
//		Row header = sheet.createRow(0);
//		header.createCell(0).setCellValue("username");
//		header.createCell(1).setCellValue("zone");
//		header.createCell(2).setCellValue("state");
//		header.createCell(3).setCellValue("city");
//
//		Row row = sheet.createRow(1);
//		row.createCell(0).setCellValue("mahesh.patil");
//		row.createCell(1).setCellValue("North Zone");
//		row.createCell(2).setCellValue("Haryana");
//		row.createCell(3).setCellValue("Gurgaon");
//
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		workbook.write(out);
//		workbook.close();
//
//		ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
//
//		// Mock file name and stream
//		when(mockFile.getOriginalFilename()).thenReturn("validFile.xlsx");
//		when(mockFile.getInputStream()).thenReturn(inputStream);
//
//		// Mock the repository calls
//		when(userMasterRepository.findUserIdByUsername(anyString())).thenReturn(Optional.of(1L));
//		when(zoneMasterRepository.findZoneIdByName(anyString())).thenReturn(Optional.of(1L));
//		when(stateMasterRepository.findStateIdByName(anyString(), anyLong())).thenReturn(Optional.of(1L));
//		when(cityMasterRepository.findCityIdByName(anyString(), anyLong()))
//				.thenThrow(new RuntimeException("Database error"));
//
//		// Act and Assert
//		RuntimeException ex = assertThrows(RuntimeException.class, () -> {
//			service.processUserLocationExcel(mockFile);
//		});
//
//		assertEquals("Error processing user locations: Database error", ex.getMessage());
//		assertTrue(ex.getCause() instanceof RuntimeException);
//		assertEquals("Database error", ex.getCause().getMessage());
//	}
//	
//	
//
//	@Test
//	void GetEventCodeExcel_Test() throws Exception{
//		
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("EventCodes");
//        
//        Row row = sheet.createRow(1);
//	    row.createCell(1).setCellValue("Supervisory Mode");
//	    row.createCell(2).setCellValue("SUPERVISORY");
//	    row.createCell(3).setCellValue("FLM");
//	    row.createCell(4).setCellValue("No");
//	    row.createCell(5).setCellValue("Complaint");
//	    row.createCell(6).setCellValue("HD;Pending");
//	    row.createCell(7).setCellValue("Supervisor");
//	    row.createCell(8).setCellValue(1);
//	    row.createCell(9).setCellValue(5);
//	    row.createCell(10).setCellValue(5);
//	    row.createCell(11).setCellValue("CRM");
//	    
//	    ByteArrayOutputStream out = new ByteArrayOutputStream();
//	    workbook.write(out);
//	    workbook.close();
//	    
//	    ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());
//        
//	    when(eventcodeRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
//	    
//	    int inserted = service.uploadEventcodeExcel(input);
//	    
//	    assertEquals(1, inserted);
//	    verify(eventcodeRepository, times(1)).saveAll(anyList());
//	}
//
//}
