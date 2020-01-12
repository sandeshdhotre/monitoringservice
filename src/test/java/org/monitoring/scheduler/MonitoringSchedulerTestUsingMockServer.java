package org.monitoring.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.monitoring.entity.ServiceDetail;
import org.monitoring.entity.ServiceDetail.Status;
import org.monitoring.repository.ServiceRepository;
import org.monitoring.scheduler.MonitorScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MonitorScheduler.class, ServiceRepository.class, RestTemplate.class})
@TestInstance(value = Lifecycle.PER_CLASS)
public class MonitoringSchedulerTestUsingMockServer {

	@Autowired
	RestTemplate restTemplate;
	
	MockRestServiceServer serviceServer;
	
	@Autowired 
	MonitorScheduler monitoringScheduler;
	
	@Autowired
	ServiceRepository serviceRepository;
	
	@BeforeAll
	public void init() {
		ServiceDetail service1 = new ServiceDetail("WelcomeService", "http://localhost:8080/","welcome");
		ServiceDetail service2 = new ServiceDetail("StudentSevice", "http://localhost:8081/","student/1");
		ServiceDetail service3 = new ServiceDetail("EmployeeService", "http://localhost:8082/","employee/2");
		serviceRepository.addService(service1);
		serviceRepository.addService(service2);
		serviceRepository.addService(service3);
		serviceServer = MockRestServiceServer.createServer(restTemplate);
	}
	
	@Test
	public void testServiceUp() {
		serviceServer.expect(ExpectedCount.times(3), MockRestRequestMatchers.anything()).
		andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
        .andRespond(MockRestResponseCreators.withSuccess("Success", MediaType.TEXT_PLAIN));
		monitoringScheduler.scheduleTaskWithFixedRate();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		serviceServer.verify();
		Collection<ServiceDetail> serviceList = serviceRepository.getAllServiceDetail();
		serviceList.stream().forEach(s -> assertEquals(s.getStatus(), Status.UP));
		serviceServer.reset();
	}
	
	
	@Test
	public void testServiceDown() {
		serviceServer.expect(ExpectedCount.times(3), MockRestRequestMatchers.anything()).
		andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
        .andRespond(MockRestResponseCreators.withServerError());
		monitoringScheduler.scheduleTaskWithFixedRate();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		serviceServer.verify();
		Collection<ServiceDetail> serviceList = serviceRepository.getAllServiceDetail();
		serviceList.stream().forEach(s -> assertEquals(s.getStatus(), Status.DOWN));
	}
}
