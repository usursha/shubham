package com.hpy.ops360.ticketing.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.ops360.ticketing.dto.AtmDetailsDto;
import com.hpy.ops360.ticketing.entity.AllAtms;
import com.hpy.ops360.ticketing.repository.AllAtmsRepository;
import com.hpy.ops360.ticketing.response.OpenTicketsResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TicketSyncService {
	
	@Value("${ops360.ticketing.batch-size}")
	int batchSize;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SynergyService synergyService;
	
	@Autowired
	private AllAtmsRepository allAtmsRepository;
	
	
//	@Scheduled(cron = "0 0/2 * * * ?")
	public void syncTickets()
	{
		log.info("syncTickets() method started");
		//get all atms list from specified
		List<AllAtms> allAtmList=allAtmsRepository.getAllAtmList();
		log.info("all atms list : {}",allAtmList);
		log.info("all atms list size : {}",allAtmList.size());
		//check and get open tickets against all atms and save in db
		try {
			long startOpenticketsTime = System.currentTimeMillis();
			getOpenTicketsInBatches(allAtmList,batchSize);
			log.info("Time required for openticket fetch and insert : {}",(System.currentTimeMillis() - startOpenticketsTime) / 1000);
			long startOpenTicketUniqueListSp = System.currentTimeMillis();
			allAtmsRepository.executeOpenTicketUniqueListSp();
			log.info("Time required for openticket unique sp : {}",(System.currentTimeMillis() - startOpenTicketUniqueListSp) / 1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("Exception occured : {}",e);
		}
		log.info("syncTickets() method completed");
	}

	
	private void getOpenTicketsInBatches(List<AllAtms> allAtms, int batchSize) throws JsonProcessingException {
		log.info("getOpenTicketsInBatches() method started");
		List<AtmDetailsDto> atmList= allAtms.stream().map(atm -> new AtmDetailsDto(atm.getAtmId())).toList();
		
		log.info("batchSize:{}", batchSize);
		// Calculate the number of batches needed
		int numBatches = (atmList.size() + batchSize - 1) / batchSize;
		log.info("numBatches:{}", numBatches);
		for (int i = 0; i < numBatches; i++) {
			int startIdx = i * batchSize;
			int endIdx = Math.min((i + 1) * batchSize, atmList.size());
			log.info("startIdx:{}", startIdx);
			log.info("endIdx:{}", endIdx);
			List<AtmDetailsDto> atmBatch = atmList.subList(startIdx, endIdx);
			long startOpenTcktSyn = System.currentTimeMillis();
			OpenTicketsResponse response = synergyService.getOpenTicketDetails(atmBatch);
			log.info("Time required for fetch openticket from synergy : {}",(System.currentTimeMillis() - startOpenTcktSyn) / 1000);
			// Convert the response object to JSON string
			String jsonString = convertObjectToJson(response);
			log.info("open ticket response in string:{}", jsonString);
			// Call the stored procedure with the JSON string
			long startTicketInsertSP = System.currentTimeMillis();
			saveToDatabaseUsingStoredProcedure(jsonString);
			log.info("Time required for openticket unique sp : {}",(System.currentTimeMillis() - startTicketInsertSP) / 1000);

		}
		
		log.info("getOpenTicketsInBatches() method completed");
	}
	
	private String convertObjectToJson(OpenTicketsResponse response) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(response);
		log.info("Generated JSON: {}", jsonString); // Log the JSON to verify its structure
		return jsonString;
	}
	
	private void saveToDatabaseUsingStoredProcedure(String jsonString) {
		log.info("saveToDatabaseUsingStoredProcedure() method started");
		try {
			// Validate JSON data
			if (jsonString == null || jsonString.isEmpty()) {
				log.error("Invalid JSON data: empty or null");
				return;
			}

			log.info("Calling stored procedure with JSON data: {}", jsonString);

			String storedProcedure = "EXEC USP_GetOpenTicketsSynergy @json_data=?";
			int rowsAffected = jdbcTemplate.update(storedProcedure, jsonString);

			if (rowsAffected > 0) {
				log.info("Data saved successfully, rows affected: {}", rowsAffected);
			} else {
				log.warn("No rows were affected, please check the stored procedure and input data.");
			}
		} catch (Exception e) {
			log.error("Error while saving data to the database: ", e);
			throw new RuntimeException("Failed to save data to the database", e);
		}
		log.info("saveToDatabaseUsingStoredProcedure() method completed");
	}

}
