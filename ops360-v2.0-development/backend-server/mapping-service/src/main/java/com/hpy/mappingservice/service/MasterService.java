package com.hpy.mappingservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hpy.mappingservice.entity.AtmEntity;
import com.hpy.mappingservice.entity.AtmMaster;
import com.hpy.mappingservice.entity.CeEntity;
import com.hpy.mappingservice.entity.CityMaster;
import com.hpy.mappingservice.entity.CmEntity;
import com.hpy.mappingservice.entity.EventCode;
import com.hpy.mappingservice.entity.RcmZonalHeadEntity;
import com.hpy.mappingservice.entity.ScmEntity;
import com.hpy.mappingservice.entity.StateMaster;
import com.hpy.mappingservice.entity.UserLocationHandling;
import com.hpy.mappingservice.entity.UserMaster;
import com.hpy.mappingservice.entity.ZoneMaster;
import com.hpy.mappingservice.repository.AtmMasterRepository;
import com.hpy.mappingservice.repository.AtmRepository;
import com.hpy.mappingservice.repository.CeRepository;
import com.hpy.mappingservice.repository.CityMasterRepository;
import com.hpy.mappingservice.repository.CmRepository;
import com.hpy.mappingservice.repository.EventCodeRepository;
import com.hpy.mappingservice.repository.ScmRepository;
import com.hpy.mappingservice.repository.StateMasterRepository;
import com.hpy.mappingservice.repository.UserLocationHandlingRepository;
import com.hpy.mappingservice.repository.UserMasterRepository;
import com.hpy.mappingservice.repository.ZoneMasterRepository;
import com.hpy.mappingservice.repository.ZonelHeadRepository;
import com.hpy.mappingservice.response.dto.AtmMasterDto;
import com.hpy.mappingservice.response.dto.AtmStatusDto;
import com.hpy.mappingservice.response.dto.CeStatusDto;
import com.hpy.mappingservice.response.dto.CityMasterDTO;
import com.hpy.mappingservice.response.dto.CmStatusDto;
import com.hpy.mappingservice.response.dto.ScmStatusDto;
import com.hpy.mappingservice.response.dto.StateMasterDTO;
import com.hpy.mappingservice.response.dto.UserMasterDto;
import com.hpy.mappingservice.response.dto.ZHeadStatusDto;
import com.hpy.mappingservice.response.dto.ZoneMasterDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MasterService {
	
	private AtmMasterRepository repository;
	
	private AtmRepository atmRepository;
	
	private CeRepository ceRepository;
	
	private CmRepository cmRepository;
	
	private ScmRepository scmRepository;
	
	private ZonelHeadRepository zoneHeadRepository;
	
    private UserMasterRepository userMasterRepository;
    
    private ZoneMasterRepository zoneMasterRepository;
    
    private StateMasterRepository stateMasterRepository;
    
    private CityMasterRepository cityMasterRepository;
    
    private UserLocationHandlingRepository userLocationHandlingRepository;
    
    private EventCodeRepository eventcodeRepository;
    
    private AtmMasterCustomRepositoryImpl atmMasterCustomRepositoryImpl;
    
    private UserMasterCustomRepositoryImpl userMasterCustomRepositoryImpl;
	
	
	public MasterService(AtmMasterRepository repository, AtmRepository atmRepository, CeRepository ceRepository,
			CmRepository cmRepository, ScmRepository scmRepository, ZonelHeadRepository zoneHeadRepository,
			UserMasterRepository userMasterRepository, ZoneMasterRepository zoneMasterRepository,
			StateMasterRepository stateMasterRepository, CityMasterRepository cityMasterRepository,
			UserLocationHandlingRepository userLocationHandlingRepository,EventCodeRepository eventcodeRepository,AtmMasterCustomRepositoryImpl atmMasterCustomRepositoryImpl,UserMasterCustomRepositoryImpl userMasterCustomRepositoryImpl) {
		super();
		this.repository = repository;
		this.atmRepository = atmRepository;
		this.ceRepository = ceRepository;
		this.cmRepository = cmRepository;
		this.scmRepository = scmRepository;
		this.zoneHeadRepository = zoneHeadRepository;
		this.userMasterRepository = userMasterRepository;
		this.zoneMasterRepository = zoneMasterRepository;
		this.stateMasterRepository = stateMasterRepository;
		this.cityMasterRepository = cityMasterRepository;
		this.userLocationHandlingRepository = userLocationHandlingRepository;
		this.eventcodeRepository= eventcodeRepository;
		this.atmMasterCustomRepositoryImpl = atmMasterCustomRepositoryImpl;
		this.userMasterCustomRepositoryImpl = userMasterCustomRepositoryImpl;
	}

	@Cacheable(value="rcmIds",key="#zHead")
	public ZHeadStatusDto findZHeadByUsername(String zHead) {
		log.info("Fetching from database rcm...");
		RcmZonalHeadEntity user=zoneHeadRepository.findRcmById(zHead);
		return new ZHeadStatusDto(user.getRcmUserId(),user.getStatus());
	}

	@Cacheable(value="scmIds",key="#scm")
	public ScmStatusDto findScmByUsername(String scm) {
		log.info("Fetching from database scm...");
		ScmEntity user=scmRepository.findScmById(scm);
		return new ScmStatusDto(user.getScmUserId(),user.getStatus());
		
	}

	@Cacheable(value="cmIds",key="#cm")
	public CmStatusDto findCmByUsername(String cm) {
		log.info("Fetching from database cm...");
		CmEntity user=cmRepository.findCmById(cm);
		return new CmStatusDto(user.getCmUserId(),user.getStatus());
		
	}

	@Cacheable(value="ceIds",key="#ce")
	public CeStatusDto findCeByUsername(String ce) {
		log.info("Fetching from database ce...");
		CeEntity user=ceRepository.findCeById(ce);
		return new CeStatusDto(user.getCeUserId(),user.getStatus());
		
	}

	@Cacheable(value="atmIds",key="#atmId")
	public AtmStatusDto findAtmById(String atmId)
	{
		log.info("Fetching from database atmId...");
		AtmEntity atm = atmRepository.findAtmById(atmId);
		return new AtmStatusDto(atm.getAtmId(),atm.getStatus());
	}
	
	public List<AtmMasterDto> findAll() {
		List<AtmMaster> atmMasterList=repository.findAll();
		if (atmMasterList.isEmpty()) {
			return Collections.emptyList();
		}
        return atmMasterList.stream().map(this::convertToDto).toList();
    }
	
	private AtmMasterDto convertToDto(AtmMaster atmMaster) {
        return AtmMasterDto.builder().atmId(atmMaster.getId()).atmCode(atmMaster.getAtmCode()).bankName(atmMaster.getBankName()).grade(atmMaster.getGrade()).address(atmMaster.getAddress()).city(atmMaster.getCity()).state(atmMaster.getState()).build();
    }

    public AtmMasterDto findById(Long id) {
    	Optional<AtmMaster> optionalAtm=repository.findById(id);
    	if (optionalAtm.isPresent()) {
    		AtmMaster atmMaster=optionalAtm.get();
			return AtmMasterDto.builder().atmId(atmMaster.getId()).atmCode(atmMaster.getAtmCode()).bankName(atmMaster.getBankName()).grade(atmMaster.getGrade()).address(atmMaster.getAddress()).city(atmMaster.getCity()).state(atmMaster.getState()).build();
		}
        return new AtmMasterDto(0L, "", "", "", "", "", "","", "");
    }

    public AtmMasterDto save(AtmMasterDto atmMaster) {
    	
    	AtmMaster savedAtm=repository.save(new AtmMaster(atmMaster.getAtmCode(), atmMaster.getBankName(), atmMaster.getGrade(), atmMaster.getCity(), atmMaster.getState(), atmMaster.getAddress(),atmMaster.getZone(), atmMaster.getSource()));
    	
    	return AtmMasterDto.builder().atmId(savedAtm.getId()).atmCode(savedAtm.getAtmCode()).bankName(savedAtm.getBankName()).grade(savedAtm.getGrade()).address(savedAtm.getAddress()).city(savedAtm.getCity()).state(savedAtm.getState()).zone(savedAtm.getZone()).build();
		
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    public String save(MultipartFile file) throws InterruptedException {
        try {
            List<AtmMaster> atmMasters = excelToAtmMasters(file.getInputStream());
            ExecutorService executorService = Executors.newFixedThreadPool(20);
            int batchSize=1000;
            for (int i = 0; i < atmMasters.size(); i += batchSize) {
            	int start = i;
                int end = Math.min(i + batchSize, atmMasters.size());
                List<AtmMaster> batch = atmMasters.subList(start, end);
                //executorService.submit(() -> repository.saveAll(batch));
                executorService.submit(() -> atmMasterCustomRepositoryImpl.bulkInsertOrUpdate(batch));
                log.info("batch start:{}",i);
			}
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);
            return "Atm Details From .Xlsx file successfully saved!!!";
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        } catch (InterruptedException e) {
			e.printStackTrace();
			throw new InterruptedException("fail to store excel data: " + e.getMessage());
		}
    }
    
    private List<AtmMaster> excelToAtmMasters(InputStream is) throws IOException {
        try {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            List<AtmMaster> atmMasters = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // skip header row
                }

                AtmMaster atmMaster = new AtmMaster();
                atmMaster.setAtmCode(formatter.formatCellValue(row.getCell(0)));
                atmMaster.setBankName(formatter.formatCellValue(row.getCell(1)));
                atmMaster.setGrade(formatter.formatCellValue(row.getCell(2)));
                atmMaster.setCity(formatter.formatCellValue(row.getCell(3)));
                atmMaster.setState(formatter.formatCellValue(row.getCell(4)));
                atmMaster.setAddress(formatter.formatCellValue(row.getCell(5)));
                atmMaster.setZone(formatter.formatCellValue(row.getCell(6)));
                atmMaster.setSource(formatter.formatCellValue(row.getCell(7)));
                atmMasters.add(atmMaster);
            }

            workbook.close();
            return atmMasters;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
    
    public List<UserMasterDto> getAllUsers() {
        return userMasterRepository.findAll().stream()
                .map(this::convertUserMasterToDTO)
                .toList();
    }

    public Optional<UserMasterDto> getUserById(Long id) {
        return userMasterRepository.findById(id)
                .map(this::convertUserMasterToDTO);
    }

    public UserMasterDto createUser(UserMasterDto userMasterDTO) {
        UserMaster userMaster = convertToEntity(userMasterDTO);
        return convertUserMasterToDTO(userMasterRepository.save(userMaster));
    }

    public UserMasterDto updateUser(Long id, UserMasterDto userMasterDTO) {
        UserMaster userMaster = userMasterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Update fields
        userMaster.setUsername(userMasterDTO.getUsername());
        userMaster.setDesignation(userMasterDTO.getDesignation());
        userMaster.setFullName(userMasterDTO.getFullName());
        userMaster.setMobileno(userMasterDTO.getMobileno());
        userMaster.setEmailId(userMasterDTO.getEmailId());
        userMaster.setEmployeeCode(userMasterDTO.getEmployeeCode());
        userMaster.setCity(userMasterDTO.getCity());
        userMaster.setState(userMasterDTO.getState());
        userMaster.setZone(userMasterDTO.getZone());
        return convertUserMasterToDTO(userMasterRepository.save(userMaster));
    }

    public void deleteUser(Long id) {
        userMasterRepository.deleteById(id);
    }

    private UserMasterDto convertUserMasterToDTO(UserMaster userMaster) {
        UserMasterDto dto = new UserMasterDto();
        dto.setId1(userMaster.getId());
        dto.setUsername(userMaster.getUsername());
        dto.setDesignation(userMaster.getDesignation());
        dto.setFullName(userMaster.getFullName());
        dto.setMobileno(userMaster.getMobileno());
        dto.setEmailId(userMaster.getEmailId());
        dto.setEmployeeCode(userMaster.getEmployeeCode());
        dto.setCity(userMaster.getCity());
        dto.setState(userMaster.getState());
        dto.setZone(userMaster.getZone());
        return dto;
    }

    private UserMaster convertToEntity(UserMasterDto userMasterDTO) {
        UserMaster userMaster = new UserMaster();
        userMaster.setUsername(userMasterDTO.getUsername());
        userMaster.setDesignation(userMasterDTO.getDesignation());
        userMaster.setFullName(userMasterDTO.getFullName());
        userMaster.setMobileno(userMasterDTO.getMobileno());
        userMaster.setEmailId(userMasterDTO.getEmailId());
        userMaster.setEmployeeCode(userMasterDTO.getEmployeeCode());
        userMaster.setCity(userMasterDTO.getCity());
        userMaster.setState(userMasterDTO.getState());
        userMaster.setZone(userMasterDTO.getZone());
        return userMaster;
    }
    
    public List<UserMaster> uploadUserMasterExcel(MultipartFile file) throws Exception {
        List<UserMaster> users = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream(); 
        	     Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Getting the first sheet
            DataFormatter formatter = new DataFormatter();
            for (Row row : sheet) {
                // Skip the header row
                if (row.getRowNum() == 0) {
                    continue;
                }

                UserMaster user = new UserMaster();
                user.setUsername(formatter.formatCellValue(row.getCell(0)) );
                user.setFullName(formatter.formatCellValue(row.getCell(1)));
                user.setDesignation(formatter.formatCellValue(row.getCell(2)));
                user.setMobileno(formatter.formatCellValue(row.getCell(3)));
                user.setEmployeeCode(formatter.formatCellValue(row.getCell(4)));
                user.setCity(formatter.formatCellValue(row.getCell(5)));
                user.setState(formatter.formatCellValue(row.getCell(6)));
                user.setZone(formatter.formatCellValue(row.getCell(7)));
                user.setEmailId(formatter.formatCellValue(row.getCell(8)));
                user.setHomeAddress(formatter.formatCellValue(row.getCell(9)));

                users.add(user);
            }
        }

        return users;
    }
    
    public String saveAllUsers(MultipartFile file) throws Exception {

            List<UserMaster> userMasters = uploadUserMasterExcel(file);
            ExecutorService executorService = Executors.newFixedThreadPool(20);
            int batchSize=1000;
            for (int i = 0; i < userMasters.size(); i += batchSize) {
            	int start = i;
                int end = Math.min(i + batchSize, userMasters.size());
                List<UserMaster> batch = userMasters.subList(start, end);
                //executorService.submit(() -> userMasterRepository.saveAll(batch));
                executorService.submit(() -> userMasterCustomRepositoryImpl.bulkInsertOrUpdate(batch));
			}
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);
            return "Atm Details From .xls file successfully saved!!!";

    }
    
    public ZoneMasterDTO  saveZone(ZoneMasterDTO zoneMasterDTO) {
        ZoneMaster zoneMaster = new ZoneMaster();
        zoneMaster.setZoneName(zoneMasterDTO.getZoneName());
        ZoneMaster savedZone = zoneMasterRepository.save(zoneMaster);
        return convertToZoneMasterDTO(savedZone);
    }

    private ZoneMasterDTO convertToZoneMasterDTO(ZoneMaster zone) {
        ZoneMasterDTO dto = new ZoneMasterDTO();
        dto.setZoneId(zone.getZoneId()); // Assuming zoneId is now used
        dto.setZoneName(zone.getZoneName());
        return dto;
    }
    
    public StateMasterDTO saveState(StateMasterDTO stateMasterDTO) {
        StateMaster stateMaster = new StateMaster();
        stateMaster.setStateName(stateMasterDTO.getStateName());
        
        if (stateMasterDTO.getZoneId() != null) {
            ZoneMaster zone = new ZoneMaster();
            zone.setZoneId(stateMasterDTO.getZoneId()); // Assuming zoneId is now used
            stateMaster.setZoneMaster(zone);
        }

        StateMaster savedState = stateMasterRepository.save(stateMaster);
        return convertToStateMasterDTO(savedState);
    }

    private StateMasterDTO convertToStateMasterDTO(StateMaster state) {
        StateMasterDTO dto = new StateMasterDTO();
        dto.setStateId(state.getStateId());
        dto.setStateName(state.getStateName());
        if (state.getZoneMaster() != null) {
            dto.setZoneId(state.getZoneMaster().getZoneId()); // Assuming zoneId is now used
        }
        return dto;
    }
    
    public CityMasterDTO saveCity(CityMasterDTO cityMasterDTO) {
        CityMaster cityMaster = new CityMaster();
        cityMaster.setCityName(cityMasterDTO.getCityName());
        
        if (cityMasterDTO.getStateId() != null) {
            StateMaster state = new StateMaster();
            state.setStateId(cityMasterDTO.getStateId()); // Assuming stateId is now used
            cityMaster.setStateMaster(state);
        }

        CityMaster savedCity = cityMasterRepository.save(cityMaster);
        return convertToCityMasterDTO(savedCity);
    }

    private CityMasterDTO convertToCityMasterDTO(CityMaster city) {
        CityMasterDTO dto = new CityMasterDTO();
        dto.setCityId(city.getCityId());
        dto.setCityName(city.getCityName());
        if (city.getStateMaster() != null) {
            dto.setStateId(city.getStateMaster().getStateId()); // Assuming stateId is now used
        }
        return dto;
    }
    
    public List<ZoneMasterDTO> getAllZones()
    {
    	return zoneMasterRepository.findAll().stream().map(this::convertToZoneMasterDTO).toList();
    }
    
    public List<StateMasterDTO> getAllStates()
    {
    	return stateMasterRepository.findAll().stream().map(this::convertToStateMasterDTO).toList();
    }
    
    public List<CityMasterDTO> getAllCities()
    {
    	return cityMasterRepository.findAll().stream().map(this::convertToCityMasterDTO).toList();
    }
    
    
    public String processExcelFileForZoneStateCityMapping(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Workbook workbook;
        InputStream inputStream = file.getInputStream();

        // Detect file format based on extension
        if (fileName.endsWith(".xls")) {
            workbook = new HSSFWorkbook(inputStream); // Handles .xls files
        } else if (fileName.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream); // Handles .xlsx files
        } else {
            return "Unsupported file format!";
        }

        Sheet sheet = workbook.getSheetAt(0);
        Map<String, ZoneMaster> zoneMap = new ConcurrentHashMap<>();
        Map<String, StateMaster> stateMap = new ConcurrentHashMap<>();

        // ExecutorService for parallel processing
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<?>> futures = new ArrayList<>();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            Future<?> future = executorService.submit(() -> {
                try {
                    String zoneName = row.getCell(0).getStringCellValue().toLowerCase().trim();
                    String stateName = row.getCell(1).getStringCellValue().toLowerCase().trim();
                    String cityName = row.getCell(2).getStringCellValue().toLowerCase().trim();

                    // Handle Zone
                    ZoneMaster zoneMaster = zoneMap.computeIfAbsent(zoneName, z -> {
                        ZoneMaster newZone = new ZoneMaster();
                        newZone.setZoneName(z);
                        return zoneMasterRepository.save(newZone);
                    });

                    // Handle State
                    StateMaster stateMaster = stateMap.computeIfAbsent(stateName, s -> {
                        StateMaster newState = new StateMaster();
                        newState.setStateName(s);
                        newState.setZoneMaster(zoneMaster);
                        return stateMasterRepository.save(newState);
                    });

                    // Handle City
                    CityMaster cityMaster = new CityMaster();
                    cityMaster.setCityName(cityName);
                    cityMaster.setStateMaster(stateMaster);
                    cityMasterRepository.save(cityMaster);

                } catch (Exception e) {
                    e.printStackTrace(); // Handle exception safely
                }
            });

            futures.add(future);
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            try {
                future.get(); // Ensure completion before closing workbook
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        workbook.close();
        executorService.shutdown();

        return "File processed successfully!";
    }
    
 
    public String processUserLocationExcel(MultipartFile file) throws IOException, InterruptedException {
        // Validate filename
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file does not have a valid name!");
        }

        // Use try-with-resources to ensure Workbook is properly closed
        try (Workbook workbook = fileName.endsWith(".xls")
                ? new HSSFWorkbook(file.getInputStream())
                : new XSSFWorkbook(file.getInputStream())) {

            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            Sheet sheet = workbook.getSheetAt(0);
            List<Future<Void>> futures = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                futures.add(executorService.submit(() -> {
                    String username = row.getCell(0).getStringCellValue().toLowerCase().trim();
                    String zoneName = row.getCell(1).getStringCellValue().toLowerCase().trim();
                    String stateName = row.getCell(2).getStringCellValue().toLowerCase().trim();
                    String cityName = row.getCell(3).getStringCellValue().toLowerCase().trim();

                    // Fetch IDs, allow null if data not found
//                    Long userId = userMasterRepository.findUserIdByUsername(username).orElse(null);
                    Long zoneId = zoneMasterRepository.findZoneIdByName(zoneName).orElse(null);
                    Long stateId = (zoneId != null) ? stateMasterRepository.findStateIdByName(stateName, zoneId).orElse(null) : null;
                    Long cityId = (stateId != null) ? cityMasterRepository.findCityIdByName(cityName, stateId).orElse(null) : null;

                    // Create UserLocationHandling with null values where needed
                    UserLocationHandling userLocationHandling = new UserLocationHandling();
//                    userLocationHandling.setUser(userId != null ? new UserMaster(userId) : null);
                    userLocationHandling.setZoneMaster(zoneId != null ? new ZoneMaster(zoneId) : null);
                    userLocationHandling.setStateMaster(stateId != null ? new StateMaster(stateId) : null);
                    userLocationHandling.setCityMaster(cityId != null ? new CityMaster(cityId) : null);

                    userLocationHandlingRepository.save(userLocationHandling);
                    return null;
                }));
            }

            // Wait for all threads to finish processing
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interrupt status
                    throw new InterruptedException("Thread interrupted while processing user location Excel file.");
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause(); // Get actual cause of error
                    throw new RuntimeException("Error processing user locations: " + cause.getMessage(), cause);
                }
            }

            executorService.shutdown();
            return "User Location data processed successfully!";
        }
    }

    public int uploadEventcodeExcel(InputStream inputStream) throws Exception {
    	int rowsInserted = 0;
    	List<EventCode> batch = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                EventCode code = new EventCode();
                code.setEventcode(row.getCell(1).getStringCellValue());
                code.setCategory(row.getCell(2).getStringCellValue());
                code.setCrmServiceCode(row.getCell(3).getStringCellValue());
                code.setReportDisplay(row.getCell(4).getStringCellValue());
                code.setCallType(row.getCell(5).getStringCellValue());
                code.setSubCallType(row.getCell(6).getStringCellValue());
                code.setEventgroup(row.getCell(7).getStringCellValue());
                code.setIsbreakdown(row.getCell(8).getNumericCellValue());
                code.setInstertTime(LocalDateTime.now());
                code.setPriorityScore((int) row.getCell(10).getNumericCellValue());
                code.setSynergyApplicationSource(row.getCell(11).getStringCellValue());

                batch.add(code);
                rowsInserted++;
            }
            eventcodeRepository.saveAll(batch);
        }
        return rowsInserted;
    }

}
