package com.racheldev.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
//public class OrdersProjectApplication extends SpringBootServletInitializer {
public class OrdersProjectApplication {
	
	@GetMapping("/mensaje")
	public String mensaje() {
		return "Hola Mundo";
	}
	
//Comentarizar para ejecución desde STS
	/*
   protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
       return builder.sources(OrdersProjectApplication.class);
   }
   */

	public static void main(String[] args) {
		SpringApplication.run(OrdersProjectApplication.class, args);
	}

}
