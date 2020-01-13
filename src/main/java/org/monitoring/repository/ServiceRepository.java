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
	
	Map<Integer, ServiceDetail> serviceMap = new HashMap<Integer, ServiceDetail>();
	
	AtomicInteger sequence = new AtomicInteger(1);
	
	public Collection<ServiceDetail> getAllServiceDetail(){
		return serviceMap.values();
	}
	
	public ServiceDetail getServiceDetail(final Integer id) {
		return serviceMap.get(id);
	}
	
	public ServiceDetail addService(final ServiceDetail service) {
		Integer id = sequence.getAndIncrement();
		service.setId(id);
		service.setStatus(Status.UP);
		service.setLastDownTime(null);
		service.setTotalDownTime(0L);
		service.setCurrentDownTime(0L);
		serviceMap.put(id, service);
		return service;
	}
	
	public ServiceDetail removeService(final Integer id) {
		return serviceMap.remove(id);
	}
	
	public Optional<ServiceDetail> getServiceByServiceURL(final String url) {
		if(serviceMap.values().size()!=0) {
			return serviceMap.values().stream().filter(s -> url.contains(s.getPath())).findFirst();	
		}
		return Optional.empty();
	}
}
