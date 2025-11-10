package org.example.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtill {
	private final String  SECREATE_KEY="3q2+7w==k9f+4W8n5h7mV1sP9y0rK8vQ2tX4uY6zA1bQ8=";
//	private final long TOKEN_EXPIRY_DURATION=10*60000;
	private final long TOKEN_EXPIRY_DURATION = 24 * 60 * 60 * 1000; // 24 hours

	
	private SecretKey getSecretKeyEntry() {
		byte[] keyBytes=Decoders.BASE64.decode(SECREATE_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
			//	generate Token 
	
			public String createToken(String emailId) {
				String token=Jwts.builder()
						.subject(emailId)
 						.issuedAt(new Date(System.currentTimeMillis())) //creation time 
						.expiration(new Date(System.currentTimeMillis()+TOKEN_EXPIRY_DURATION)) // expiry time
						.signWith(getSecretKeyEntry())
						.compact();
				System.out.println("Token is created : "+token);
				return token;
			}
			
//			validate the token 
			public boolean isValidateToken(String id,String token) {
				String userIdFromToken=getUserIdFromToken(token);
				System.out.println("User id retrive from token :"+userIdFromToken);
				return userIdFromToken.equalsIgnoreCase(id)&& isTokenExpired(token);
			     	
			}
			
//			User Id from token 
			public String getUserIdFromToken(String token) {	
				return Jwts.parser()
						.verifyWith(getSecretKeyEntry())
						.build()
						.parseSignedClaims(token)
						.getPayload()
						.getSubject();
			}
			
//			check if token is expired 
			public boolean isTokenExpired(String token) {
				 Date expirationTimeDate=Jwts.parser()
						 .verifyWith(getSecretKeyEntry())
						 .build()
						 .parseSignedClaims(token)
						 .getPayload()
						 .getExpiration();
				 System.out.println("Token expiry time :"+expirationTimeDate);
				 return expirationTimeDate.after(new Date());
			}
}
