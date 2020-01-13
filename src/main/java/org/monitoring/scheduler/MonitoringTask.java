package org.monitoring.scheduler;

import java.time.Duration;
import java.time.LocalDateTime;

import org.monitoring.entity.ServiceDetail;
import org.monitoring.entity.ServiceDetail.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class MonitoringTask implements Runnable{
	
	private RestTemplate restTemplate;
	
	private ServiceDetail service;
	
	private Logger logger = LoggerFactory.getLogger(MonitoringTask.class); 
	
	public MonitoringTask(ServiceDetail serviceDetail, RestTemplate restTemplate) {
		this.service = serviceDetail;
		this.restTemplate = restTemplate;
	}

	@Override
	public void run() {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(service.getServiceURL()).path(service.getPath());
		final UriComponents uri = uriBuilder.build();
		HttpStatus status;
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(uri.toString(), String.class);	
			status = response.getStatusCode();
		}
		catch(RestClientResponseException e) {
			logger.error("RestClient Exception"+e.toString());
			int rawStatus = e.getRawStatusCode();
			status = HttpStatus.valueOf(rawStatus);
		}
		catch(ResourceAccessException e) {
			logger.error("Service Down"+e.toString());
			status = HttpStatus.SERVICE_UNAVAILABLE;
		}
		if(HttpStatus.OK == status){
			if(service.getStatus() != Status.UP) {
				service.setStatus(Status.UP);
				service.setCurrentDownTime(0L);
			}
		}
		else{
			LocalDateTime currentTime = LocalDateTime.now();
			if(service.getStatus() == Status.UP) {
				service.setStatus(Status.DOWN);
				service.setLastDownTime(currentTime);
			}
			LocalDateTime downTime = service.getLastDownTime();
			Duration duration = Duration.between(downTime, currentTime);
			final long minutes = duration.toMinutes();
			if(minutes>0) {
				Long temp = minutes - service.getCurrentDownTime();
				service.setCurrentDownTime(minutes);
				service.setTotalDownTime(service.getTotalDownTime()+temp);
			}
		}
	}
	
	
}
