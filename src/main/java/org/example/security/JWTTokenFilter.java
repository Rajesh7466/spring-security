package org.example.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {
	Logger logger = LoggerFactory.getLogger(JWTTokenFilter.class); 
	@Autowired
	JwtUtill  jwtToken;
	@Autowired
	UserAuthenticationServiceImpl userAuthenticationServiceImpl;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 logger.info("validation of JWT token by OncePerRequestFilter"); 
		 String token=request.getHeader("Authorization");
		 logger.info( "JWT token :"+token );
		 String userName=null;
		 if(token!=null) {
		 userName=jwtToken.getUserIdFromToken(token);
		 logger.info("Token from userName: "+userName);
		
	}else {
		logger.info("Token is missing please come with token ");
		}
		 if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
		//	 if token is not expired :
				 System.out.println("OncePerRequestFilter : get userfomaton from the database by passing   userid ");
			 
//			 get userfomaton from the database by passing   userid :got from the token 
			UserDetails userDetails= userAuthenticationServiceImpl.loadUserByUsername(userName);
			
//			now passing the unique userid(came from the db) to the token validator 
			 System.out.println("OncePerRequestFilter :Now token is validationg is expiring or not");
			 
			 boolean isValidToken=jwtToken.isValidateToken(userDetails.getUsername(), token);
			 System.out.println("OncePerRequestFilter : Token validation result  " + isValidToken);
			 
			 if(isValidToken) {
				 System.out.println(" OncePerRequestFilter : Setting security context as for that user id User");
				 UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
				 		=new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null,null);
				 	usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				 	SecurityContextHolder .getContext().setAuthentication(usernamePasswordAuthenticationToken);
			 }else {
				 System.out.println("token is invaild , please try with valid token ");
			 }
			 
		  }
		 
		 filterChain.doFilter(request, response);
	}
}
