package com.hpy.ops360.ticketing.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hpy.ops360.ticketing.dto.LanguageCategoryDto;
import com.hpy.ops360.ticketing.service.LanguageCategoryService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.dto.ResponseListDto;
import com.hpy.rest.util.RestUtils;

@ExtendWith(MockitoExtension.class)
class LanguageCategoryControllerTest {

	@Mock
	private LanguageCategoryService languageCategoryService;

	@InjectMocks
	private LanguageCategoryController languageCategoryController;

	@Mock
	private RestUtils restUtils;

	private List<LanguageCategoryDto> languageCategories;

	@Test
	void testGetActivatedLanguageCategories() {

		LanguageCategoryDto t1 = new LanguageCategoryDto();
		t1.setName("Marathi");
		t1.setLanguageLevel("मराठी");
		t1.setLanguageCode("mr");

		LanguageCategoryDto t2 = new LanguageCategoryDto();
		t1.setName("Hindi");
		t1.setLanguageLevel("हिंदी");
		t1.setLanguageCode("hi");

		List<LanguageCategoryDto> expectedLanguageCategories = List.of(t1, t2);

		when(languageCategoryService.getActivatedLanguageCategories()).thenReturn(expectedLanguageCategories);

		ResponseEntity<List<LanguageCategoryDto>> response = languageCategoryController
				.getActivatedLanguageCategories();

		assertEquals(expectedLanguageCategories.get(0).getLanguageCode(), response.getBody().get(0).getLanguageCode());
	}

//	@Test
//	void testGetActivatedLanguageCategories_Success() {
//		when(languageCategoryService.getActivatedLanguageCategories()).thenReturn(languageCategories);
//		when(restUtils.wrapResponse(eq(languageCategories), toString())).thenReturn(new ResponseListDto<>());
//
//		ResponseEntity<IResponseDto> response = languageCategoryController.getActivatedLanguageCategories();
//
//		assertNotNull(response);
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		verify(languageCategoryService, times(1)).getActivatedLanguageCategories();
//		verify(restUtils, times(1)).wrapResponse(eq(languageCategories), toString());
//	}

}
