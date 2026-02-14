//package com.hpy.ops360.ticketing.service;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//import java.text.SimpleDateFormat;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.hpy.generic.Exception.EntityNotFoundException;
//import com.hpy.generic.impl.GenericService;
//import com.hpy.ops360.ticketing.dto.TicketAssetsDto;
//import com.hpy.ops360.ticketing.entity.Remark;
//import com.hpy.ops360.ticketing.entity.TicketAssets;
//import com.hpy.ops360.ticketing.repository.RemarkRepository;
//import com.hpy.ops360.ticketing.repository.TicketAssetsRepository;
//
//import lombok.extern.log4j.Log4j2;
//
//@Log4j2
//@Service
//public class TicketAssetsService extends GenericService<TicketAssetsDto, TicketAssets> {
//
//	private TicketAssetsRepository ticketAssetsRepository;
//
//	private RemarkRepository remarksRepository;
//
//	public TicketAssetsService(TicketAssetsRepository ticketAssetsRepository, RemarkRepository remarksRepository) {
//		this.ticketAssetsRepository = ticketAssetsRepository;
//		this.remarksRepository = remarksRepository;
//	}
//
//	@Value("${app.documentUploadDirectory}")
//	private String documentUploadDirectory;
//
//	@Value("${app.maxFileSize}")
//	private int maxFileSize;
//
//	public void uploadFiles(Long remarkId, MultipartFile[] files) {
//
//		File baseDirectory = new File(documentUploadDirectory);
//		if (!baseDirectory.exists()) {
//			if (baseDirectory.mkdirs()) {
//				log.info("Base directory created : {} ", baseDirectory.getAbsolutePath());
//			} else {
//				log.error("Failed to create base directory : {} ", baseDirectory.getAbsolutePath()); // return;
//			}
//		}
//
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		String currentDateFolder = dateFormat.format(new Date());
//		String uploadDirectory = documentUploadDirectory + File.separator + currentDateFolder;
//
//		File directory = new File(uploadDirectory);
//		if (!directory.exists()) {
//			if (directory.mkdirs()) {
//				log.info("Upload directory created : {} ", directory.getAbsolutePath());
//			} else {
//				log.error("Failed to create upload directory : {} ", directory.getAbsolutePath());
//				return;
//			}
//		}
//
//		Optional<Remark> remarks = remarksRepository.findById(remarkId);
//
//		log.info("remarks : {} ", remarks);
//
//		for (int i = 0; i < files.length; i++) {
//			MultipartFile file = files[i];
//
//			if (file.getSize() <= maxFileSize) {
//
//				String fileName = file.getOriginalFilename();
//
//				String filePath = uploadDirectory + File.separator + fileName;
//				String filePath1 = uploadDirectory;
//				try {
//
//					Files.copy(file.getInputStream(), Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);
//
//					TicketAssets ticketAssetsEntity = new TicketAssets();
//					ticketAssetsEntity.setFilePath(filePath1);
//					ticketAssetsEntity.setFileName(fileName);
//					ticketAssetsEntity.setRemarkId(remarkId);
//
//					ticketAssetsEntity.setCreatedBy(remarks.get().getCreatedBy());
//					ticketAssetsEntity.setLastModifiedBy(remarks.get().getLastModifiedBy());
//					ticketAssetsEntity.setParentRemarkId(remarks.get().getParentRemark().getId());
//					log.info("saving data : {} ", ticketAssetsEntity);
//					ticketAssetsRepository.save(ticketAssetsEntity);
//					log.info("after saving data");
//
//				} catch (Exception e) {
//					log.error("ERROR : {} ", e, e.getMessage(), e.getCause());
//				}
//
//			} else {
//				log.error("File size exceeds the allowed limit: " + file.getOriginalFilename());
//
//			}
//		}
//	}
//
//	public List<TicketAssetsDto> getAssetsByRemarkId(Long remarkId) {
//		List<TicketAssets> assets = ticketAssetsRepository.findAllByRemarkId(remarkId);
//		return getMapper().toDto(assets);
//	}
//
//	public Map<String, Object> downloadAssetById(Long assetId) {
//		Map<String, Object> result = new HashMap<>();
//		try {
//			TicketAssetsDto asset = findById(assetId);
//			if (asset == null)
//				return Collections.emptyMap();
//			else {
//				String fileName = asset.getFilePath() + File.separator + asset.getFileName();
//				RandomAccessFile f = new RandomAccessFile(fileName, "r");
//				byte[] b = new byte[(int) f.length()];
//				f.readFully(b);
//				f.close();
//
//				result.put("fileName", asset.getFileName());
//				result.put("file", b);
//				return result;
//
//			}
//		} catch (EntityNotFoundException | IOException e) {
//			log.error("Entity Not found of assetId: {}", assetId, e);
//			return null;
//		}
//
//	}
//}
