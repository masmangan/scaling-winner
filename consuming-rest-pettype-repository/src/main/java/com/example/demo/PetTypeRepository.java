// Copyright (c) 2020, the REST Pet Type Repository project authors.  Please see
// the AUTHORS file for details. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.	

package com.example.demo;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

/**
 * PetTypeRepository class is a RESTful wrapper.
 * 
 * All method calls are forwarded to a REST server.
 * REST server must be running on localhost.
 * 
 * @author marco.mangan@pucrs.br
 *
 */
@Repository
public class PetTypeRepository {

	/**
	 * Server-side Application Logger 
	 */
	private static final Logger log = Logger.getLogger(ConsumingRestApplication.class.getName());

	/**
	 * Template for REST requests and responses
	 */
	@Autowired
	RestTemplate restTemplate;

	/**
	 * Creates a request header with username and password.
	 * 
	 * @param username
	 * @param password
	 * 
	 * @return a HTTP Basic request header
	 */
	HttpHeaders createHeaders(String username, String password) {
		return new HttpHeaders() {
			/**
			* 
			*/
			private static final long serialVersionUID = 1L;

			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
			}
		};
	}

	/**
	 * Forwards a findById request to the REST server.
	 * 
	 * @return
	 */
	public PetType findById(int id) {
		HttpHeaders hs = createHeaders("admin", "admin");

		String serverUri = "http://localhost:9966/petclinic/api/pettypes/{id}";
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(hs);

		ResponseEntity<PetType> response = this.restTemplate.exchange(serverUri, HttpMethod.GET, request, PetType.class,
				id);
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new RuntimeException("PetTypeRepository::findById ID not found.");
		}
		PetType type = response.getBody();
		log.info(type.toString());
		return type;
	}

	/**
	 * Forwards a findAll request to the REST server.
	 * 
	 * @return
	 */
	public List<PetType> findAll() {
		HttpHeaders hs = createHeaders("admin", "admin");

		String serverUri = String.format("http://localhost:9966/petclinic/api/pettypes");
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(hs);

		ResponseEntity<PetType[]> response = this.restTemplate.exchange(serverUri, HttpMethod.GET, request,
				PetType[].class);
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new RuntimeException("PetTypeRepository::findAll Resource not found.");
		}
		PetType[] typeArray = response.getBody();
		List<PetType> types = Arrays.asList(typeArray);
		log.info(types.toString());
		return types;
	}

	/**
	 * Forwards a add or update request to the REST server.
	 * 
	 * A new petType object is created on the server and its Id is collected.
	 * Otherwise, the object data is updated on the server.
	 *  
	 * @param petType
	 */
	public void save(PetType petType) {
		HttpHeaders hs = createHeaders("admin", "admin");
		HttpEntity<PetType> request = new HttpEntity<>(petType, hs);
		ResponseEntity<PetType> response;

		if (petType.isNew()) {
			String serverUri = "http://localhost:9966/petclinic/api/pettypes";
			response = this.restTemplate.exchange(serverUri, HttpMethod.POST, request, PetType.class);
			if (response.getStatusCode() != HttpStatus.CREATED) {
				throw new RuntimeException("PetTypeRepository::save Save failed.");
			}
			PetType updated = response.getBody();
			petType.setId(updated.getId());
		} else {
			String serverUri = "http://localhost:9966/petclinic/api/pettypes/{id}";
			response = this.restTemplate.exchange(serverUri, HttpMethod.PUT, request, PetType.class, petType.getId());
			if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
				throw new RuntimeException("PetTypeRepository::save Update failed.");
			}
		}
		// log.info("::save HTTP Status " + response.getStatusCodeValue());

	}
}
