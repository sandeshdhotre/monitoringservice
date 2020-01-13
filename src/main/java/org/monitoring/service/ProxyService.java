package org.monitoring.service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.http.HttpServletRequest;
import org.monitoring.entity.ServiceDetail;
import org.monitoring.repository.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ProxyService {
	
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private Logger logger = LoggerFactory.getLogger(ProxyService.class); 

	public ResponseEntity<String> callService(final HttpServletRequest request){
		String url = request.getRequestURI();
		
		Optional<ServiceDetail> serviceDetails = serviceRepository.getServiceByServiceURL(url);
		
		ServiceDetail service = serviceDetails.orElse(null);
		
		ResponseEntity<String> response = new ResponseEntity<String>("No Service Found for Given URL", HttpStatus.BAD_REQUEST);
		
		if (service != null) {
			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(service.getServiceURL()).path(url);
			if(request.getQueryString()!=null) {
				uriBuilder.path("?"+request.getQueryString());	
			}
			final UriComponents uri = uriBuilder.build();
			try {
				response = restTemplate.getForEntity(uri.toString(), String.class);	
				setCount(service, response);
			}
			catch(RestClientResponseException e) {
				logger.error("RestClient Exception"+e.toString());
				int rawStatus = e.getRawStatusCode();
				HttpStatus status = HttpStatus.valueOf(rawStatus);
				response = new ResponseEntity<String>(status);
				setCount(service, response);
			}
			catch(ResourceAccessException e) {
				logger.error("Service Down"+e.toString());
				HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
				response = new ResponseEntity<String>(status);
				setCount(service, response);
			}
		}
		return response;
	}
	
	private void setCount(ServiceDetail service, ResponseEntity<String> response) {
		AtomicLong count = service.getRequestCount().get(response.getStatusCode());
		if (count == null) {
			count = new AtomicLong(1);
			service.getRequestCount().put(response.getStatusCode(), count);
		}
		else {
			count.incrementAndGet();
		}
		
	}
}
