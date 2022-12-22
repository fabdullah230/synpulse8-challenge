package com.synpulse8.transactionconsumer.api;


import com.synpulse8.transactionconsumer.model.AuthReq;
import com.synpulse8.transactionconsumer.model.AuthRes;
import com.synpulse8.transactionconsumer.security.JWTUtil;
import com.synpulse8.transactionconsumer.service.CustomUserDetailsService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
@Log
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JWTUtil jwtUtil;

	@PostMapping
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthReq authReq) throws Exception {
		
		try {
			log.info("Accessing Authentication Controller");
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
		}
		catch (BadCredentialsException e) {
			throw new Exception("BAD_CREDENTIALS", e);
		}
		UserDetails userdetails = userDetailsService.loadUserByUsername(authReq.getUsername());
		String JWTtoken = jwtUtil.generateToken(userdetails);
		return ResponseEntity.ok(new AuthRes(JWTtoken));
	}
}

