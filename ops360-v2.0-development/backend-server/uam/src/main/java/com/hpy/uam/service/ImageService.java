package com.hpy.uam.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpy.rest.exception.ResourceNotFoundException;
import com.hpy.uam.dto.ProfilePictureRequestDto;
import com.hpy.uam.entity.Image;
import com.hpy.uam.repo.ImageRepository;
import com.hpy.uam.util.MethodUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageService {

	@Value("${basePath}")
	private String basePath;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private MethodUtil methodUtil;

	public byte[] decodeBase64String(String base64String) {
		Base64.Decoder decoder = Base64.getDecoder();
		return decoder.decode(base64String);
	}

	public String uploadOrUpdateImage(ProfilePictureRequestDto profilePictureRequestDto) throws IOException {
		String username = methodUtil.getLoggedInUserName();

		Optional<Image> optionalImage = imageRepository.findByUsername(username);

		byte[] fileBytes = decodeBase64String(profilePictureRequestDto.getBase64String());
		Path uploadPath = Paths.get(basePath);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		String extension = getExtensionFromMediaType(profilePictureRequestDto.getMediaType());
		String uniqueFilename = UUID.randomUUID().toString() + "." + extension;
		Path filePath = uploadPath.resolve(uniqueFilename);

		Files.write(filePath, fileBytes);

		if (optionalImage.isPresent()) {
			Image existingImage = optionalImage.get();

			Path oldFilePath = Paths.get(existingImage.getDocPath());
			if (Files.exists(oldFilePath)) {
				try {
					Files.delete(oldFilePath);
					log.info("Old file deleted successfully: {}", oldFilePath);
				} catch (IOException e) {
					log.error("Failed to delete old file: {}", oldFilePath, e);
				}
			}

			existingImage.setFileName(uniqueFilename);
			existingImage.setDocPath(filePath.toString());
			existingImage.setMediaType(profilePictureRequestDto.getMediaType());

			imageRepository.save(existingImage);
			return "File updated successfully: " + uniqueFilename;
		} else {
			Image newImage = new Image();
			newImage.setMediaType(profilePictureRequestDto.getMediaType());
			newImage.setFileName(uniqueFilename);
			newImage.setDocPath(filePath.toString());
			newImage.setUsername(username);

			imageRepository.save(newImage);
			return "New file uploaded successfully: " + uniqueFilename;
		}
	}

	public String deleteImage(String fileName) {
		log.info("Inside deleteImage method");
		Image image = imageRepository.findByFileName(fileName)
				.orElseThrow(() -> new ResourceNotFoundException("No image exits with fileName"));

		Path filePath = Paths.get(basePath).resolve(image.getFileName()).normalize();
		try {
			Files.delete(filePath);
			imageRepository.delete(image);
			log.info("Inside has been deleted");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Image has been deleted";
	}

	private String getExtensionFromMediaType(String mediaType) {
		switch (mediaType) {
		case "image/jpeg":
			return "jpg";
		case "image/png":
			return "png";
		case "image/gif":
			return "gif";
		default:
			return "bin";
		}
	}
// file changes
}
