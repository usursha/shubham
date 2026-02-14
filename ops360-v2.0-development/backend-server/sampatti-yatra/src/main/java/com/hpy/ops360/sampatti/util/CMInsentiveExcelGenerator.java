package com.hpy.ops360.sampatti.util;

import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hpy.ops360.sampatti.dto.LeaderBoardIncentiveDownloadDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CMInsentiveExcelGenerator {

	@Value("${ceIncentivedownload.columns}")

	private String ceIncentivedownloadColumns;

	@Value("${cmIncentivedownload.columns}")

	private String cmIncentivedownloadColumns;

	@Value("${scmIncentivedownload.columns}")

	private String scmIncentivedownloadColumns;

	@Value("${rcmIncentivedownload.columns}")

	private String rcmIncentivedownloadColumns;

	public XSSFWorkbook generateExcelCE(List<LeaderBoardIncentiveDownloadDto> reportData) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Report");
		log.info("row:{}", reportData);
		Row headerRow = sheet.createRow(0);
		List<String> columns = Arrays.asList(ceIncentivedownloadColumns.split(","));

		for (int i = 0; i < columns.size(); i++) {

			headerRow.createCell(i).setCellValue(columns.get(i));
		}

		int rowNum = 1;

		for (LeaderBoardIncentiveDownloadDto report : reportData) {

			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(report.getRank());
			row.createCell(1).setCellValue(report.getFullName());
			row.createCell(2).setCellValue(report.getRole());
			row.createCell(3).setCellValue(report.getTarget());
			row.createCell(4).setCellValue(report.getAchieved());
			row.createCell(5).setCellValue(report.getOverageShortage());
			row.createCell(6).setCellValue(report.getIncentiveAmount());
			row.createCell(7).setCellValue(report.getChannelManager());
			row.createCell(8).setCellValue(report.getStateChannelManager());
			row.createCell(9).setCellValue(report.getZonalHead());

		}

		return workbook;

	}

	public XSSFWorkbook generateExcelCM(List<LeaderBoardIncentiveDownloadDto> reportData) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Report");
		log.info("row:{}", reportData);

		Row headerRow = sheet.createRow(0);

		List<String> columns = Arrays.asList(cmIncentivedownloadColumns.split(","));

		for (int i = 0; i < columns.size(); i++) {

			headerRow.createCell(i).setCellValue(columns.get(i));
		}

		int rowNum = 1;
		for (LeaderBoardIncentiveDownloadDto report : reportData) {

			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(report.getRank());
			row.createCell(1).setCellValue(report.getFullName());
			row.createCell(2).setCellValue(report.getRole());
			row.createCell(3).setCellValue(report.getTarget());
			row.createCell(4).setCellValue(report.getAchieved());
			row.createCell(5).setCellValue(report.getOverageShortage());
			row.createCell(6).setCellValue(report.getIncentiveAmount());
			row.createCell(7).setCellValue(report.getStateChannelManager());
			row.createCell(8).setCellValue(report.getZonalHead());

		}

		return workbook;

	}

	public XSSFWorkbook generateExcelSCM(List<LeaderBoardIncentiveDownloadDto> reportData) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Report");
		log.info("row:{}", reportData);

		Row headerRow = sheet.createRow(0);

		List<String> columns = Arrays.asList(scmIncentivedownloadColumns.split(","));

		for (int i = 0; i < columns.size(); i++) {

			headerRow.createCell(i).setCellValue(columns.get(i));
		}

		int rowNum = 1;
		for (LeaderBoardIncentiveDownloadDto report : reportData) {

			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(report.getRank());
			row.createCell(1).setCellValue(report.getFullName());
			row.createCell(2).setCellValue(report.getRole());
			row.createCell(3).setCellValue(report.getTarget());
			row.createCell(4).setCellValue(report.getAchieved());
			row.createCell(5).setCellValue(report.getOverageShortage());
			row.createCell(6).setCellValue(report.getIncentiveAmount());
			row.createCell(7).setCellValue(report.getZonalHead());

		}

		return workbook;

	}

	public XSSFWorkbook generateExcelRCM(List<LeaderBoardIncentiveDownloadDto> reportData) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Report");
		log.info("row:{}", reportData);

		Row headerRow = sheet.createRow(0);

		List<String> columns = Arrays.asList(rcmIncentivedownloadColumns.split(","));

		for (int i = 0; i < columns.size(); i++) {

			headerRow.createCell(i).setCellValue(columns.get(i));
		}

		int rowNum = 1;
		for (LeaderBoardIncentiveDownloadDto report : reportData) {

			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(report.getRank());
			row.createCell(1).setCellValue(report.getFullName());
			row.createCell(2).setCellValue(report.getRole());
			row.createCell(3).setCellValue(report.getTarget());
			row.createCell(4).setCellValue(report.getAchieved());
			row.createCell(5).setCellValue(report.getOverageShortage());
			row.createCell(6).setCellValue(report.getIncentiveAmount());

		}

		return workbook;

	}

}
