package com.shop.repository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.multipart.MultipartFile;

import com.shop.domain.Continent;
import com.shop.domain.Image;
import com.shop.domain.Member;
import com.shop.domain.Product;

import ch.qos.logback.classic.Logger;

import com.shop.domain.IMAGE_TYPE;

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
		Product product = Product.builder().member(null).title("title").description("desc").price(10000)
				.continent(null).build();
//		product.addImage(imageRepository.save(image));
//		product.addImage(imageRepository.save(image2));
//		product.addImage(imageRepository.save(image3));
		product.addImage(image);
		product.addImage(image2);
		product.addImage(image3);
		productRepository.save(product);
		
		Product searchProduct = productRepository.findById((long)1).orElseThrow(() -> new Exception("없음"));
		System.out.println(searchProduct.toString());
		
	}
}
