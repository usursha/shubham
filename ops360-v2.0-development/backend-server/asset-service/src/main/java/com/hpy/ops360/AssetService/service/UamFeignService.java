package com.hpy.ops360.AssetService.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.apache.http.HttpStatus;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hpy.keycloakbase.util.LoginUtil;
import com.hpy.ops360.AssetService.dto.DataResponseDto;
import com.hpy.ops360.AssetService.dto.HttpResponseDto;
import com.hpy.ops360.AssetService.dto.IResponseDtoImpl;
import com.hpy.ops360.AssetService.dto.ProfilePictureDto;
import com.hpy.ops360.AssetService.dto.ProfilePictureRequestDto;
import com.hpy.ops360.AssetService.dto.ProfilePictureResponseDto;
import com.hpy.ops360.AssetService.dto.UserProfilePictureDto;
import com.hpy.ops360.AssetService.dto.UsernameRequestDto;
import com.hpy.ops360.AssetService.entity.Asset;
import com.hpy.ops360.AssetService.repository.AssetRepository;
import com.hpy.ops360.AssetService.repository.UamFeignClient;
import com.hpy.ops360.AssetService.util.UtilMethod;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UamFeignService {

	@Value("${basePath}")
	private String basePath;

	@Value("${read-filePath}")
	private String dirPath;

	@Value("${asset.parent-type}")
	private String parentType;
	
	@Autowired
	private UamFeignClient feignclient;

	@Autowired
	private AssetRepository assetRepository;

	@Autowired
	private AssetService service;

	@Autowired
	private UtilMethod utilMethod;
	
	@Autowired
	private LoginUtil loginUtil;
	
	public ProfilePictureResponseDto getPhotoId(UsernameRequestDto request) {
		ProfilePictureResponseDto pic=new ProfilePictureResponseDto();
		String userId=utilMethod.getUserIdByUsername(request.getUsername());
		UserRepresentation user = utilMethod.getUsersResource().get(userId).toRepresentation();
		String picture=user.getAttributes().get("Photo").get(0);
		pic.setImageId(picture);
		return pic;
	}

	public IResponseDtoImpl savePicture(String token, ProfilePictureRequestDto assetDto) throws IOException {
			Asset data = new Asset();
			ProfilePictureDto profilepicture = new ProfilePictureDto();
//			data.setAssetName(asset.getAssetName());
			data.setParentId(loginUtil.getLoggedInUserId());
			data.setParentType(assetDto.getParentType());
			data.setMediaType(assetDto.getMediaType());
			data.setFileName(assetDto.getFileName());
			assetRepository.save(data);
			String generatedPath = service.generatePath(assetDto.getParentType());
			data.setDocPath(data.getParentType() + '/' + data.getAssetId());
			assetRepository.save(data);
			String file = assetDto.getFileName();
			String id = service.getId(assetDto.getFileName());
			profilepicture.setImageId(id);
			byte[] fileBytes = service.decodeBase64String(assetDto.getBase64String());
			Path path = Paths.get(generatedPath + '/' + id);
			Path paths = Files.write(path, fileBytes);
			IResponseDtoImpl response=feignclient.addProfilePhoto(token, profilepicture);
			log.info("file created successfully at: " + basePath);
			return response;
	}

	public IResponseDtoImpl fetchProfilePicture(String token) throws MalformedURLException, IOException {
		IResponseDtoImpl response=new IResponseDtoImpl();
		DataResponseDto data=new DataResponseDto();
		IResponseDtoImpl response2=feignclient.getPhoto(token);
		HttpResponseDto data2=service.getFile(response2.getData().getImageId());
		byte[] profilePicture=data2.getFile();
		String base64Image = Base64.getEncoder().encodeToString(profilePicture);
	    data.setImageId(base64Image);
		response.setData(data);
		response.setResponseCode(response2.getResponseCode());
		response.setMessage(response2.getMessage());
		response.setError(response2.getError());
		response.setErrorNumber(response2.getErrorNumber());
		response.setTimestamp(response2.getTimestamp());
		return response;
    }
	
	public IResponseDtoImpl fetchProfilePictureByUsername(String token, UserProfilePictureDto request) throws MalformedURLException, IOException {
		IResponseDtoImpl response=new IResponseDtoImpl();
		DataResponseDto data=new DataResponseDto();
		IResponseDtoImpl response2=feignclient.getPhotoByUsername(token, request);
		HttpResponseDto data2=service.getFileById(response2.getData().getImageId());
		byte[] profilePicture=data2.getFile();
		String base64Image = Base64.getEncoder().encodeToString(profilePicture);
	    data.setImageId(base64Image);
		response.setData(data);
		response.setResponseCode(response2.getResponseCode());
		response.setMessage(response2.getMessage());
		response.setError(response2.getError());
		response.setErrorNumber(response2.getErrorNumber());
		response.setTimestamp(response2.getTimestamp());
		return response;
    }
	

    public ResponseEntity<byte[]> viewFile(String username) throws IOException {
    	UsernameRequestDto request=new UsernameRequestDto();
		request.setUsername(username);
		ProfilePictureResponseDto response=getPhotoId(request);
		String fileId=response.getImageId();
    	String path=dirPath + File.separator + fileId;
    	log.info(path);
        byte[] thumbnailData = generateThumbnail(dirPath + File.separator + parentType + File.separator+fileId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpeg");
        return new ResponseEntity<>(thumbnailData, headers, HttpStatus.SC_OK);
    }

    private byte[] generateThumbnail(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }


}
