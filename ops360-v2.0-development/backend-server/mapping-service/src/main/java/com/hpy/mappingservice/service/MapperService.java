package com.hpy.mappingservice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.mappingservice.entity.AllAssignedAtms;
import com.hpy.mappingservice.entity.AllCeEntity;
import com.hpy.mappingservice.entity.AllCmEntity;
import com.hpy.mappingservice.entity.AllUnassignedAtms;
import com.hpy.mappingservice.entity.AllUnassignedCe;
import com.hpy.mappingservice.entity.SubmitMappingReponse;
import com.hpy.mappingservice.repository.AllAssignedAtmsRepository;
import com.hpy.mappingservice.repository.AllAssignedCeRepository;
import com.hpy.mappingservice.repository.AllCeRepository;
import com.hpy.mappingservice.repository.AllCmListRepository;
import com.hpy.mappingservice.repository.AllUnassignedAtmsRepository;
import com.hpy.mappingservice.repository.AllUnassignedCeRepository;
import com.hpy.mappingservice.repository.SubmitMappingReponseRepository;
import com.hpy.mappingservice.request.dto.Base64FileRequestDto;
import com.hpy.mappingservice.request.dto.MappingRecordReq;
import com.hpy.mappingservice.request.dto.SubmitAtmCeMappingDto;
import com.hpy.mappingservice.response.dto.AllAssignedAtmsDto;
import com.hpy.mappingservice.response.dto.AllAssignedCeDto;
import com.hpy.mappingservice.response.dto.AllCeListDto;
import com.hpy.mappingservice.response.dto.AllCmListDto;
import com.hpy.mappingservice.response.dto.AllUnassignedAtmsDto;
import com.hpy.mappingservice.response.dto.AllUnassignedCeDto;
import com.hpy.mappingservice.response.dto.AtmDto;
import com.hpy.mappingservice.response.dto.AtmStatusDto;
import com.hpy.mappingservice.response.dto.CeDto;
import com.hpy.mappingservice.response.dto.CeStatusDto;
import com.hpy.mappingservice.response.dto.CmDto;
import com.hpy.mappingservice.response.dto.CmStatusDto;
import com.hpy.mappingservice.response.dto.ExcelReponseWithStatus;
import com.hpy.mappingservice.response.dto.MessageResponseDto;
import com.hpy.mappingservice.response.dto.ScmDto;
import com.hpy.mappingservice.response.dto.ScmStatusDto;
import com.hpy.mappingservice.response.dto.ZHeadStatusDto;
import com.hpy.mappingservice.response.dto.ZonalHeadDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MapperService {
	
	private AllCeRepository allCeRepository;
	
	private AllUnassignedAtmsRepository allUnassignedAtmsRepository;
	
	private AllAssignedAtmsRepository allAssignedAtmsRepository;
	
	private AllCmListRepository allCmListRepository;
	
	private AllUnassignedCeRepository allUnassignedCeRepository;
	
	private AllAssignedCeRepository allAssignedCeRepository;
	
	private MasterService atmMasterService;
	
	private SubmitMappingReponseRepository submitMappingReponseRepository;
	
	@Value("${mapping.batch-size}")
	private int chunkSize;
	
//	private AtmMasterRepository atmMasterRepository;
	
	public MapperService(AllCeRepository allCeRepository, AllUnassignedAtmsRepository allUnassignedAtmsRepository,
			AllAssignedAtmsRepository allAssignedAtmsRepository, AllCmListRepository allCmListRepository,
			AllUnassignedCeRepository allUnassignedCeRepository, AllAssignedCeRepository allAssignedCeRepository,
			MasterService atmMasterService,SubmitMappingReponseRepository submitMappingReponseRepository) {
		super();
		this.allCeRepository = allCeRepository;
		this.allUnassignedAtmsRepository = allUnassignedAtmsRepository;
		this.allAssignedAtmsRepository = allAssignedAtmsRepository;
		this.allCmListRepository = allCmListRepository;
		this.allUnassignedCeRepository = allUnassignedCeRepository;
		this.allAssignedCeRepository = allAssignedCeRepository;
		this.atmMasterService = atmMasterService;
		this.submitMappingReponseRepository = submitMappingReponseRepository;
	}



	public List<AllCeListDto> getAllCeList() {
		List<AllCeEntity> allCeList =allCeRepository.getAllCeListFromSp();
		return allCeList.stream().map(ce -> new AllCeListDto(ce.getSrno(),ce.getUsername(),ce.getDisplayName(),ce.getUserStatus())).toList();
	}

	public List<AllUnassignedAtmsDto> getAllCeUnassignedAtmList() {
		List<AllUnassignedAtms> list =allUnassignedAtmsRepository.getAllCeUnassignedAtmList();
		return list.stream().map(atm -> new AllUnassignedAtmsDto(atm.getSrno(),atm.getAtmId()==null?"":atm.getAtmId())).toList();
	}
	
	public List<AllAssignedAtmsDto> getAllAssignedAtmList(String username)
	{
		List<AllAssignedAtms> list=allAssignedAtmsRepository.getAllAssignedAtms(username);
		return list.stream().map(atm -> new AllAssignedAtmsDto(atm.getSrno(),atm.getAtmId())).toList();
	}

	public String updateAtmCeMapping(String username, String assignedAtms, String unAssignedAtms) {
		log.info("username :{}",username);
		log.info("assignedAtms :{}",assignedAtms);
		log.info("unAssignedAtms :{}",unAssignedAtms);
		int result = allCeRepository.updateAtmCeMapping(username,assignedAtms,unAssignedAtms);
		return result==1?"ATM CE Mapping Updated":"Error while updating mapping";
	}

	public List<AllCmListDto> getAllCmList() {
		List<AllCmEntity> list=allCmListRepository.getAllCmList();
		return list.stream().map(cm -> new AllCmListDto(cm.getSrno(),cm.getUsername(),cm.getDisplayName())).toList();
	}

	public List<AllUnassignedCeDto> getAllUnassignedCeList() {
		List<AllUnassignedCe> list =allUnassignedCeRepository.getAllUnassignedCe();
		return list.stream().map(ce -> new AllUnassignedCeDto(ce.getSrno(),ce.getUsername(),ce.getDisplayName())).toList();
	}

	public List<AllAssignedCeDto> getAllAssignedCeList(String username) {
		List<AllUnassignedCe> list=allAssignedCeRepository.getAllAssignedCe(username);
		return list.stream().map(ce -> new AllAssignedCeDto(ce.getSrno(),ce.getUsername(),ce.getDisplayName())).toList();
	}
	
	public String updateCeCmMapping(String username, String assignedCes, String unAssignedCms) {
		log.info("username :{}",username);
		log.info("assignedCes :{}",assignedCes);
		log.info("unAssignedCms :{}",unAssignedCms);
		int result = allCmListRepository.updateCeCmMapping(username,assignedCes,unAssignedCms);
		return result==1?"CE -> CM Mapping Updated":"Error while updating mapping";
	}
	
	public ExcelReponseWithStatus convertBase64StringToMultipart(Base64FileRequestDto requestDto) throws IOException {
		log.info("convertBase64StringToMultipart() method started");
		long startconvertBase64StringTime = System.currentTimeMillis();
		byte[] decodedBytes = Base64.getDecoder().decode(requestDto.getBase64FileString());
        MultipartFile multipartFile = new MockMultipartFile("file", requestDto.getFileName(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", decodedBytes);
        log.info("convertBase64StringToMultipart() method ended");
        log.info("Time for execution of convertBase64StringToMultipart() : {} seconds",
				(System.currentTimeMillis() - startconvertBase64StringTime) / 1000);
        return verifyMappingExcel(multipartFile);
	}
	
	public ExcelReponseWithStatus verifyMappingExcel(MultipartFile file) throws IOException {
		long startverifyMappingExcelTime = System.currentTimeMillis();
		log.info("verifyMappingExcel() method started");
	    // Read the Excel file and get column data
		long startcolumnDataMapTime = System.currentTimeMillis();
	    Map<String, List<String>> columnDataMap = readColumnData(file);
	    log.info("Time for execution of readColumnData() : {} seconds",
				(System.currentTimeMillis() - startcolumnDataMapTime) / 1000);

	    // Prepare result list
	    List<AtmDto> atmDtoList = new ArrayList<>();

	    // Perform verification for each record
	    List<String> psIds = columnDataMap.getOrDefault("PS ID", new ArrayList<>());
	    List<String> atmIds = columnDataMap.getOrDefault("atmid_current", new ArrayList<>());
	    List<String> ceIds = columnDataMap.getOrDefault("CE Name-ID", new ArrayList<>());
	    List<String> cmIds = columnDataMap.getOrDefault("CM Name-ID", new ArrayList<>());
	    List<String> scmIds = columnDataMap.getOrDefault("SCM Name-ID", new ArrayList<>());
	    List<String> rcmIds = columnDataMap.getOrDefault("Zonal Head- ID", new ArrayList<>());
	    List<String> zones = columnDataMap.getOrDefault("Zone", new ArrayList<>());
	    
	 // Prepare result list
	    ExecutorService executorService = Executors.newFixedThreadPool(20); // Adjust the pool size as needed
	    List<Future<List<AtmDto>>> futures = new ArrayList<>();
	    
//	    int chunkSize = 1000; // Adjust chunk size based on performance
	    long startbatchProcessingTime = System.currentTimeMillis();
	    for (int start = 0; start < atmIds.size(); start += chunkSize) {
	        int end = Math.min(start + chunkSize, atmIds.size());
	        log.info("start idx: {}",start);
	        log.info("end idx: {}",end);
	        List<String> psIdChunk = psIds.subList(start, end);
	        List<String> atmIdChunk = atmIds.subList(start, end);
	        List<String> ceIdChunk = ceIds.subList(start, end);
	        List<String> cmIdChunk = cmIds.subList(start, end);
	        List<String> scmIdChunk = scmIds.subList(start, end);
	        List<String> rcmIdChunk = rcmIds.subList(start, end);
	        List<String> zoneChunk= zones.subList(start, end);

	        // Submit task for processing a chunk
	        futures.add(executorService.submit(() -> batchProcessing(psIdChunk, atmIdChunk, ceIdChunk, cmIdChunk, scmIdChunk, rcmIdChunk, zoneChunk)));
	    }
	    log.info("Time for execution of batchProcessing() : {} seconds",
				(System.currentTimeMillis() - startbatchProcessingTime) / 1000);

	 // Aggregate results
	    for (Future<List<AtmDto>> future : futures) {
	        try {
	            atmDtoList.addAll(future.get());
	        } catch (InterruptedException | ExecutionException e) {
	        	log.error("Exception occurred during batch processing: {}", e.getMessage());
	            throw new RuntimeException("Error processing Excel data", e);
	        }
	    }

	    executorService.shutdown();
	    
	    
	    
	    // Iterate through records (assuming all lists have the same size)
	    //batchProcessing(atmDtoList, psIds, atmIds, ceIds, cmIds, scmIds, rcmIds, zones);
	    log.info("Time for execution of verifyMappingExcel() : {} seconds",
				(System.currentTimeMillis() - startverifyMappingExcelTime) / 1000);
	    log.info("verifyMappingExcel() method ended");

	    return new ExcelReponseWithStatus(atmDtoList);
	}



	/**
	 * @param psIds
	 * @param atmIds
	 * @param ceIds
	 * @param cmIds
	 * @param scmIds
	 * @param rcmIds
	 * @param zones
	 */
	private List<AtmDto> batchProcessing(List<String> psIds, List<String> atmIds, List<String> ceIds,
			List<String> cmIds, List<String> scmIds, List<String> rcmIds, List<String> zones) {
		List<AtmDto> atmDtoList = new ArrayList<>();
		for (int i = 0; i < atmIds.size(); i++) {
	    	String psId = atmIds.size() > i ? psIds.get(i) : "";
	        String atmId = atmIds.size() > i ? atmIds.get(i) : "";
	        String ceId = ceIds.size() > i ? ceIds.get(i) : "";
	        String cmId = cmIds.size() > i ? cmIds.get(i) : "";
	        String scmId = scmIds.size() > i ? scmIds.get(i) : "";
	        String rcmId = rcmIds.size() > i ? rcmIds.get(i) : "";
	        String zone = atmIds.size() > i ? zones.get(i) : "";

	        // Verify each ID and create DTOs
	        AtmDto atmDto = new AtmDto();
	        atmDto.setAtmId(atmId);
	        atmDto.setStatus(atmMasterService.findAtmById(atmId).getStatus());
	        atmDto.setPsId(psId);

	        CeDto ceDto = new CeDto();
	        ceDto.setUsername(ceId);
	        ceDto.setStatus(atmMasterService.findCeByUsername(ceId).getStatus());

	        CmDto cmDto = new CmDto();
	        cmDto.setUsername(cmId);
	        cmDto.setStatus(atmMasterService.findCmByUsername(cmId).getStatus());

	        ScmDto scmDto = new ScmDto();
	        scmDto.setUsername(scmId);
	        scmDto.setStatus(atmMasterService.findScmByUsername(scmId).getStatus());

	        ZonalHeadDto zonalHeadDto = new ZonalHeadDto();
	        zonalHeadDto.setUsername(rcmId);
	        zonalHeadDto.setStatus(atmMasterService.findZHeadByUsername(rcmId).getStatus());
	        zonalHeadDto.setZone(zone);

	        // Set nested DTOs
	        scmDto.setZonalHeadDto(zonalHeadDto);
	        cmDto.setScmDto(scmDto);
	        ceDto.setCmDto(cmDto);
	        atmDto.setCeDto(ceDto);

	        // Add to result list
	        atmDtoList.add(atmDto);
	    }
		return atmDtoList;
	}
	
	public Workbook getWorkbook(MultipartFile file) throws IOException {
	    String fileType = file.getContentType();
	    
	    if (fileType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
	        return new XSSFWorkbook(file.getInputStream());
	    } else {
	        throw new IllegalArgumentException("Unsupported file type: " + fileType);
	    }
	}
	
	private Map<String, List<String>> readColumnData(MultipartFile file) throws IOException {
	    Map<String, List<String>> columnDataMap = new LinkedHashMap<>(); // Maintain column order with LinkedHashMap
	    
	    try (Workbook workbook = getWorkbook(file)) {
	        Sheet sheet = workbook.getSheetAt(0);

	        // Ensure the sheet is not empty
	        if (sheet == null || sheet.getPhysicalNumberOfRows() == 0) {
	            throw new IllegalArgumentException("The Excel file is empty or invalid.");
	        }

	        // Get the header row to determine column names
	        Row headerRow = sheet.getRow(0);
	        int numberOfColumns = headerRow.getPhysicalNumberOfCells();

	        // Initialize a list for each column based on header names
	        for (int colIndex = 0; colIndex < numberOfColumns; colIndex++) {
	            Cell headerCell = headerRow.getCell(colIndex);
	            String columnName = (headerCell == null) ? "Column" + (colIndex + 1) : headerCell.toString().trim();
	            columnDataMap.put(columnName, new ArrayList<>());
	        }

	        // Iterate through the rows, skipping the header row
	        for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
	            Row row = sheet.getRow(rowIndex);
	            if (row == null) continue;

	            for (int colIndex = 0; colIndex < numberOfColumns; colIndex++) {
	                Cell cell = row.getCell(colIndex);
	                String cellValue = (cell == null) ? "" : cell.toString().trim(); // Handle null cells
	                
	                // Add value to the corresponding column
	                String columnName = headerRow.getCell(colIndex).toString().trim();
	                columnDataMap.get(columnName).add(cellValue);
	            }
	        }
	    }

	    return columnDataMap;
	}



	public MessageResponseDto verifyEachRow(MappingRecordReq req) {

	    StringBuilder messageBuilder = new StringBuilder();

	    // Check ATM status
	    if (req.getAtmId() == null || req.getAtmId().trim().isEmpty()) {
	        messageBuilder.append("ATM ID is empty, ");
	    } else {
	        AtmStatusDto atmStatus = atmMasterService.findAtmById(req.getAtmId());
	        if (atmStatus == null || atmStatus.getStatus() == 0) {
	            messageBuilder.append("ATM ID does not exist, ");
	        }
	    }

	    // Check CE status
	    if (req.getCeUser() == null || req.getCeUser().trim().isEmpty()) {
	        messageBuilder.append("CE User is empty, ");
	    } else {
	        CeStatusDto ceStatus = atmMasterService.findCeByUsername(req.getCeUser());
	        if (ceStatus == null || ceStatus.getStatus() == 0) {
	            messageBuilder.append("CE User does not exist, ");
	        }
	    }

	    // Check CM status
	    if (req.getCmUser() == null || req.getCmUser().trim().isEmpty()) {
	        messageBuilder.append("CM User is empty, ");
	    } else {
	        CmStatusDto cmStatus = atmMasterService.findCmByUsername(req.getCmUser());
	        if (cmStatus == null || cmStatus.getStatus() == 0) {
	            messageBuilder.append("CM User does not exist, ");
	        }
	    }

	    // Check SCM status
	    if (req.getScmUser() == null || req.getScmUser().trim().isEmpty()) {
	        messageBuilder.append("SCM User is empty, ");
	    } else {
	        ScmStatusDto scmStatus = atmMasterService.findScmByUsername(req.getScmUser());
	        if (scmStatus == null || scmStatus.getStatus() == 0) {
	            messageBuilder.append("SCM User does not exist, ");
	        }
	    }

	    // Check ZHead status
	    if (req.getRcmUser() == null || req.getRcmUser().trim().isEmpty()) {
	        messageBuilder.append("ZHead User is empty, ");
	    } else {
	        ZHeadStatusDto zHeadStatus = atmMasterService.findZHeadByUsername(req.getRcmUser());
	        if (zHeadStatus == null || zHeadStatus.getStatus() == 0) {
	            messageBuilder.append("ZHead User does not exist, ");
	        }
	    }

	    // Remove the trailing comma and space
	    String finalMessage = messageBuilder.toString().trim();
	    if (finalMessage.endsWith(",")) {
	        finalMessage = finalMessage.substring(0, finalMessage.length() - 1);
	    }

	    // Return the final message in MessageResponseDto
	    MessageResponseDto response = new MessageResponseDto();
	    response.setMessage(finalMessage.isEmpty() ? "All records are valid" : finalMessage);
	    return response;
	}



	public MessageResponseDto submitAtmCeMapping(List<SubmitAtmCeMappingDto> atmMappings) {
		SubmitMappingReponse response = null;
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String jsonListString = mapper.writeValueAsString(atmMappings);
			response = submitMappingReponseRepository.submitAtmCeMappingList(jsonListString);
		} catch (JsonProcessingException e) {
			log.error("Failed to map list of object: {}",e);
			return new MessageResponseDto("Failed to process the request");
		}
		
		return new MessageResponseDto(response.getDataSaved()+" rows affected");
	}



	public MessageResponseDto submitAtmCeMappingDataFromExcel(MultipartFile file) {
	    List<SubmitAtmCeMappingDto> atmCeMappingList = new ArrayList<>();

	    try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
	        Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
	        Iterator<Row> rowIterator = sheet.iterator();

	        // Skip the header row
	        if (rowIterator.hasNext()) {
	            rowIterator.next();
	        }

	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();

	            // Check if the row is blank
	            if (isRowBlank(row)) {
	                continue;
	            }

	            SubmitAtmCeMappingDto dto = new SubmitAtmCeMappingDto();

	            dto.setPsId(getCellValue(row.getCell(0)));
	            dto.setAtmId(getCellValue(row.getCell(1)));
	            dto.setCeUserId(getCellValue(row.getCell(2)));
	            dto.setCmUserId(getCellValue(row.getCell(3)));
	            dto.setScmUserId(getCellValue(row.getCell(4)));
	            dto.setZcmUserId(getCellValue(row.getCell(5)));
	            dto.setZone(getCellValue(row.getCell(6)));

	            atmCeMappingList.add(dto);
	        }

	    } catch (IOException e) {
	        System.err.println("Error reading Excel file: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return submitAtmCeMapping(atmCeMappingList);
	}

	private boolean isRowBlank(Row row) {
	    for (int i = 0; i < row.getLastCellNum(); i++) {
	        Cell cell = row.getCell(i);
	        if (cell != null && cell.getCellType() != CellType.BLANK) {
	            return false;
	        }
	    }
	    return true;
	}

	private String getCellValue(Cell cell) {
	    if (cell == null) {
	        return null;
	    }

	    switch (cell.getCellType()) {
	        case STRING:
	            return cell.getStringCellValue();
	        case NUMERIC:
	            if (DateUtil.isCellDateFormatted(cell)) {
	                return cell.getDateCellValue().toString();
	            } else {
	                return String.valueOf((int) cell.getNumericCellValue());
	            }
	        case BOOLEAN:
	            return String.valueOf(cell.getBooleanCellValue());
	        case FORMULA:
	            return cell.getCellFormula();
	        default:
	            return null;
	    }
	}


	

}
