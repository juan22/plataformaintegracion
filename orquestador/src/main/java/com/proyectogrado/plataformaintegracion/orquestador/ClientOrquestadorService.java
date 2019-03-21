package com.proyectogrado.plataformaintegracion.orquestador;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientOrquestadorService {
	
	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	protected String serviceUrl;

	protected Logger logger = Logger.getLogger(ClientOrquestadorService.class.getName());
			
	public ClientOrquestadorService() {
	}
	
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl: "http://" + serviceUrl;
	}
	
	public String ejecutarServicio(String mensajeCanonico){
		HttpEntity<String> entity = new HttpEntity<String>(mensajeCanonico);
		ResponseEntity<String> resultado = restTemplate.exchange(serviceUrl + "/ejecutar", HttpMethod.POST, entity, String.class);
		System.out.println("RESPUESTA!!!!="+resultado.getBody());
		return resultado.getBody();
	}
	
}
