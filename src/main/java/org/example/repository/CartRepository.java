package org.example.repository;

import java.util.Optional;

import org.example.entity.CartEntity;
import org.example.entity.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long>{

	 Optional<CartEntity> findByUser(UserInformation user);

}
