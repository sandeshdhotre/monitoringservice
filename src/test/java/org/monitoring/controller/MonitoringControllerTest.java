package org.monitoring.controller;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.monitoring.entity.ServiceDetail;
import org.monitoring.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ServiceRepository.class, MonitoringController.class })
public class MonitoringControllerTest {

	@Autowired
	ServiceRepository repository;
	
	@Autowired
	MonitoringController controller;
	
	@Test
	public void testRegisterService() {
		ServiceDetail service1 = new ServiceDetail("WelcomeService", "http://localhost:8080/","welcome");
		ServiceDetail response = controller.registerService(service1);
		ServiceDetail getResponse = controller.getServiceDetails(String.valueOf(response.getId()));
		assertEquals(response, getResponse);
	}
	
	@Test
	public void testUnregisterService() {
		ServiceDetail service1 = new ServiceDetail("WelcomeService", "http://localhost:8080/","welcome");
		ServiceDetail response = controller.registerService(service1);
		controller.unregisterService(String.valueOf(response.getId()));
		ServiceDetail getResponse = controller.getServiceDetails(String.valueOf(response.getId()));
		assertNull(getResponse);
	}
	
	@Test
	public void testGetRegisteredServiceList() {
		ServiceDetail service1 = new ServiceDetail("WelcomeService", "http://localhost:8080/","welcome");
		ServiceDetail service2 = new ServiceDetail("StudentSevice", "http://localhost:8081/","student/1");
		ServiceDetail service3 = new ServiceDetail("EmployeeService", "http://localhost:8082/","employee/2");
		controller.registerService(service1);
		controller.registerService(service2);
		controller.registerService(service3);
		Collection<ServiceDetail> list = controller.getRegisteredServiceList();
		assertEquals(3, list.size());
	}
}
