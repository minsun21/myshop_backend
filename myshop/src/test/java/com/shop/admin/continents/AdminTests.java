package com.shop.admin.continents;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shop.domain.Continent;
import com.shop.domain.Order;
import com.shop.domain.OrderItem;
import com.shop.domain.TripProduct;
import com.shop.domain.enums.ItemStatus;
import com.shop.domain.enums.OrderStatus;
import com.shop.repository.ContinentsRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import com.shop.repository.TripProductRepository;

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

	@Autowired
	OrderItemRepository orderItemRepository;

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
		// TripItem이 걸려있는 Order들의 상태가 결제 완료 됐고, 여행 시작일과 종료일에 걸쳐있으면 안됨.
		// 오늘 날짜가 여행 시작일에 있으면 안됨.
		// 걸려있으면 삭제는 나중에 하고 해당 나라여행 상품들은 비활성화 시키기

		String name = "newContinent";
		// 1. 해당 나라 여행 상품들을 찾는다. (결제 된것들)
		List<Order> cartOrders = orderRepository.findByStatus(OrderStatus.ORDER);
		boolean isRemove = true;
		for (Order order : cartOrders) {
			List<OrderItem> items = order.getOrderItems();
			isRemove = date_비교(items, name);
//			for (OrderItem orderItem : items) {
//				TripProduct tripItem = (TripProduct) orderItem.getItem();
//				String conditionContinentName = tripItem.getContinent().getName();
//				// 2. 삭제 하려는 나라와 여행 상품이 같으면
//				if (name.equals(conditionContinentName)) {
////					isRemove = date_비교(tripItem);
//				}
//				
////				String conName = ((TripProduct) item).getContinent().getName();
////				// cart에 있는데 비활성화 상태가 아니면,
////				if(name.equals(conName) && !item.getStatus().equals(ItemStatus.DISABLED)) {
////					log.info(">>>>>>>same continent");
////					item.modifiedStatus(ItemStatus.DISABLED);
////				}
//			}
		}
		// 걸린게 아무것도 없을 경우 삭제
		Continent findNameContinent = continentsRepository.findByName(name);
		if (isRemove) {
			continentsRepository.delete(findNameContinent);
		}else {
			// 걸린게 하나라도 있으면 비활성화
			TripProduct tripProduct = tripProductRepository.findByContinent(findNameContinent);
			tripProduct.modifiedStatus(ItemStatus.DISABLED);
		}
		em.flush();
		em.clear();

		// 비활성화 확인
		Continent continent = new Continent(name);
		TripProduct findProduct = tripProductRepository.findByContinent(continent);
		Assertions.assertThat(ItemStatus.DISABLED).isEqualTo(findProduct.getStatus());
	}

	@Test
	boolean date_비교(List<OrderItem> items, String name) {
		LocalDateTime today = LocalDateTime.now();
		for (OrderItem orderItem : items) {
			TripProduct tripItem = (TripProduct) orderItem.getItem();
			LocalDateTime startDate = tripItem.getStartDate();
			String conditionContinentName = tripItem.getContinent().getName();
			// 2. 삭제 하려는 나라와 여행 상품이 같으면
			if (name.equals(conditionContinentName)) {
				// 3. 여행일이 아직 안됐을 경우 -> 비활성화 
				if (startDate.isAfter(today))
					return false;
				// 4. 여행중일 경우 -> 비활성화
				else if (startDate.isBefore(today) && tripItem.getEndDate().isAfter(today))
					return false;
			}
		}
		return true;
	}
}
