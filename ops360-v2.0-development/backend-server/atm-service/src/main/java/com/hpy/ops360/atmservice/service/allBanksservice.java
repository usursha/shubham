package com.hpy.ops360.atmservice.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hpy.ops360.atmservice.dto.allBanksResponsedto;
import com.hpy.ops360.atmservice.entity.allBanksentity;
import com.hpy.ops360.atmservice.repository.allBanksrepository;

@Service
public class allBanksservice {

    @Autowired
    private allBanksrepository allbanksrepository;

    public List<allBanksResponsedto> getAllBanksList(String cmUserId) {

        List<allBanksentity> entities = allbanksrepository.getAllBanksListForCmUser(cmUserId);
        return entities.stream()
                       .map(entity -> new allBanksResponsedto(entity.getBankName()))
                       .collect(Collectors.toList());
    }
}
