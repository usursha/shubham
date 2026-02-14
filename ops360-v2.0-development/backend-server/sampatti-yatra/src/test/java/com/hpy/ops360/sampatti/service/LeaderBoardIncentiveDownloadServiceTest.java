package com.hpy.ops360.sampatti.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.sampatti.dto.response.GenericResponseDto;
import com.hpy.ops360.sampatti.entity.LeaderBoardIncentiveDownloadEntity;
import com.hpy.ops360.sampatti.repository.LeaderBoardIncentiveDownloadRepo;
import com.hpy.ops360.sampatti.util.CMInsentiveCSVGenerator;
import com.hpy.ops360.sampatti.util.CMInsentiveExcelGenerator;

@ExtendWith(MockitoExtension.class)
public class LeaderBoardIncentiveDownloadServiceTest {

	 @InjectMocks
	    private LeaderBoardIncentiveDownloadService service;

	    @Mock
	    private CMInsentiveExcelGenerator excelGenerator;

	    @Mock
	    private CMInsentiveCSVGenerator csvGenerator;

	    @Mock
	    private LeaderBoardIncentiveDownloadRepo repo;

	    private List<LeaderBoardIncentiveDownloadEntity> mockData;

	    @BeforeEach
	    public void setup() {
	        MockitoAnnotations.openMocks(this);
	        mockData = List.of(
	                new LeaderBoardIncentiveDownloadEntity(1, "Santosh Kumar Singh", "CE", 109, 121, 12, 15000, "sameer.zaidi", "rohit.gupta", "gulshan.srivastav"),
	                new LeaderBoardIncentiveDownloadEntity(1, "Smruti Ranjan Rout", "CE", 72, 94, 22, 15000, "ajit.das", "surajit.biswas", "soumesh.majumdar")
	        );
	    }

	    @Test
	    public void testGetDataExcel_Positive() throws IOException {
	        when(repo.getCmIncentiveMonthlyData("Feb 2025", "CE")).thenReturn(mockData);

	        XSSFWorkbook mockWorkbook = new XSSFWorkbook();
	        when(excelGenerator.generateExcelCE(any())).thenReturn(mockWorkbook);

	        GenericResponseDto response = service.getDataExcel("Feb 2025", "CE");

	        assertNotNull(response);
	        assertNotNull(response.getMessage());
	        assertTrue(Base64.getDecoder().decode(response.getMessage()).length > 0);
	    }


	    @Test
	    public void testGetDataExcel_Negative_NullWorkbook() {
	        when(repo.getCmIncentiveMonthlyData("Feb 2025", "XYZ")).thenReturn(mockData);

	        GenericResponseDto response = service.getDataExcel("Feb 2025", "XYZ");

	        assertNotNull(response);
	        assertEquals("Error generating CM incentive Excel file", response.getMessage());
	    }
	    
	    @Test
	    public void testGetDataCSV_Positive() throws IOException {
	        when(repo.getCmIncentiveMonthlyData("Feb 2025", "CE")).thenReturn(mockData);
	        when(csvGenerator.generateCSVCE(any())).thenReturn(Base64.getEncoder().encodeToString("test,csv,data".getBytes()));

	        GenericResponseDto response = service.getDataCSV("Feb 2025", "CE");

	        assertNotNull(response);
	        assertEquals(Base64.getEncoder().encodeToString("test,csv,data".getBytes()), response.getMessage());
	    }

//	    @Test
//	    public void testGetDataCSV_Negative_IOException() throws IOException { 
//	        when(repo.getCmIncentiveMonthlyData("Feb 2025", "CE")).thenReturn(mockData);
//	        when(csvGenerator.generateCSVCE(any())).thenThrow(new IOException("File generation failed"));
//
//	        GenericResponseDto response = service.getDataCSV("Feb 2025", "CE");
//
//	        assertNotNull(response);
//	        assertEquals("Error generating CM incentive CSV file", response.getMessage());
//	    }
}
