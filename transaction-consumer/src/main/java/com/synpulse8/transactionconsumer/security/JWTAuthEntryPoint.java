package com.synpulse8.transactionconsumer.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
@Log
public class JWTAuthEntryPoint implements AuthenticationEntryPoint {


	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		log.info("Accessing JWTAuthEntryPoint");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		String out;
		if (authException.getCause() != null) {
			out = authException.getCause().toString() + " " + authException.getMessage();
		} else {
			out = authException.getMessage();
		}

		byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", out));

		response.getOutputStream().write(body);
	}


}
