package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryArchiveResponseDto {

	private ExcelData excelData;
    private CsvData csvData;

    @Data
    public static class ExcelData {
        private String base64;
        private String downloadLink; // Or you can omit this if you don't generate a link
    }

    @Data
    public static class CsvData {
        private String base64;
        private String downloadLink; // Or you can omit this if you don't generate a link
    }

}
