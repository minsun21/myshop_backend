package com.shop.repository;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.multipart.MultipartFile;

import com.shop.domain.Continent;
import com.shop.domain.IMAGE_TYPE;
import com.shop.domain.Image;
import com.shop.domain.Member;
import com.shop.domain.Product;

@Transactional
@SpringBootTest
public class ProductRepositoryTest {
	private final String IMAGE_FOLDER = "src/main/resources/";
	@Autowired
	ContinentsRepository continentsRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ImageRepository imageRepository;

	@Test
	public void insertAllContinents() {
		Continent con1 = new Continent("Africa");
		Continent con12 = new Continent("Europe");
		Continent con13 = new Continent("Asia");
		Continent con14 = new Continent("North America");
		Continent con15 = new Continent("South America");
		Continent con16 = new Continent("Australia");
		Continent con17 = new Continent("Antarctica");
		List<Continent> customers = Arrays.asList(con1, con12, con13, con14, con15, con16, con17);
		continentsRepository.saveAll(customers);
	}

	@Test
	public void getAllContinents() {
		// 나라 전체 불러오기
		List<Continent> continents = continentsRepository.findAll();
		Assertions.assertThat(continents.size()).isEqualTo(7);
	}

	@Test
	public void upload_image(MultipartFile multipartFile) {
		File targetFile = new File(IMAGE_FOLDER + multipartFile.getOriginalFilename());
		try (BufferedInputStream bis = new BufferedInputStream(multipartFile.getInputStream())) {
			FileUtils.copyInputStreamToFile(bis, targetFile);
		} catch (Exception e) {
			FileUtils.deleteQuietly(targetFile);
		}
	}

	public List<Image> image_test() {
		String filePath1 = IMAGE_FOLDER + "product/99872A335A1AF37C31.jpg";
		String filePath12 = IMAGE_FOLDER + "product/2020051403217_0.png";
		String filePath13 = IMAGE_FOLDER + "product/4876484354132.jpg";

		int index = filePath1.lastIndexOf(".");
		String ext = filePath1.substring(index + 1).toUpperCase();
		Image image = Image.builder().path(filePath1).type(IMAGE_TYPE.valueOf(ext)).build();

		int index2 = filePath12.lastIndexOf(".");
		String ext2 = filePath12.substring(index2 + 1).toUpperCase();
		Image image2 = Image.builder().path(filePath12).type(IMAGE_TYPE.valueOf(ext2)).build();

		int index3 = filePath13.lastIndexOf(".");
		String ext3 = filePath13.substring(index3 + 1).toUpperCase();
		Image image3 = Image.builder().path(filePath13).type(IMAGE_TYPE.valueOf(ext3)).build();

		List<Image> images = new ArrayList<Image>();
		images.add(image);
		images.add(image2);
		images.add(image3);

		return images;
	}

	@Test
	public void find_member() throws Exception {
		Member member = memberRepository.findById((long) 1).orElseThrow(() -> new Exception("없음"));
		Assertions.assertThat(member.getEmail()).isEqualTo("admin@admin.com");
	}

	@Test
	public void find_continent() throws Exception {
		Continent continent = continentsRepository.findById((long) 1).orElseThrow(() -> new Exception("없음"));
		Assertions.assertThat(continent.getName()).isEqualTo("Africa");
	}

	@Test
	public void upload_image() {
		String filePath1 = IMAGE_FOLDER + "product/99872A335A1AF37C31.jpg";
		int index = filePath1.lastIndexOf(".");
		String ext = filePath1.substring(index + 1).toUpperCase();
		Image image = Image.builder().path(filePath1).type(IMAGE_TYPE.valueOf(ext)).build();
		imageRepository.save(image);
	}

	@Test
	@Rollback(false)
	public void upload_product() throws Exception {
		// 상품 등록 하기

//		Member member = memberRepository.findById((long) 1).orElseThrow(() -> new Exception("없음"));
//		Continent continent = continentsRepository.findById((long) 1).orElseThrow(() -> new Exception("없음"));
//		System.out.println(continent.getName());
//		 new image들
		String filePath1 = IMAGE_FOLDER + "product/99872A335A1AF37C31.jpg";
		String filePath12 = IMAGE_FOLDER + "product/2020051403217_0.png";
		String filePath13 = IMAGE_FOLDER + "product/4876484354132.jpg";

		int index = filePath1.lastIndexOf(".");
		String ext = filePath1.substring(index + 1).toUpperCase();
		Image image = Image.builder().path(filePath1).type(IMAGE_TYPE.valueOf(ext)).build();
		int index2 = filePath12.lastIndexOf(".");
		String ext2 = filePath12.substring(index2 + 1).toUpperCase();
		Image image2 = Image.builder().path(filePath12).type(IMAGE_TYPE.valueOf(ext2)).build();
		int index3 = filePath13.lastIndexOf(".");
		String ext3 = filePath13.substring(index3 + 1).toUpperCase();
		Image image3 = Image.builder().path(filePath13).type(IMAGE_TYPE.valueOf(ext3)).build();
		Product product = Product.builder().member(null).title("title").description("desc").price(10000).continent(null)
				.build();
//		product.addImage(imageRepository.save(image));
//		product.addImage(imageRepository.save(image2));
//		product.addImage(imageRepository.save(image3));
		try {
			product.addImage(image);
			product.addImage(image2);
			product.addImage(image3);
			productRepository.save(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Product searchProduct = productRepository.findById((long) 1).orElseThrow(() -> new Exception("없음"));
		System.out.println(searchProduct.toString());
	}

	@Test
	public void Page() {
		int skip = 0;
		int limit = 5;
		PageRequest pageRequest = PageRequest.of(skip, limit);

		Page<Product> page = productRepository.findAll(pageRequest);
//		List<ProductDto> productList = page.getContent().stream().map(ProductDto::new)
//				.collect(Collectors.toList());
//		Page<ProductDto> productList = page.map(ProductDto::new);	
//		List<ProductDto> productList = productRepository.findAll().stream().map(ProductDto::new)
//				.collect(Collectors.toList());
	}
}
