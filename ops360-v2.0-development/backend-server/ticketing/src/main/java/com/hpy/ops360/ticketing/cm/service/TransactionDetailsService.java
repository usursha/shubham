package com.hpy.ops360.ticketing.cm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.ACTransactionDetailsRequest;
import com.hpy.ops360.ticketing.cm.dto.TransTargetdetailsDto;
import com.hpy.ops360.ticketing.cm.entity.TransTargetdetailsEntity;
import com.hpy.ops360.ticketing.cm.repo.TransTargetdetailsRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionDetailsService {

	@Autowired
	private TransTargetdetailsRepo repository;

	public List<TransTargetdetailsDto> gettranstargetdetails(ACTransactionDetailsRequest request) {

		log.info("******* Inside gettranstargetdetails Service *********");
		log.info("Request Recieved:- " + request);

		List<TransTargetdetailsEntity> rawResults = repository.gettranstargetdetail(request.getUserId(),
				request.getDate());

		log.info("Response Recieved From Repo:- " + rawResults);
		List<TransTargetdetailsDto> response = rawResults.stream()
				.map(result -> new TransTargetdetailsDto(result.getSrno(), result.getDate(), result.getUserId(),
						result.getTransactionTrend(), result.getTransactionTarget(), result.getPercentage_change()))
				.toList();
		return response;

	}
}
