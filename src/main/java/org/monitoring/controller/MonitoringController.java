package org.monitoring.controller;

import java.util.Collection;
import org.monitoring.entity.ServiceDetail;
import org.monitoring.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitoring")
public class MonitoringController {
	
	@Autowired
	ServiceRepository serviceRepository;
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public ServiceDetail registerService(@RequestBody ServiceDetail service) {
		return serviceRepository.addService(service);
	}
	
	@RequestMapping(value= "/deregister/{serviceId}", method = RequestMethod.POST)
	public boolean unregisterService(@PathVariable("serviceId") String id) {
		Integer serviceId = Integer.parseInt(id);
		serviceRepository.removeService(serviceId);
		return true;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public Collection<ServiceDetail> getRegisteredServiceList(){
		return serviceRepository.getAllServiceDetail();
	}
	
	@RequestMapping(value = "/register/{id}", method = RequestMethod.GET)
	public ServiceDetail getServiceDetails(@PathVariable("id") String id) {
		Integer serviceId = Integer.parseInt(id);
		return serviceRepository.getServiceDetail(serviceId);
	}
	
	@RequestMapping(value="/register/{id}/reset", method = RequestMethod.GET)
	public ServiceDetail resetServiceAvailability(@PathVariable("id") String id) {
		Integer serviceId = Integer.parseInt(id);
		ServiceDetail service = serviceRepository.getServiceDetail(serviceId);
		service.setLastDownTime(null);
		service.setCurrentDownTime(0L);
		service.setTotalDownTime(0L);
		return service;
	}
}
