package com.hpy.ops360.ticketing.request;

import java.util.ArrayList;
import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationSearchResponse extends GenericDto {

	private List<String> response = new ArrayList<>();
}
