package com.meli.registry_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RegistryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistryServerApplication.class, args);
		System.out.println("\n\n\n=================================");
		System.out.println("         MELI Challenge            ");
		System.out.println("=================================");
		System.out.println("-------- Registry Server ---------");
		System.out.println("=================================");
		System.out.println("Ejecutando: http://localhost:8761");
		System.out.println("=================================");
	}

}
