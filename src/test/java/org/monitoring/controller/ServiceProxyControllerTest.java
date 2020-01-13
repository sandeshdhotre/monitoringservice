package org.monitoring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.monitoring.entity.ServiceDetail;
import org.monitoring.repository.ServiceRepository;
import org.monitoring.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ServiceRepository.class, ServiceProxyController.class, RestTemplate.class, ProxyService.class})
@TestInstance(value = Lifecycle.PER_CLASS)
public class ServiceProxyControllerTest {
	
	@Autowired
	RestTemplate restTemplate;
	
	MockRestServiceServer serviceServer;
	
	@Autowired 
	ProxyService proxyService;
	
	@Autowired
	ServiceRepository serviceRepository;
	
	@Autowired 
	ServiceProxyController controller;
	
	@BeforeAll
	public void init() {
		ServiceDetail service1 = new ServiceDetail("WelcomeService", "http://localhost:8080/","welcome");
		serviceRepository.addService(service1);
		serviceServer = MockRestServiceServer.createServer(restTemplate);
	}
	
	@Test
	public void testGetService() {
		String url = "http://localhost:8080/welcome";
		serviceServer.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(url)).
		andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
        .andRespond(MockRestResponseCreators.withSuccess("Welcome", MediaType.TEXT_PLAIN));
		MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.toString(), "welcome");
		ResponseEntity<String> response = controller.getService(request);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		serviceServer.verify();
		serviceServer.reset();
	}
	
	@Test
	public void testGetServiceWithPathParam() {
		String url = "http://localhost:8080/welcome?name=sandesh";
		serviceServer.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(url)).
		andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
        .andRespond(MockRestResponseCreators.withSuccess("Welcome sandesh", MediaType.TEXT_PLAIN));
		MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.toString(), "welcome");
		request.setQueryString("name=sandesh");
		ResponseEntity<String> response = controller.getService(request);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody(), "Welcome sandesh");
		serviceServer.verify();
		serviceServer.reset();
	}
	
	@Test
	public void testGetServiceWithPathVariable() {
		String url = "http://localhost:8080/welcome/sandesh";
		serviceServer.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(url)).
		andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
        .andRespond(MockRestResponseCreators.withSuccess("Welcome sandesh", MediaType.TEXT_PLAIN));
		MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.toString(), "/welcome/sandesh");
		ResponseEntity<String> response = controller.getService(request);
		assertEquals(response.getBody(), "Welcome sandesh");
		serviceServer.verify();
		serviceServer.reset();
	}

}
