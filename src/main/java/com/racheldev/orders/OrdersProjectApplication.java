package com.racheldev.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class OrdersProjectApplication extends SpringBootServletInitializer {
//public class OrdersProjectApplication {
	
//Comentarizar para ejecución desde STS
   protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
       return builder.sources(OrdersProjectApplication.class);
   }

	public static void main(String[] args) {
		SpringApplication.run(OrdersProjectApplication.class, args);
	}

}
