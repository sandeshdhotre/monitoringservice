package org.monitoring.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.monitoring.entity.ServiceDetail;
import org.monitoring.entity.ServiceDetail.Status;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceRepository {
	
	Map<String, ServiceDetail> serviceMap = new HashMap<String, ServiceDetail>();
	
	AtomicInteger sequence = new AtomicInteger(1);
	
	public Collection<ServiceDetail> getAllServiceDetail(){
		return serviceMap.values();
	}
	
	public ServiceDetail getServiceDetail(final String name) {
		return serviceMap.get(name);
	}
	
	public ServiceDetail addService(final ServiceDetail service) {
		Integer id = sequence.getAndIncrement();
		service.setId(id);
		service.setStatus(Status.UP);
		service.setLastDownTime(null);
		service.setTotalDownTime(0L);
		service.setCurrentDownTime(0L);
		serviceMap.put(service.getServiceName(), service);
		return service;
	}
	
	public ServiceDetail removeService(final String name) {
		return serviceMap.remove(name);
	}
	
	public Optional<ServiceDetail> getServiceByServiceName(final String name) {
		if(serviceMap.values().size()!=0) {
			return Optional.of(serviceMap.get(name));
		}
		return Optional.empty();
	}
}
