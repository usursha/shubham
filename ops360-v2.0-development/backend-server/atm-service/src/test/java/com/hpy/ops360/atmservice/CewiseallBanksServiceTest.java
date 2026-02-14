package com.hpy.ops360.atmservice;

import com.hpy.ops360.atmservice.dto.CewiseallBanksResponseDto;
import com.hpy.ops360.atmservice.entity.CewiseallBanksEntity;
import com.hpy.ops360.atmservice.repository.CewiseallBanksRepository;
import com.hpy.ops360.atmservice.service.CewiseallBanksService;

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
public class CewiseallBanksServiceTest {

    @Mock
    private CewiseallBanksRepository cewiseallbanksrepository; 

    @InjectMocks
    private CewiseallBanksService cewiseallbanksservice; 

    @Test
    void testGetCewiseallBanksList_Success() {
        String ceUserId = "CE_TEST_001";
        List<CewiseallBanksEntity> mockEntities = Arrays.asList(
                new CewiseallBanksEntity("1", "HDFC Bank"),
                new CewiseallBanksEntity("2", "ICICI Bank"),
                new CewiseallBanksEntity("3", "SBI")
        );
        
        Mockito.when(cewiseallbanksrepository.getallBanksListForCeUser(ceUserId)).thenReturn(mockEntities);
        
        List<CewiseallBanksResponseDto> result = cewiseallbanksservice.getcewiseBanksList(ceUserId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("HDFC Bank", result.get(0).getBankName());
        assertEquals("ICICI Bank", result.get(1).getBankName());
        assertEquals("SBI", result.get(2).getBankName());

        Mockito.verify(cewiseallbanksrepository, Mockito.times(1)).getallBanksListForCeUser(ceUserId);
    }

    @Test
    void testGetCewiseallBanksList_EmptyResponse() {
    	
        String ceUserId = "CM_NO_BANKS";
        List<CewiseallBanksEntity> mockEntities = Arrays.asList(); 

        Mockito.when(cewiseallbanksrepository.getallBanksListForCeUser(ceUserId)).thenReturn(mockEntities);

        List<CewiseallBanksResponseDto> result = cewiseallbanksservice.getcewiseBanksList(ceUserId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());

        Mockito.verify(cewiseallbanksrepository, Mockito.times(1)).getallBanksListForCeUser(ceUserId);
    }

    @Test
    void testGetCewiseallBanksList_NullCmUserId() {
        String ceUserId = null;
        List<CewiseallBanksEntity> mockEntities = Arrays.asList(
                new CewiseallBanksEntity("1", "Bank A")
        );

        Mockito.when(cewiseallbanksrepository.getallBanksListForCeUser(ceUserId)).thenReturn(mockEntities);

        List<CewiseallBanksResponseDto> result = cewiseallbanksservice.getcewiseBanksList(ceUserId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bank A", result.get(0).getBankName());

        Mockito.verify(cewiseallbanksrepository, Mockito.times(1)).getallBanksListForCeUser(ceUserId);
    }
}



