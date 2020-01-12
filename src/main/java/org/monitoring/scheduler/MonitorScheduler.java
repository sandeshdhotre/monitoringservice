package org.monitoring.scheduler;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.monitoring.entity.ServiceDetail;
import org.monitoring.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MonitorScheduler {
	
	@Autowired
	private ServiceRepository service;	
	
	@Autowired
	RestTemplate restTemplate;
	
	private final int poolSize = 10;
	
	private final long constant = 10000L;
	
	ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
	
	@Scheduled(fixedRate = constant)
	public void scheduleTaskWithFixedRate() {
		Collection<ServiceDetail> serviceList = service.getAllServiceDetail();
		
		Iterator<ServiceDetail> itr = serviceList.iterator();
		
		while(itr.hasNext()) {
			ServiceDetail service = itr.next();
			executorService.execute(new MonitoringTask(service, restTemplate));
		}
	}
}