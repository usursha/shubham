package com.hpy.ops360.AssetService.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.AssetService.dto.ImageData;
import com.hpy.ops360.AssetService.dto.ImageSaveRequest;
import com.hpy.ops360.AssetService.dto.ImageSaveResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/image")
public class ImageController {

	@Value("${asset.base.path}")
	private String BASE_IMAGE_PATH;

	@Value("${asset.base.url}")
	private String BASE_IMAGE_URL;

	private static final long MAX_SIZE_BYTES = 1024 * 1024; // 1 MB
	private static final float MIN_QUALITY = 0.1f;

	@PostMapping("/save-images")
	public ResponseEntity<ImageSaveResponse> saveImages(@RequestBody ImageSaveRequest request) {
		log.info("Received image save request for ATM ID: {} and Ticket No: {}", request.getAtmId(),
				request.getTicketNo());

		List<String> newImageUrls = new ArrayList<>();
		String atmId = request.getAtmId();
		String ticketNo = request.getTicketNo();
		List<ImageData> images = request.getImages();

		String directoryPath = BASE_IMAGE_PATH + atmId + "/" + ticketNo;
		File directory = new File(directoryPath);
		if (!directory.exists()) {
			boolean created = directory.mkdirs();
			if (created) {
				log.info("Created directory: {}", directoryPath);
			} else {
				log.warn("Failed to create directory: {}", directoryPath);
			}
		}

		for (int i = 0; i < images.size() && i < 5; i++) {
			ImageData imageData = images.get(i);
			try {
				log.info("Processing image {} with name: {}", i + 1, imageData.getFileName());

				byte[] imageBytes = Base64.getDecoder().decode(imageData.getBase64Content());

				long sizeInBytes = imageBytes.length;
				double sizeInKB = sizeInBytes / 1024.0;
				double sizeInMB = sizeInKB / 1024.0;

				// Log size
				log.info("Image size: {} bytes, {:.2f} KB, {:.2f} MB", sizeInBytes, sizeInKB, sizeInMB);

				if (imageBytes.length > MAX_SIZE_BYTES) {
					log.info("Image exceeds 1 MB. Starting compression...");
					imageBytes = compressUntilUnderLimit(imageBytes);
				}

				String fileName = imageData.getFileName() + "_" + System.currentTimeMillis() + "_" + (i + 1) + ".jpg";
				File imageFile = new File(directory, fileName);
				Files.write(imageFile.toPath(), imageBytes);

				String imageUrl = BASE_IMAGE_URL + atmId + "/" + ticketNo + "/" + fileName;
				newImageUrls.add(imageUrl);

				log.info("Saved image {} as {} and URL: {}", i + 1, fileName, imageUrl);
			} catch (Exception e) {
				log.error("Error saving image {} for ATM ID: {} and Ticket No: {}. Image name: {}", i + 1, atmId,
						ticketNo, imageData.getFileName(), e);
			}
		}

		log.info("Successfully saved {} images for ATM ID: {} and Ticket No: {}", newImageUrls.size(), atmId, ticketNo);
		return ResponseEntity.ok(new ImageSaveResponse(newImageUrls));
	}

	private byte[] compressUntilUnderLimit(byte[] originalBytes) throws IOException {
		log.info("Started Compressing the image");
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(originalBytes));
		float quality = 0.9f;

		while (quality >= MIN_QUALITY) {
			byte[] compressed = compressImage(image, quality);
			log.debug("Compressed at quality {:.2f} -> {} KB", quality, compressed.length / 1024);

			if (compressed.length <= MAX_SIZE_BYTES) {
				return compressed;
			}
			quality -= 0.1f;
		}

		log.warn("Could not compress below 1 MB. Returning lowest quality.");
		return compressImage(image, MIN_QUALITY);
	}

	private byte[] compressImage(BufferedImage image, float quality) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
		ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();

		jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpgWriteParam.setCompressionQuality(quality);

		try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
			jpgWriter.setOutput(ios);
			jpgWriter.write(null, new IIOImage(image, null, null), jpgWriteParam);
		} finally {
			jpgWriter.dispose();
		}

		return baos.toByteArray();
	}

	private void logSize(String label, long bytes) {
		double kb = bytes / 1024.0;
		double mb = kb / 1024.0;
		log.debug("{} image size: {} bytes, {:.2f} KB, {:.2f} MB", label, bytes, kb, mb);
	}
}
