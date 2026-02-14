//package com.hpy.ops360.ticketing.service;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.hpy.generic.impl.GenericService;
//import com.hpy.ops360.ticketing.dto.RemarkDto;
//import com.hpy.ops360.ticketing.entity.Remark;
//import com.hpy.ops360.ticketing.mapper.RemarkMapper;
//import com.hpy.ops360.ticketing.repository.RemarkRepository;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//@AllArgsConstructor
//public class RemarkService extends GenericService<RemarkDto, Remark> {
//
//	private final RemarkRepository remarkRepository;
//
//	private final RemarkMapper remarkMapper;
//
//	private final RemarkAssetsService remarkAssetsService;
//
//	protected Integer createSeqNumber(Long ticketNumber, RemarkDto parentRemark) {
//		Integer maxSeqNumber = remarkRepository.findMaxSeqNumberByTicketNumberAndParentRemarkId(ticketNumber,
//				parentRemark == null ? null : parentRemark.getId());
//		return (maxSeqNumber != 0) ? maxSeqNumber + 1 : 1;
//	}
//
//	public RemarkDto addRemark(RemarkDto remarkDto) throws Exception {
//
//		// Determine the sequence number for the new remark
//		Integer seqNumber = createSeqNumber(remarkDto.getTicketId(), remarkDto.getParentRemark());
//		remarkDto.setSeqNumber(seqNumber);
//		log.info("remarkDto: {}", remarkDto);
//		// Save the new remark to the database
//		RemarkDto savedRemark = save(remarkDto);
//		if (remarkDto.getFile().length > 0 && remarkDto.getFile() != null) {
//			remarkAssetsService.uploadRemarkFiles(savedRemark.getId(), remarkDto.getFile());
//		}
//
//		return savedRemark;
//	}
//
////	public RemarkDto replyOnRemark(RemarkDto remarkDto) throws EntityNotFoundException {
////		// Determine the sequence number for the new remark
////		Integer seqNumber = createSeqNumber(remarkDto.getTicketId(), remarkDto.getParentRemark());
////		remarkDto.setSeqNumber(seqNumber);
////		log.info("remarkDto: {}", remarkDto);
////		RemarkDto savedRemarkReply = save(remarkDto);
////
////		RemarkDto existingParentRemark;
////		existingParentRemark = findById(remarkDto.getId());
////
//////		existingParentRemark.setParentRemarkId(remarkDto.getParentRemarkId());
////
////		log.info("existingParentRemark: {}", existingParentRemark);
////		List<RemarkDto> updateChildRemarks;
////		if (existingParentRemark.getChildRemarks() != null) {
////			updateChildRemarks = existingParentRemark.getChildRemarks();
////		} else {
////			updateChildRemarks = new ArrayList<>();
////		}
////		// Get existing child remarks
////		updateChildRemarks.add(savedRemarkReply); // Add the new remark as a child remark
////		existingParentRemark.setChildRemarks(updateChildRemarks); // Update the child remarks list
////		log.info("existingParentRemark: {}", existingParentRemark);
////		return update(existingParentRemark);
////	}
//
//	public List<RemarkDto> getRemarksForTicket(Long ticketId) {
//		List<Remark> remarks = remarkRepository.findByTicketId(ticketId);
//		log.info("remarks: {}", remarks);
//		return remarkMapper.toDto(remarks);
//	}
//
//}
