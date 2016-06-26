package com.palvair.springboot.rest.token;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Timed;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.palvair.springboot.rest.token.StatelessAuthenticationSecurityConfig;
import com.palvair.springboot.rest.token.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StatelessAuthenticationSecurityConfig.class)
@WebIntegrationTest
public class ApplicationIT {

	private RestTemplate restTemplate = new TestRestTemplate();

	@Value("${server.port}")
	private String port;

	@Test
	@Timed(millis = 5000)
	public void testToken() {
		HttpHeaders httpHeaders = getToken();
		System.out.println("headers = " + httpHeaders);
	}

	@Test
	public void testUserCurrent() {
		HttpHeaders httpHeaders = getToken();
		HttpEntity<String> testRequest = new HttpEntity<>(null, httpHeaders);
		ResponseEntity<User> testResponse = restTemplate.exchange("http://localhost:" + port + "/api/users/current",
				HttpMethod.GET, testRequest, User.class);
		Assert.assertEquals(HttpStatus.OK, testResponse.getStatusCode());
	}

	private HttpHeaders getToken() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		String password = "admin";
		String username = "admin";
		HttpEntity<String> login = new HttpEntity<>(
				"{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}", httpHeaders);
		ResponseEntity<Void> results = restTemplate.postForEntity("http://localhost:" + port + "/api/login", login,
				Void.class);
		Assert.assertEquals(HttpStatus.OK, results.getStatusCode());
		Assert.assertNotNull(results.getHeaders().getFirst("X-AUTH-TOKEN"));
		httpHeaders.add("X-AUTH-TOKEN", results.getHeaders().getFirst("X-AUTH-TOKEN"));
		return httpHeaders;
	}

}
