package org.example.repository;

import org.example.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepoitory  extends JpaRepository<ProductEntity, Long> {

}
