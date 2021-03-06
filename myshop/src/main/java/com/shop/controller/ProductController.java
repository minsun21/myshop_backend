package com.shop.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shop.domain.Continent;
import com.shop.domain.Image;
import com.shop.domain.Member;
import com.shop.domain.TripProduct;
import com.shop.domain.enums.IMAGE_TYPE;
import com.shop.repository.ContinentsRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.TripProductRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {
	private final String IMAGE_FOLDER = "src/main/resources/product/";

	private final ContinentsRepository continentsRepository;
	private final TripProductRepository tripProductRepository;
	private final MemberRepository memberRepository;

	@GetMapping(value = "/continents")
	public List<ContinentsDto> getContinentsList() {
//		Continent con1 = new Continent("Africa");
//		Continent con12 = new Continent("Europe");
//		Continent con13 = new Continent("Asia");
//		Continent con14 = new Continent("North America");
//		Continent con15 = new Continent("South America");
//		Continent con16 = new Continent("Australia");
//		Continent con17 = new Continent("Antarctica");
//		List<Continent> customers = Arrays.asList(con1, con12, con13, con14, con15, con16, con17);
//		continentsRepository.saveAll(customers);
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
	@GetMapping(value = "/products_by_id")
	public ProductDto getProductById(@RequestParam("id")String id,@RequestParam("type")String type) {
		ProductDto dto = tripProductRepository.findById(Long.parseLong(id)).map(ProductDto::new).orElseThrow(IllegalArgumentException::new);
		return dto;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/products")
	public Result<ProductMainDto> getProducts(@RequestBody Map<String, Object> conditionBody) {
		// limit:8. skip:0. 0번째부터 8개 가져와.
		int skip = (int)conditionBody.get("skip");
		// limit값이 있으면 limit. 없으면 20
		// skip값이 있으면 skip. 없으면 0
		int limit = (int) conditionBody.get("limit");
		PageRequest pageRequest = PageRequest.of(skip, limit);
		
//		Page<Product> page = productRepository.findAll(pageRequest);
//		List<ProductDto> productList = page.getContent().stream().map(ProductDto::new)
//				.collect(Collectors.toList());
		List<ProductMainDto> productList = tripProductRepository.findAll().stream().map(ProductMainDto::new)
				.collect(Collectors.toList());
//		return new Result(productList,page.hasNext());
		return new Result(productList,false);
	}
	
//	@PostMapping("/products")
//	public List<ProductDto> getProducts(@RequestBody Map<String, Object> conditionBody) {
//		int limit = (int) conditionBody.get("limit");
//		System.out.println(conditionBody.get("filters"));
//		List<Product> allProduct = productRepository.findAll();
//		
//		List<ProductDto> productList = allProduct.stream().map(ProductDto::new)
//				.collect(Collectors.toList());
//		return productList;
//	}
	@Data
	class ProductMainDto{
		private String id;
		private String title;
		private List<String> imagePathList = new ArrayList<String>();
		private String continent;
		public ProductMainDto(TripProduct product) {
			this.id = String.valueOf(product.getProductId());
			this.title = product.getTitle();
			this.continent = product.getContinent().getName();
			for(Image image : product.getImages()) {
				imagePathList.add(image.getPath());
			}
		}
	}

	@Data
	class ProductDto {
		private String id;
		private String title;
		private String description;
		private int price;
		private int sold;
		private int views;
		private String continent;
		private List<String> imagePathList = new ArrayList<String>();

		public ProductDto(TripProduct product) {
			this.id = String.valueOf(product.getProductId());
			this.title = product.getTitle();
			this.description = product.getDescription();
			this.price = product.getPrice();
			this.sold = product.getSold();
			this.views = product.getViews();
			this.continent = product.getContinent().getName();
			for(Image image : product.getImages()) {
				imagePathList.add(image.getPath());
			}
		}
	}

	@Transactional
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
//		Member member = Member.builder().email("admin@admin.com").password("1234").name("adminUser")
//				.phone("010-1234-4321").role(ROLE.ADMIN).build();
		Member member = null;
		TripProduct product = TripProduct.builder().member(member).title(productBody.get("title").toString())
				.description(productBody.get("desc").toString()).price(price).continent(continent).build();
		memberRepository.save(member);
		List<String> imageArray = (ArrayList<String>) productBody.get("images");
		for (String imagePath : imageArray) {
			product.addImage((getImageInfo(imagePath)));
		}
		tripProductRepository.save(product);
	
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
	@AllArgsConstructor
	class Result<T> {
		private T list;
		private boolean hasNext;
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
