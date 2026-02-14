//package com.hpy.ops360.dashboard.util;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//import org.springframework.stereotype.Component;
//
//import com.hpy.ops360.dashboard.dto.AtmDetailsDto;
//import com.hpy.ops360.dashboard.dto.OpenTicketsResponse;
//
//@Component
//public class AtmBatchProcessor {
//
//	private ExecutorService executorService;
//
////	@Value("${myapp.executor.poolSize}")
////    private int poolSize;
//
//	public AtmBatchProcessor(int numThreads) {
//		this.executorService = Executors.newFixedThreadPool(numThreads);
//	}
//
//	public List<OpenTicketsResponse> getOpenTicketDetailsInBatches(List<AtmDetailsDto> atms, int batchSize) {
//		List<Future<OpenTicketsResponse>> futures = new ArrayList<>();
//
//		for (int i = 0; i < atms.size(); i += batchSize) {
//			int startIdx = i;
//			int endIdx = Math.min(i + batchSize, atms.size());
//
//			List<AtmDetailsDto> atmBatch = atms.subList(startIdx, endIdx);
//			futures.add(executorService.submit(() -> getOpenTicketsForBatch(atmBatch)));
//		}
//
//		List<OpenTicketsResponse> allResponses = new ArrayList<>();
//		for (Future<OpenTicketsResponse> future : futures) {
//			try {
//				OpenTicketsResponse response = future.get();
//				allResponses.add(response);
//			} catch (Exception e) {
//				// Handle exceptions if needed
//				e.printStackTrace();
//			}
//		}
//
//		return allResponses;
//	}
//
//	public void shutdown() {
//		executorService.shutdown();
//	}
//
//}
