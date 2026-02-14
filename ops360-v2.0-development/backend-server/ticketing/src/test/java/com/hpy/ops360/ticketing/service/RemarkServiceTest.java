//package com.hpy.ops360.ticketing.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import com.hpy.ops360.ticketing.dto.RemarkDto;
//import com.hpy.ops360.ticketing.entity.Remark;
//import com.hpy.ops360.ticketing.entity.Ticket;
//import com.hpy.ops360.ticketing.mapper.RemarkMapper;
//import com.hpy.ops360.ticketing.repository.RemarkRepository;
//
//class RemarkServiceTest {
//	private RemarkRepository remarkRepository;
//	private RemarkMapper remarkMapper;
//	private RemarkService remarkService;
//	private RemarkAssetsService remarkAssetsService;
//
//	@BeforeEach
//	public void setUp() {
//		remarkRepository = mock(RemarkRepository.class);
//		remarkMapper = mock(RemarkMapper.class);
//
//		remarkService = new RemarkService(remarkRepository, remarkMapper, remarkAssetsService);
//	}
//
//	@Test
//	void testCreateSeqNumber() {
//		Long ticketNumber = 123L;
//		RemarkDto parentRemark = new RemarkDto();
//		parentRemark.setId(456L);
//
//		when(remarkRepository.findMaxSeqNumberByTicketNumberAndParentRemarkId(ticketNumber, parentRemark.getId()))
//				.thenReturn(2);
//
//		int seqNumber = remarkService.createSeqNumber(ticketNumber, parentRemark);
//		System.out.println("seqNumber" + seqNumber);
//		assertEquals(3, seqNumber);
//	}
//
//	@Test
//	void testAddRemark() throws Exception {
//		RemarkDto remarkDto = new RemarkDto();
//		remarkDto.setTicketId(123L);
//		remarkDto.setRemarks("Remark");
//
////		RemarkDto parentRemarkDto = new RemarkDto();
////		parentRemarkDto.setId(null);
//		remarkDto.setParentRemark(null);
//
//		Ticket ticket = new Ticket();
//		ticket.setId(123L);
//
//		Remark remark = new Remark();
//		remark.setTicket(ticket);
//		remark.setRemarks("Remark");
//
////		when(remarkRepository.findMaxSeqNumberByTicketNumberAndParentRemarkId(remarkDto.getTicketId(),
////				remarkDto.getParentRemark().getId())).thenReturn(1);
//		when(remarkService.createSeqNumber(remarkDto.getTicketId(), remarkDto.getParentRemark())).thenReturn(2);
//		when(remarkMapper.toEntity(remarkDto)).thenReturn(remark);
////		when(remarkRepository.saveAndFlush(new Remark())).thenReturn(new Remark());
//		when(remarkRepository.save(remark)).thenReturn(remark);
//		when(remarkMapper.toDto(remark)).thenReturn(remarkDto);
//
//		RemarkDto result = remarkService.addRemark(remarkDto);
//
//		assertEquals(remarkDto, result);
//	}
//
//	@Test
//	void testGetRemarksForTicket() {
//		// Mock data
//		Remark remark1 = new Remark();
//		remark1.setId(1L);
//		remark1.setRemarks("Remark 1");
//		;
//
//		Remark remark2 = new Remark();
//		remark2.setId(2L);
//		remark2.setRemarks("Remark 2");
//
//		List<Remark> remarkList = Arrays.asList(remark1, remark2);
//
//		RemarkDto remarkDto1 = new RemarkDto();
//		remarkDto1.setId(1L);
//		remarkDto1.setRemarks("Remark 1");
//
//		RemarkDto remarkDto2 = new RemarkDto();
//		remarkDto2.setId(2L);
//		remarkDto2.setRemarks("Remark 2");
//
//		List<RemarkDto> remarkDtoList = Arrays.asList(remarkDto1, remarkDto2);
//
//		// Stubbing behavior
//		when(remarkRepository.findByTicketId(123L)).thenReturn(remarkList);
//		when(remarkMapper.toDto(remarkList)).thenReturn(remarkDtoList);
////		when(remarkMapper.toDto(remark2)).thenReturn(remarkDto2);
//
//		// Call the method under test
//		List<RemarkDto> result = remarkService.getRemarksForTicket(123L);
//
//		// Verify the result
//		assertEquals(remarkDtoList, result);
//	}
//
//}
