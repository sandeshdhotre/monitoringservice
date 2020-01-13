package org.monitoring;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class MonitoringServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoringServiceApplication.class, args);
	}

	@Bean
	@Autowired
	public RestTemplate getRestTemplate(SimpleClientHttpRequestFactory clientHttpRequestFactory) {
		return new RestTemplate(clientHttpRequestFactory);
	}
	
	@Bean
	public SimpleClientHttpRequestFactory getClientHttpRequestFactory() 
	{
	    SimpleClientHttpRequestFactory clientHttpRequestFactory
	                      = new SimpleClientHttpRequestFactory();
	    //Connect timeout
	    clientHttpRequestFactory.setConnectTimeout(1000);
	     
	    //Read timeout
	    clientHttpRequestFactory.setReadTimeout(1000);
	    return clientHttpRequestFactory;
	}
	
	
	@Bean
	public Docket getDocket() {
		return new Docket(DocumentationType.SWAGGER_2).
		select().
		paths(PathSelectors.ant("/monitoring/*")).
		apis(RequestHandlerSelectors.basePackage("org.javaindeapth")).build().apiInfo(getApiInfo());
	}
	
	private ApiInfo getApiInfo() {
		return new ApiInfo
				("Monitoring Service API", 
				"This Api Monitor Register services and Act as a Proxy server", 
				"1.0", 
				"Only Within JavaInDeapth", 
				new Contact("Sandesh", "http://localhost:9000/", "sandesh.dhotre@test.com"), 
				"", 
				"http://localhost:9000/",
				Collections.EMPTY_LIST);
	}
}
