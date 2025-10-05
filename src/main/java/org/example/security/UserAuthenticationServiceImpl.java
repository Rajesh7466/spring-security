package org.example.security;

import org.example.entity.UserInformation;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class UserAuthenticationServiceImpl implements UserDetailsService {

	Logger logger = LoggerFactory.getLogger(UserAuthenticationServiceImpl.class); 
   @Autowired 
   UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		logger.info("fetaching userdetails "); 
		UserInformation userInformation=repository.findByEmailId(emailId).orElseThrow(()-> new UsernameNotFoundException("invalid user name"));
		
 		return  userInformation;
	}

}
