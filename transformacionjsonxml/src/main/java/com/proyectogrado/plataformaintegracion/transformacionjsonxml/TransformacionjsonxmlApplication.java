package com.proyectogrado.plataformaintegracion.transformacionjsonxml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TransformacionjsonxmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransformacionjsonxmlApplication.class, args);
	}

}
