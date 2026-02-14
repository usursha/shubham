package com.hpy.ops360.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelemetryRequestWrapper {
	 private List<TelemetryRequest> data;
}
