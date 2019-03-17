package com.proyectogrado.plataformaintegracion.conectorSalida;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ConectorSalidaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConectorSalidaApplication.class, args);
	}

}
