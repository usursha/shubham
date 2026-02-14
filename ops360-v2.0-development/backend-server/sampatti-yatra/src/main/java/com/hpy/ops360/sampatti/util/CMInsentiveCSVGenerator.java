package com.hpy.ops360.sampatti.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hpy.ops360.sampatti.dto.LeaderBoardIncentiveDownloadDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CMInsentiveCSVGenerator {
	@Value("${ceIncentivedownload.columns}")
	private String ceIncentivedownloadColumns;

	@Value("${cmIncentivedownload.columns}")
	private String cmIncentivedownloadColumns;

	@Value("${scmIncentivedownload.columns}")
	private String scmIncentivedownloadColumns;

	@Value("${rcmIncentivedownload.columns}")
	private String rcmIncentivedownloadColumns;

	public String generateCSVCE(List<LeaderBoardIncentiveDownloadDto> reportData) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		//CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(ceIncentivedownloadColumns.split(",")).build();
		
		// Split and trim headers
	    String[] headers = Arrays.stream(ceIncentivedownloadColumns.split(","))
	                             .map(String::trim)
	                             .toArray(String[]::new);

	    CSVFormat format = CSVFormat.DEFAULT.builder()
	            .setHeader(headers)
	            .setQuoteMode(QuoteMode.MINIMAL)
	            .build();

		try (Writer writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
				
				CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {
			
	        if (reportData == null || reportData.isEmpty()) {
	            log.error("No data found for CSV generation");
	            throw new IOException("No data found for CSV generation");
	        }
			
			for (LeaderBoardIncentiveDownloadDto report : reportData) {
				csvPrinter.printRecord(
						report.getRank(), 
						report.getFullName(), 
						report.getRole(), 
						report.getTarget(),
						report.getAchieved(), 
						report.getOverageShortage(), 
						report.getIncentiveAmount(),
						report.getChannelManager(), 
						report.getStateChannelManager(), 
						report.getZonalHead());
			}

			csvPrinter.flush();
		}catch (IOException e) {
	        log.error("Error while generating CSV for CE incentive data", e);
	        throw e;  // Propagate the exception
	    }

		return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
	}

	public String generateCSVCM(List<LeaderBoardIncentiveDownloadDto> reportData) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	//	CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(cmIncentivedownloadColumns.split(",")).build();
		
	    String[] headers = Arrays.stream(cmIncentivedownloadColumns.split(","))
	                             .map(String::trim)
	                             .toArray(String[]::new);

	    CSVFormat format = CSVFormat.DEFAULT.builder()
	            .setHeader(headers)
	            .setQuoteMode(QuoteMode.MINIMAL)
	            .build();

		try (Writer writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
				
				CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {
			
	        if (reportData == null || reportData.isEmpty()) {
	            log.error("No data found for CSV generation");
	            throw new IOException("No data found for CSV generation");
	        }

			for (LeaderBoardIncentiveDownloadDto report : reportData) {
				csvPrinter.printRecord(report.getRank(), 
						report.getFullName(), 
						report.getRole(), 
						report.getTarget(),
						report.getAchieved(), 
						report.getOverageShortage(), 
						report.getIncentiveAmount(),
						report.getStateChannelManager(), 
						report.getZonalHead());
			}

			csvPrinter.flush();
		}catch (IOException e) {
	        log.error("Error while generating CSV for CM incentive data", e);
	        throw e;  
	    }

		return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
	}

	public String generateCSVSCM(List<LeaderBoardIncentiveDownloadDto> reportData) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	//	CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(scmIncentivedownloadColumns.split(",")).build();
		
	    String[] headers = Arrays.stream(scmIncentivedownloadColumns.split(","))
	                             .map(String::trim)
	                             .toArray(String[]::new);

	    CSVFormat format = CSVFormat.DEFAULT.builder()
	            .setHeader(headers)
	            .setQuoteMode(QuoteMode.MINIMAL) 
	            .build();

		try (Writer writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
				
				CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {
			
	        if (reportData == null || reportData.isEmpty()) {
	            log.error("No data found for CSV generation");
	            throw new IOException("No data found for CSV generation");
	        }

			for (LeaderBoardIncentiveDownloadDto report : reportData) {
				csvPrinter.printRecord(report.getRank(), 
						report.getFullName(), 
						report.getRole(), 
						report.getTarget(),
						report.getAchieved(), 
						report.getOverageShortage(), 
						report.getIncentiveAmount(),
						report.getZonalHead());
			}

			csvPrinter.flush();
		}catch (IOException e) {
	        log.error("Error while generating CSV for SCM incentive data", e);
	        throw e;  
	    }

		return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
	}

	
	public String generateCSVRCM(List<LeaderBoardIncentiveDownloadDto> reportData) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	//	CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(rcmIncentivedownloadColumns.split(",")).build();
		
	    String[] headers = Arrays.stream(rcmIncentivedownloadColumns.split(","))
	                             .map(String::trim)
	                             .toArray(String[]::new);

	    CSVFormat format = CSVFormat.DEFAULT.builder()
	            .setHeader(headers)
	            .setQuoteMode(QuoteMode.MINIMAL)
	            .build();

		try (Writer writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
				
				CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {
			
	        if (reportData == null || reportData.isEmpty()) {
	            log.error("No data found for CSV generation");
	            throw new IOException("No data found for CSV generation");
	        }

			for (LeaderBoardIncentiveDownloadDto report : reportData) {

				csvPrinter.printRecord(report.getRank(), 
						report.getFullName(), 
						report.getRole(), 
						report.getTarget(),
						report.getAchieved(), 
						report.getOverageShortage(), 
						report.getIncentiveAmount());
			}

			csvPrinter.flush();
		}catch (IOException e) {
	        log.error("Error while generating CSV for RCM incentive data", e);
	        throw e; 
	    }

		return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
	}
}
