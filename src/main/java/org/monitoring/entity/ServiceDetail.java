package org.monitoring.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ServiceDetail {
	private int id;
	private String serviceName;
	private String serviceURL;
	private String path;
	private Status status;
	private LocalDateTime lastDownTime;
	private Long currentDownTime;
	private Long totalDownTime;
	private Map<HttpStatus,AtomicLong> requestCount = new HashMap<HttpStatus,AtomicLong>();
	public enum Status{ UP,DOWN }
	
	public ServiceDetail() {}
	
	public ServiceDetail(String serviceName, String serviceURL, String path) {
		this.serviceName = serviceName;
		this.serviceURL = serviceURL;
		this.path = path;
	}
	
}
