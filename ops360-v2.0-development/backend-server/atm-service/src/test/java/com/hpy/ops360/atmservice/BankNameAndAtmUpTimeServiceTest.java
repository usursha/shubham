//package com.hpy.ops360.atmservice;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.hpy.ops360.atmservice.dto.BankNameAndAtmUpTimeDto;
//import com.hpy.ops360.atmservice.entity.BankNameAndAtmUpTimeEntity;
//import com.hpy.ops360.atmservice.repository.BankNameAndAtmUpTimeRepository;
//import com.hpy.ops360.atmservice.service.BankNameAndAtmUpTimeService;
//
//@ExtendWith(MockitoExtension.class)
//public class BankNameAndAtmUpTimeServiceTest {
//
//	@Mock
//	private BankNameAndAtmUpTimeRepository bankNameAndAtmUpTimeRepository;
//
//	@InjectMocks
//	private BankNameAndAtmUpTimeService bankNameAndAtmUpTimeService;
//
//	@BeforeEach
//	public void setUp() {
//		MockitoAnnotations.openMocks(this);
//	}
//
//	@Test
//	public void testGetBankNameAndAtmUpTime_success() {
//		String testAtmId = "CPCN61210";
//
//		BankNameAndAtmUpTimeEntity mockEntity = new BankNameAndAtmUpTimeEntity();
//		mockEntity.setSrNo("1");
//		mockEntity.setBankName("Axis Bank Non DFS");
//		mockEntity.setMonthtotilldateuptime(83.97153);
//
//		BankNameAndAtmUpTimeDto expectedDto = new BankNameAndAtmUpTimeDto();
//		expectedDto.setSrno("1");
//		expectedDto.setBankName("Axis Bank Non DFS");
//		expectedDto.setMonthtotilldateuptime(74.987);
//
//		when(bankNameAndAtmUpTimeRepository.getBankNameAndAtmUpTime(testAtmId)).thenReturn(mockEntity);
//
//		BankNameAndAtmUpTimeDto actualDto = bankNameAndAtmUpTimeService.getBankNameAndAtmUpTime(testAtmId);
//
//		assertEquals(expectedDto.getSrno(), actualDto.getSrno());
//		assertEquals(expectedDto.getBankName(), actualDto.getBankName());
//		assertEquals(expectedDto.getMonthtotilldateuptime(), actualDto.getMonthtotilldateuptime());
//
//		verify(bankNameAndAtmUpTimeRepository, times(1)).getBankNameAndAtmUpTime(testAtmId);
//	}
//
//	@Test
//	public void testGetBankNameAndAtmUpTime_noDataFound() {
//		String notExistAtmId = "NOTEXISTATM";
//
//		when(bankNameAndAtmUpTimeRepository.getBankNameAndAtmUpTime(notExistAtmId)).thenReturn(null);
//
//		BankNameAndAtmUpTimeDto actualDto = bankNameAndAtmUpTimeService.getBankNameAndAtmUpTime(notExistAtmId);
//
//		assertNull(actualDto, "Expected null response for nonexistent ATM ID");
//
//		verify(bankNameAndAtmUpTimeRepository, times(1)).getBankNameAndAtmUpTime(notExistAtmId);
//	}
//
//	@Test
//	public void testGetBankNameAndAtmUpTime_repositoryThrowsException() {
//		String testAtmId = "EXCEPTIONATM";
//
//		when(bankNameAndAtmUpTimeRepository.getBankNameAndAtmUpTime(testAtmId))
//				.thenThrow(new RuntimeException("Database error"));
//
//		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//			bankNameAndAtmUpTimeService.getBankNameAndAtmUpTime(testAtmId);
//		});
//
//		assertEquals("Failed to retrieve ATM uptime details", exception.getMessage());
//
//		verify(bankNameAndAtmUpTimeRepository, times(1)).getBankNameAndAtmUpTime(testAtmId);
//	}
//	
//}
