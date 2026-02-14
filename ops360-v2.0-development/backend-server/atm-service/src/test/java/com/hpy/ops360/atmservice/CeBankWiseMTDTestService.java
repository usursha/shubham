package com.hpy.ops360.atmservice;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.atmservice.dto.CeBankWiseMTDResponseDto;
import com.hpy.ops360.atmservice.entity.CeBankWiseMTDEntity;
import com.hpy.ops360.atmservice.repository.CeBankWiseMTDRepository;
import com.hpy.ops360.atmservice.service.CeBankWiseMTDService;


@ExtendWith(MockitoExtension.class)
	public class CeBankWiseMTDTestService {
	
	@Mock
	private CeBankWiseMTDRepository cebankWiseMTDRepository;
	
	@InjectMocks
	private CeBankWiseMTDService cebankWiseMTDService;
	

	 @Test
	    public void testGetCeBankWiseMTD() {
		 
	        CeBankWiseMTDEntity mockEntity = new CeBankWiseMTDEntity("1", "95.5%");
	        when(cebankWiseMTDRepository.getAllCeBankWiseMTDForCeUser("user123", "BankA")).thenReturn(mockEntity);

	        // Invoke service method
	        CeBankWiseMTDResponseDto response = cebankWiseMTDService.getCeBankWiseMTD("user123", "BankA");

	        // Assertions
	        assertNotNull(response);
	        assertEquals("1", response.getSrNo());
	        assertEquals("95.5%", response.getMtdUptime());
	        
	        Mockito.verify(cebankWiseMTDRepository, Mockito.times(1)).getAllCeBankWiseMTDForCeUser("user123", "BankA");
	    }
	 
	 	
	 public void testGetBankWiseNullMTD() {
		 
	        CeBankWiseMTDEntity mockEntity = new CeBankWiseMTDEntity(null, null);
	        when(cebankWiseMTDRepository.getAllCeBankWiseMTDForCeUser(null, null)).thenReturn(mockEntity);

	        // Invoke service method
	        CeBankWiseMTDResponseDto response = cebankWiseMTDService.getCeBankWiseMTD(null, null);

	        // Assertions
	       // assertNotNull(response);
	        assertEquals(null, response.getSrNo());
	        assertEquals(null, response.getMtdUptime());
	        
	        Mockito.verify(cebankWiseMTDRepository, Mockito.times(1)).getAllCeBankWiseMTDForCeUser(null, null);
	    }
	  
	}
	
	
	
	


