package com.meli.config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
		System.out.println("\n\n\n=================================");
		System.out.println("         MELI Challenge            ");
		System.out.println("=================================");
		System.out.println("-------- Config Server ---------");
		System.out.println("=================================");
		System.out.println("Ejecutando: http://localhost:7777");
		System.out.println("=================================");
	}

}
