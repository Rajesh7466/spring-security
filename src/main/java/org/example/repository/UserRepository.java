package org.example.repository;

import java.util.Optional;

import org.example.entity.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<UserInformation, String> {

	UserInformation  findByEmailIdAndPassword(String emailId, String password);
	
	 Optional<UserInformation> findByEmailId(String emailId); 
}
