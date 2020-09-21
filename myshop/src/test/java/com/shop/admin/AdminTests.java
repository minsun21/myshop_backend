package com.shop.admin;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shop.domain.Continent;
import com.shop.domain.Item;
import com.shop.domain.Order;
import com.shop.domain.OrderItem;
import com.shop.domain.TripProduct;
import com.shop.domain.enums.ItemStatus;
import com.shop.domain.enums.OrderStatus;
import com.shop.repository.ContinentsRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.OrderRepository;
import com.shop.repository.TripProductRepository;

import jdk.internal.org.jline.utils.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class AdminTests {
	@Autowired
	ContinentsRepository continentsRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	TripProductRepository tripProductRepository;
	
	@PersistenceContext
	EntityManager em;

	@Transactional
	@Test
	void addContinent() {
		// 나라 추가하기
		String name = "newContinent";
		Continent continent = new Continent(name);
		continentsRepository.save(continent);
		em.flush();
		em.clear();
		Continent findContinent = continentsRepository.findByName(name);
		Assertions.assertThat(name).isEqualTo(findContinent.getName());
	}
	
	@Transactional
	@Test
	void removeContinent() {
		// 나라 삭제하기
		// 단, Order의 상태가 카트인게 하나라도 걸려있으면 안됨.
		// 걸려있으면 삭제는 나중에 하고 해당 나라여행 상품들은 비활성화 시키기
		String name = "newContinent";
		// select * from orders where status='cart' and order_item
		List<Order> cartOrders = orderRepository.findByStatus(OrderStatus.ORDER);
		for(Order order : cartOrders) {
			List<OrderItem> items = order.getOrderItems();
			for(OrderItem orderItem : items) {
				Item item = orderItem.getItem();
				String conName = ((TripProduct) item).getContinent().getName();
				// cart에 있는데 비활성화 상태가 아니면,
				if(name.equals(conName) && !item.getStatus().equals(ItemStatus.DISABLED)) {
					log.info(">>>>>>>same continent");
					item.modifiedStatus(ItemStatus.DISABLED);
				}
			}
		}
		em.flush();
		em.clear();
		
		// 비활성화 확인
		Continent continent = new Continent(name);
		TripProduct findProduct = tripProductRepository.findByContinent(continent);
		Assertions.assertThat(ItemStatus.DISABLED).isEqualTo(findProduct.getStatus());
	}
}
