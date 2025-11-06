package org.example.repository;

import java.util.List;

import org.example.entity.OrderEntity;
import org.example.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

	List<OrderItems> findByOrder(OrderEntity order);
}
