package org.example.repository;

import java.util.List;
import java.util.Optional;

import org.example.entity.OrderEntity;
import org.example.entity.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>{

	List<OrderEntity> findByUserOrderByOrderDateDesc(UserInformation user);
	List<OrderEntity> findByUserAndStatus(UserInformation user, String status);
    Optional<OrderEntity> findByIdAndUser(Long orderId, UserInformation user);
    List<OrderEntity> findByUser_EmailId(String emailId);
}
