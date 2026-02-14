package com.hpy.ops360.atmservice;

import com.hpy.ops360.atmservice.dto.allBanksResponsedto;
import com.hpy.ops360.atmservice.entity.allBanksentity;
import com.hpy.ops360.atmservice.repository.allBanksrepository;
import com.hpy.ops360.atmservice.service.allBanksservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AllBanksServiceTest {

    @Mock
    private allBanksrepository allbanksrepository; 

    @InjectMocks
    private allBanksservice allbanksservice; 

    @Test
    void testGetAllBanksList_Success() {
        String cmUserId = "CM_TEST_001";
        List<allBanksentity> mockEntities = Arrays.asList(
                new allBanksentity("1", "HDFC Bank"),
                new allBanksentity("2", "ICICI Bank"),
                new allBanksentity("3", "SBI")
        );
        
        Mockito.when(allbanksrepository.getAllBanksListForCmUser(cmUserId)).thenReturn(mockEntities);
        
        List<allBanksResponsedto> result = allbanksservice.getAllBanksList(cmUserId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("HDFC Bank", result.get(0).getBankName());
        assertEquals("ICICI Bank", result.get(1).getBankName());
        assertEquals("SBI", result.get(2).getBankName());

        Mockito.verify(allbanksrepository, Mockito.times(1)).getAllBanksListForCmUser(cmUserId);
    }

    @Test
    void testGetAllBanksList_EmptyResponse() {
    	
        String cmUserId = "CM_NO_BANKS";
        List<allBanksentity> mockEntities = Arrays.asList(); 

        Mockito.when(allbanksrepository.getAllBanksListForCmUser(cmUserId)).thenReturn(mockEntities);

        List<allBanksResponsedto> result = allbanksservice.getAllBanksList(cmUserId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());

        Mockito.verify(allbanksrepository, Mockito.times(1)).getAllBanksListForCmUser(cmUserId);
    }

    @Test
    void testGetAllBanksList_NullCmUserId() {
        String cmUserId = null;
        List<allBanksentity> mockEntities = Arrays.asList(
                new allBanksentity("1", "Bank A")
        );

        Mockito.when(allbanksrepository.getAllBanksListForCmUser(cmUserId)).thenReturn(mockEntities);

        List<allBanksResponsedto> result = allbanksservice.getAllBanksList(cmUserId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bank A", result.get(0).getBankName());

        Mockito.verify(allbanksrepository, Mockito.times(1)).getAllBanksListForCmUser(cmUserId);
    }
}
