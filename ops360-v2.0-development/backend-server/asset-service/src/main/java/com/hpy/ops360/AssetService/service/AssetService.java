package com.hpy.ops360.AssetService.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;

import com.hpy.ops360.AssetService.dto.AssetMediaRequestDto;
import com.hpy.ops360.AssetService.dto.AssetMediaTypeResponseDto;
import com.hpy.ops360.AssetService.dto.AssetRequestDto;
import com.hpy.ops360.AssetService.dto.DataResponseDto;
import com.hpy.ops360.AssetService.dto.DtoAssetRequestWithMultipleFiles;
import com.hpy.ops360.AssetService.dto.HttpResponseDto;
import com.hpy.ops360.AssetService.dto.IResponseDtoImpl;
import com.hpy.ops360.AssetService.dto.ImageUrlsList;
import com.hpy.ops360.AssetService.dto.MediaFilesDto;
import com.hpy.ops360.AssetService.dto.MediaRequestDto;
import com.hpy.ops360.AssetService.entity.Asset;
import com.hpy.ops360.AssetService.entity.AssetMediaTypeEntity;
import com.hpy.ops360.AssetService.repository.AssetMediaTypeRepository;
import com.hpy.ops360.AssetService.repository.AssetRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetService {
	
	@Value("${read-ticketimagesPath}")
	private String dirPath;
	
	@Value("${saveAndReadTicketImagePath}")
	private String ticketImagePath;
	
	@Value("${ticket-images-path.url}")
	private String imageUrls;
	
	@Autowired
	private AssetRepository assetRepository;
	
	@Autowired
	private AssetMediaTypeRepository assetMediaTypeRepository;
	

	@Value("${basePath}")
    private String basePath;
	
	 public String getId(String filename) {
	    	String id=assetRepository.findAssetIdByFilename(filename);
	    	log.info(id);
	    	return id;
	    }
    
    public String getAssetId(String filename) {
    	String id=assetMediaTypeRepository.findAssetIdByFilename(filename);
    	log.info(id);
    	return id;
    }
    
    public String generatePath(String parentType) {
    	Path path = Paths.get(basePath, parentType);
    	try {
            // Create the directory if it doesn't exist
            Files.createDirectories(path);
            return path.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate path", e);
        }
    }

	// Decode the base64 String working code
	public byte[] decodeBase64String(String base64String) {
	    Base64.Decoder decoder = Base64.getDecoder();
	    return decoder.decode(base64String);
	}

	public String convertBase64ToFile(AssetRequestDto assetDto) {
		try {
			AssetMediaTypeEntity data = new AssetMediaTypeEntity();
//			data.setAssetName(asset.getAssetName());
			data.setAtmId(assetDto.getAtmId());
			data.setParentId(assetDto.getParentId());
			data.setParentType(assetDto.getParentType());
			data.setMediaType(assetDto.getMediaType());
			data.setFileName(assetDto.getFileName());
			assetMediaTypeRepository.save(data);
			String generatedPath=generatePath(assetDto.getParentType());
			data.setDocPath(data.getParentType()+File.separator+assetDto.getAtmId()+File.separator+assetDto.getParentId());
			assetMediaTypeRepository.save(data);
			String id = getAssetId(assetDto.getFileName());
			byte[] fileBytes = decodeBase64String(assetDto.getBase64String());
//			Path path = Paths.get(generatedPath+File.separator+id);
			Path path = Paths.get(generatedPath+File.separator+assetDto.getAtmId()+File.separator+assetDto.getParentId());
		    Path paths=Files.write(path, fileBytes);
			log.info("file created successfully at: " + basePath);
			return "file saved successfully..";
		} catch (IOException e) {
			e.printStackTrace();
			return "exception during saving the file!!";
		}
	}

	public ImageUrlsList savemultiplefiles(List<DtoAssetRequestWithMultipleFiles> assetDtoList) {
	    StringBuilder result = new StringBuilder();
	    ImageUrlsList urls=new ImageUrlsList();
	    List<String> listOfUrls=new ArrayList<>();
	    try {
	        for(DtoAssetRequestWithMultipleFiles assetDto : assetDtoList)  {
	            String basePath = generatePath(
	                assetDto.getParentType() + File.separator +
	                assetDto.getAtmId() + File.separator +
	                assetDto.getParentId()
	            );
	            Path dirPath = Paths.get(basePath);
	            if (!Files.exists(dirPath)) {
	                Files.createDirectories(dirPath);
	            }
	            int index = 1;
	            for (MediaFilesDto media : assetDto.getMediafiles()) {
	                byte[] fileBytes = decodeBase64String(media.getBase64());
	                AssetMediaTypeEntity data = new AssetMediaTypeEntity();
	                data.setAtmId(assetDto.getAtmId());
	                data.setParentId(assetDto.getParentId());
	                data.setParentType(assetDto.getParentType());
	                data.setMediaType(assetDto.getMediaType());
	                data.setFileName(media.getFilename());
	                assetMediaTypeRepository.save(data);
	                String docPath = assetDto.getParentType() + File.separator +
	                                 assetDto.getAtmId() + File.separator +
	                                 assetDto.getParentId() + File.separator +
	                                 data.getAssetId();
	                data.setDocPath(docPath);
	                assetMediaTypeRepository.save(data);
	                Path filePath = Paths.get(basePath + File.separator + data.getAssetId());
	                Files.write(filePath, fileBytes);
	                log.info("Saved file: " + media.getFilename() + " at " + filePath);
	                //fetching urls
	                AssetMediaRequestDto request=new AssetMediaRequestDto();
	                AssetMediaTypeResponseDto response=new AssetMediaTypeResponseDto();
	                request.setAtmId(assetDto.getAtmId());
	                request.setTicketId(assetDto.getParentId());
	                response=getImageUrlById(request, data.getAssetId());
	                listOfUrls.addAll(response.getImagesUrl());
	                urls.setImageurls(listOfUrls);
	                index++;
	            }
	        }
	        return urls;
	    } catch (IOException e) {
	        log.error("Error saving files: ", e);
	        result.append("Error occurred during saving the file..");
	        return null;
	    }
	}
	
	public AssetMediaTypeResponseDto getImageUrlById(AssetMediaRequestDto request, String targetFileId) throws IOException {
        List<String> ticketImagesUrl = new ArrayList<>();
        AssetMediaTypeResponseDto apiResponse = new AssetMediaTypeResponseDto();
        apiResponse.setAtmId(request.getAtmId());
        apiResponse.setTicketId(request.getTicketId());
        apiResponse.setImagesUrl(ticketImagesUrl);

        String dir = dirPath + File.separator + request.getAtmId() + File.separator + request.getTicketId();
        log.info("Directory path: " + dir);
        File directory = new File(dir);

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Provided path is not a directory");
        }

        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("Unable to list files in directory");
        }
        // Search for the specific file by ID
        Optional<File> targetFile = findFileById(files, targetFileId);
        if (targetFile.isPresent()) {
            String fullPath = targetFile.get().getAbsolutePath();
            String url = generateTicketImageUrls(request);
            String imageUrl = url + File.separator + Paths.get(fullPath).getFileName().toString();
            log.info("Found file: " + imageUrl);
            ticketImagesUrl.add(imageUrl);
        } else {
            log.warn("File with ID " + targetFileId + " not found.");
            throw new IOException("File with ID " + targetFileId + " not found.");
        }
        return apiResponse;
    }

    private Optional<File> findFileById(File[] files, String targetFileId) {
        for (File file : files) {
            if (file.isFile() && file.getName().equals(targetFileId)) {
                return Optional.of(file);
            }
        }
        return Optional.empty();
    }

	
	private String generateTicketImageUrls(AssetMediaRequestDto request) {
		String profilepictureUrl = String.format(imageUrls, request.getAtmId(),request.getTicketId());
		String url="";
        try {
        	url=profilepictureUrl;
        	log.info(url);
        	return url;
        }catch(NullPointerException npe) {
        	url="";
        	return npe.getMessage();
        }   
	}

	public AssetMediaTypeResponseDto getImageUrls(AssetMediaRequestDto request) throws IOException {
		List<String> ticketImagesUrl = new ArrayList<>();
		AssetMediaTypeResponseDto apiResponse=new AssetMediaTypeResponseDto();
		apiResponse.setAtmId(request.getAtmId());
		apiResponse.setTicketId(request.getTicketId());
		apiResponse.setImagesUrl(ticketImagesUrl);
		String dir = dirPath + File.separator + request.getAtmId() + File.separator + request.getTicketId();
		log.info("Directory path: " + dirPath);
		File directory = new File(dir);
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("Provided path is not a directory");
		}
		File[] files = directory.listFiles();
		if (files == null) {
			throw new IOException("Unable to list files in directory");
		}
		for (File file : files) {
			if (file.isFile()) {
				String fullPath = file.getAbsolutePath();
				String fileId = Paths.get(fullPath).getFileName().toString();
				String url = generateTicketImageUrls(request);
				String imagesurl = url + File.separator + fileId;
				log.info(imagesurl);
				ticketImagesUrl.add(imagesurl);
			}
		}
		return apiResponse;
	}

	public String getPath(String assetId) {
		AssetMediaTypeEntity asset=assetMediaTypeRepository.getReferenceById(assetId);
		String path=asset.getDocPath();
		String fullPath=basePath+path;
		return fullPath;
	}

	public HttpResponseDto getFileById(@PathVariable String id) throws MalformedURLException, IOException {
		HttpResponseDto res=new HttpResponseDto();
	    AssetMediaTypeEntity asset=assetMediaTypeRepository.getReferenceById(id);
	   	String parentType=asset.getParentType();
	    final Path filelocation = Paths.get(generatePath(parentType));
	    Path file = filelocation.resolve(id);
	    Resource resource = new UrlResource(file.toUri());
	    if (resource.exists() || resource.isReadable()) {
	    	byte[] fileContent = StreamUtils.copyToByteArray(resource.getInputStream());
	        res.setFile(fileContent);
	    }
        return res;                 
	}
	
	public HttpResponseDto getFile(@PathVariable String id) throws MalformedURLException, IOException {
		HttpResponseDto res=new HttpResponseDto();
	    Asset asset=assetRepository.getReferenceById(id);
	   	String parentType=asset.getParentType();
	    final Path filelocation = Paths.get(generatePath(parentType));
	    Path file = filelocation.resolve(id);
	    Resource resource = new UrlResource(file.toUri());
	    if (resource.exists() || resource.isReadable()) {
	    	byte[] fileContent = StreamUtils.copyToByteArray(resource.getInputStream());
	        res.setFile(fileContent);
	    }
        return res;                 
	}
	
	public Path getFilePath(@PathVariable String imageId) throws MalformedURLException, IOException {
		HttpResponseDto res=new HttpResponseDto();
	    AssetMediaTypeEntity asset=assetMediaTypeRepository.getReferenceById(imageId);
	   	String parentType=asset.getParentType();
	    final Path filelocation = Paths.get(generatePath(parentType));
	    Path path = filelocation.resolve(imageId);
        return path;                 
	}
	
	public ResponseEntity<byte[]> viewFileByName(String atmId, String ticketId, String fileId) throws IOException {
    	log.info(dirPath);
    	String dir=dirPath
    			+File.separator
    			+atmId
    			+File.separator
    			+ticketId;
        byte[] thumbnailData = generateThumbnail(dir+ File.separator + fileId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpeg");
        return new ResponseEntity<>(thumbnailData, headers, HttpStatus.SC_OK);
    }

	private byte[] generateThumbnail(String filePath) throws IOException {
	    return Files.readAllBytes(Paths.get(filePath));
	}

}
