package com.proyectogrado.plataformaintegracion.transformacionxmljson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TransformacionxmljsonApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransformacionxmljsonApplication.class, args);
	}

}
