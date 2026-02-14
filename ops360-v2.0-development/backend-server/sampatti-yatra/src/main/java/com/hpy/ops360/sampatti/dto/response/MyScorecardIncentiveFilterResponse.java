package com.hpy.ops360.sampatti.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyScorecardIncentiveFilterResponse extends GenericDto {
	@JsonIgnore
	private Long id;

	private List<UserIncentiveRecordFilterResponse> userIncentives;

	public MyScorecardIncentiveFilterResponse(List<UserIncentiveRecordFilterResponse> userIncentives) {
		super();
		this.userIncentives = userIncentives;
	}

}
