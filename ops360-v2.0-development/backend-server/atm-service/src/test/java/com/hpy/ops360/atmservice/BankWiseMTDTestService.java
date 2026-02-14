package com.hpy.ops360.atmservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.atmservice.dto.BankWiseMTDResponseDto;
import com.hpy.ops360.atmservice.entity.BankWiseMTDEntity;
import com.hpy.ops360.atmservice.repository.BankWiseMTDRepository;
import com.hpy.ops360.atmservice.service.BankWiseMTDService;

@ExtendWith(MockitoExtension.class)
	public class BankWiseMTDTestService {
	
	@Mock
	private BankWiseMTDRepository bankWiseMTDRepository;
	
	@InjectMocks
	private BankWiseMTDService bankWiseMTDService;
	

	 @Test
	    public void testGetBankWiseMTD() {
		 
	        BankWiseMTDEntity mockEntity = new BankWiseMTDEntity("1", "99.5%");
	        when(bankWiseMTDRepository.getAllBankWiseMTDForCmUser("user123", "BankA")).thenReturn(mockEntity);

	        // Invoke service method
	        BankWiseMTDResponseDto response = bankWiseMTDService.getBankWiseMTD("user123", "BankA");

	        // Assertions
	        assertNotNull(response);
	        assertEquals("1", response.getSrNo());
	        assertEquals("99.5%", response.getMtdUptime());
	        
	        Mockito.verify(bankWiseMTDRepository, Mockito.times(1)).getAllBankWiseMTDForCmUser("user123", "BankA");
	    }
	 
	 	
	 public void testGetBankWiseNullMTD() {
		 
	        BankWiseMTDEntity mockEntity = new BankWiseMTDEntity(null, null);
	        when(bankWiseMTDRepository.getAllBankWiseMTDForCmUser(null, null)).thenReturn(mockEntity);

	        // Invoke service method
	        BankWiseMTDResponseDto response = bankWiseMTDService.getBankWiseMTD(null, null);

	        // Assertions
	       // assertNotNull(response);
	        assertEquals(null, response.getSrNo());
	        assertEquals(null, response.getMtdUptime());
	        
	        Mockito.verify(bankWiseMTDRepository, Mockito.times(1)).getAllBankWiseMTDForCmUser(null, null);
	    }
	 
	 
	
	 
	 
	 
	}
	
	
	
	


