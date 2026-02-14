package com.hpy.sampatti_data_service.response;

import lombok.Data;

@Data
public class BankDataDto {
    private String atmId;
    private String grade;
    private String bankName;
    private String location;
    private int transactionTrend;
    private int uptimeTrend;
    private double downTime;
    private String downTimeInString;
    private String reason;
    private int mtdperformance;
    private String colorCode;
    private String bankType;
}
