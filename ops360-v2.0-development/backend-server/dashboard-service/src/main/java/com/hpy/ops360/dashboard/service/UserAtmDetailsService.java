package com.hpy.ops360.dashboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.dto.UsersAtmDetailsDto;
import com.hpy.ops360.dashboard.entity.UserAtmDetails;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.repository.UserAtmDetailsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserAtmDetailsService {

    private final UserAtmDetailsRepository userAtmDetailsRepository;

    // Method to get ATM details for any type of user like CM/CE
    @Loggable
    public List<UsersAtmDetailsDto> getUserAtmDetails(String userId) {
        List<UserAtmDetails> userAtmDetailsList = userAtmDetailsRepository.getUserAtmDetails(userId);

        // Convert List<UserAtmDetails> to List<UserAtmDetailsDto>
        return userAtmDetailsList.stream()
                .map(result -> new UsersAtmDetailsDto(
                        result.getSr_no(),
                        result.getUser_id(),
                        result.getUser_login_id(),
                        result.getUser_display_name(),
                        result.getAtm_code()
                )).toList();
    }
}
