package org.monitoring.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.monitoring.MonitoringServiceApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MonitoringServiceApplication.class, 
webEnvironment = WebEnvironment.RANDOM_PORT)
class MonitoringSchedulerTest {
	
	@LocalServerPort
	private int port;
	
	@Test
	void test() {
	}

}
