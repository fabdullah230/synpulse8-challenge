package com.synpulse8.transactionconsumer.service;

import lombok.extern.java.Log;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Log
public class CustomUserDetailsService implements UserDetailsService {
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Accessing CustomUserDetailsService");
		List<SimpleGrantedAuthority> roles = null;
		if(username.equals("testUser"))
		{
			roles=Arrays.asList(new SimpleGrantedAuthority("ROLE_VIEW"));
			return new User("testUser","$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",roles);
		}
		throw new UsernameNotFoundException("No user with username: "+ username);
	}

}
