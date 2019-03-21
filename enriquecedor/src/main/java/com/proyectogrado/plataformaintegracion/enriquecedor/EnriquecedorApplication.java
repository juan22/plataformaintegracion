package com.proyectogrado.plataformaintegracion.enriquecedor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EnriquecedorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnriquecedorApplication.class, args);
	}

}
