package org.monitoring.controller;

import java.util.Collection;
import org.monitoring.entity.ServiceDetail;
import org.monitoring.entity.ServiceDetail.Status;
import org.monitoring.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitoring")
public class MonitoringController {
	
	@Autowired
	private ServiceRepository serviceRepository;
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public ResponseEntity<ServiceDetail> registerService(@RequestBody ServiceDetail service) {
		ServiceDetail serviceDetails = serviceRepository.addService(service);
		return new ResponseEntity<ServiceDetail>(serviceDetails, HttpStatus.CREATED);
	}
	
	@RequestMapping(value= "/register/{serviceName}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> unregisterService(@PathVariable("serviceName") String serviceName) {
		serviceRepository.removeService(serviceName);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ResponseEntity<Collection<ServiceDetail>> getRegisteredServiceList(){
		Collection<ServiceDetail> list = serviceRepository.getAllServiceDetail();
		return new ResponseEntity<Collection<ServiceDetail>>(list, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/register/{serviceName}", method = RequestMethod.GET)
	public ResponseEntity<ServiceDetail> getServiceDetails(@PathVariable("serviceName") String serviceName) {
		ServiceDetail serviceDetails = serviceRepository.getServiceDetail(serviceName);
		return new ResponseEntity<ServiceDetail>(serviceDetails, HttpStatus.OK);
	}
	
	@RequestMapping(value="/register/{serviceName}/reset", method = RequestMethod.PUT)
	public ResponseEntity<ServiceDetail> resetServiceAvailability(@PathVariable("serviceName") String serviceName) {
		ServiceDetail service = serviceRepository.getServiceDetail(serviceName);
		service.setStatus(Status.UP);
		service.setLastDownTime(null);
		service.setCurrentDownTime(0L);
		service.setTotalDownTime(0L);
		return new ResponseEntity<ServiceDetail>(service, HttpStatus.OK);
	}
}
