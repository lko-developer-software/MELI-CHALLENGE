package com.meli.auth_server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AuthServerApplication {


	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);

		System.out.println("\n\n\n=================================");
		System.out.println("         MELI Challenge            ");
		System.out.println("=================================");
		System.out.println("----------- auth-server ----------");
		System.out.println("=================================");
		System.out.println("Ejecutando: http://localhost:3030");
		System.out.println("=================================");
	}




}
