package com.shop.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.shop.domain.IMAGE_TYPE;
import com.shop.domain.Image;
import com.shop.domain.Member;
import com.shop.domain.Product;
import com.shop.domain.ROLE;
import com.shop.repository.ContinentsRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.ProductRepository;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {
	private final String IMAGE_FOLDER = "src/main/resources/product/";

	private final ContinentsRepository continentsRepository;
	private final ProductRepository productRepository;
	private final MemberRepository memberRepository;

	@GetMapping(value = "/continents")
	public List<ContinentsDto> getContinentsList() {
		Continent con1 = new Continent("Africa");
		Continent con12 = new Continent("Europe");
		Continent con13 = new Continent("Asia");
		Continent con14 = new Continent("North America");
		Continent con15 = new Continent("South America");
		Continent con16 = new Continent("Australia");
		Continent con17 = new Continent("Antarctica");
		List<Continent> customers = Arrays.asList(con1, con12, con13, con14, con15, con16, con17);
		continentsRepository.saveAll(customers);
		List<ContinentsDto> resultList = continentsRepository.findAll().stream().map(ContinentsDto::new)
				.collect(Collectors.toList());
		return resultList;
	}

	@PostMapping(value = "/upload/image")
	public Map<String, String> uploadImage(@RequestParam("file") MultipartFile multipartFile) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		File targetFile = new File(IMAGE_FOLDER + multipartFile.getOriginalFilename());
		try (BufferedInputStream bis = new BufferedInputStream(multipartFile.getInputStream())) {
			FileUtils.copyInputStreamToFile(bis, targetFile);
		} catch (Exception e) {
			FileUtils.deleteQuietly(targetFile);
			result.put("result", e.getMessage());
			return result;
		}
		String path = "product/" + targetFile.getName();
		result.put("result", "success");
		result.put("path", path);
		return result;
	}

	@PostMapping("/products")
	public List<ProductDto> getProducts(@RequestBody Map<String, Object> conditionBody) {
		List<ProductDto> productList = productRepository.findAll().stream().map(ProductDto::new)
				.collect(Collectors.toList());
		return productList;
	}

	@Data
	class ProductDto {
		private String title;
		private String description;
		private int price;
		private int sold;
		private int views;
		private Continent continent;
		private List<String> imagePathList = new ArrayList<String>();

		public ProductDto(Product product) {
			this.title = product.getTitle();
			this.description = product.getDescription();
			this.price = product.getPrice();
			this.sold = product.getSold();
			this.views = product.getViews();
			this.continent = product.getContinent();
			for(Image image : product.getImages()) {
				imagePathList.add(image.getPath());
			}
		}
	}

	@PostMapping(value = "/upload")
	public Map<String, String> uploadProduct(@RequestBody Map<String, Object> productBody) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
//		try{
//			for(String key : productBody.keySet()) {
//				System.out.println("key : " + key);
//				System.out.println(productBody.get(key));
//			}
//		} catch (Exception e) {
//			result.put("result", e.getMessage());
//			return result;
//		}
		// [product/4876484354132.jpg, product/2020051403217_0.png,
		// product/99872A335A1AF37C31.jpg]

		int price = Integer.parseInt((String) productBody.get("price"));
		String con = String.valueOf(productBody.get("continent"));
		Continent continent = continentsRepository.findById(Long.valueOf(con)).orElseThrow(() -> new Exception("없음"));
		Member member = Member.builder().email("admin@admin.com").password("1234").name("adminUser")
				.phone("010-1234-4321").role(ROLE.ADMIN).build();
		Product product = Product.builder().member(member).title(productBody.get("title").toString())
				.description(productBody.get("desc").toString()).price(price).continent(continent).build();
		memberRepository.save(member);
		List<String> imageArray = (ArrayList<String>) productBody.get("images");
		for (String imagePath : imageArray) {
			product.addImage((getImageInfo(imagePath)));
		}
		productRepository.save(product);
	
		result.put("result", "success");
		return result;
	}

	public Image getImageInfo(String filePath) {
		int index = filePath.lastIndexOf(".");
		String ext = filePath.substring(index + 1).toUpperCase();
		Image image = Image.builder().path(filePath).type(IMAGE_TYPE.valueOf(ext)).build();
		return image;
	}

	@Data
	class Result<T> {
		private T data;
	}

	@Data
	class ContinentsDto {
		private Long continentId;
		private String name;

		public ContinentsDto(Continent continent) {
			this.continentId = continent.getContinentId();
			this.name = continent.getName();
		}
	}
}
