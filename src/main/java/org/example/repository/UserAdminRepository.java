package org.example.repository;

import org.example.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserAdminRepository extends JpaRepository<AdminEntity, String> {
	
	
}
