package com.shop.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shop.domain.Continent;
import com.shop.repository.ContinentsRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {
	private final String IMAGE_FOLDER = "src/main/resources/product/";

	private final ContinentsRepository continentsRepository;

	@GetMapping(value = "/upload/continents")
	public List<ContinentsDto> getContinentsList() {
		List<ContinentsDto> resultList = continentsRepository.findAll().stream()
				.map(m -> new ContinentsDto(m.getContinentId(), m.getName())).collect(Collectors.toList());
		return resultList;
	}

	@PostMapping(value = "/upload/image")
	public Map<String,String> uploadImage(@RequestParam("file") MultipartFile multipartFile) throws Exception {
		Map<String,String> result = new HashMap<String, String>();
		File targetFile = new File(IMAGE_FOLDER + multipartFile.getOriginalFilename());
		try (BufferedInputStream bis = new BufferedInputStream(multipartFile.getInputStream())) {
			FileUtils.copyInputStreamToFile(bis, targetFile);
		} catch (Exception e) {
			FileUtils.deleteQuietly(targetFile);
			result.put("result", e.getMessage());
			return result;
		}
		String path = "product/"+targetFile.getName();
		result.put("result", "success");
		result.put("path", path);
		return result;
	}

	@PostMapping(value = "/upload")
	public  Map<String,String> uploadProduct(@RequestBody Map<String,Object> productBody) {
		Map<String,String> result = new HashMap<String, String>();
		try{
			for(String key : productBody.keySet()) {
				System.out.println("key : " + key);
				System.out.println(productBody.get(key));
			}
		} catch (Exception e) {
			result.put("result", e.getMessage());
			return result;
		}
		result.put("result", "success");
		return result;
	}
	
	@Data
	@AllArgsConstructor
	class Result<T> {
		private T data;
	}

	@Data
	@AllArgsConstructor
	class ContinentsDto {
		private Long continentId;
		private String name;
	}

	@Data
	@AllArgsConstructor
	class ProductDto {
		private MultipartFile file;
		private String desc;
		private String name;
	}
}
