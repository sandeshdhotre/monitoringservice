package org.monitoring.controller;

import javax.servlet.http.HttpServletRequest;
import org.monitoring.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ServiceProxyController {
	
	@Autowired
	private ProxyService proxyService;
	
	@RequestMapping(value = "**", method = RequestMethod.GET)
	public ResponseEntity<String> getService(final HttpServletRequest request) {
		return proxyService.callService(request);
	}
}
