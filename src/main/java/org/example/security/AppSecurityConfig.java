package org.example.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppSecurityConfig {
	@Autowired
	JWTTokenFilter jwtTokenFilter;
	
	Logger logger = LoggerFactory.getLogger(AppSecurityConfig.class);  
//	password encoder 
	@Bean
	BCryptPasswordEncoder getBCryptPasswordEncoder() {
		System.out.println("BCryptPasswordEncoder : Instance is creating");
		return new BCryptPasswordEncoder();
	}
//	bean object 
	@Bean
	AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
	{
		System.out.println("create instance of AuthenticatonManger");
		return authenticationConfiguration.getAuthenticationManager();
	}
	
//	Security filter chain : public/protected api
	@Bean
	public SecurityFilterChain securityConfig(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(csrf->csrf.disable())
		//.cors(csrf->csrf.disable())
		.authorizeHttpRequests(
				reqs->reqs.requestMatchers("/public/**")
				.permitAll()
	            .anyRequest()
	            .authenticated()
	            ).addFilterBefore(this.jwtTokenFilter,UsernamePasswordAuthenticationFilter.class);
		
		return  httpSecurity.build();
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
	    return new WebMvcConfigurer() {
	        @Override
	        public void addCorsMappings(CorsRegistry registry) {
	            registry.addMapping("/**")
	                .allowedOrigins("*") // or specify your frontend URL
	                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	                .allowedHeaders("*")
	                .exposedHeaders("Authorization"); // <-- This exposes the header!
	        }
	    };
	}
}