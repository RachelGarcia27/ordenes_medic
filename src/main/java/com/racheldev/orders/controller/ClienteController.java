package com.racheldev.orders.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClienteController {


	  /**
	   * Variable que apunta a la bitácora de la clase.
	   */
	    private static final Logger LOGGER
	    = LoggerFactory.getLogger(ClienteController.class);
		
	  /**
	   * Constante que hace referencia al grid que contiene el listado de
	   * instancias de la clase Clientes.
	   */
	  protected static final String GRID_VIEW = "clientes_grid";

	  /**
	   * Procesa la petición al grid de Clientes disponibles.
	   *
	   * @return El nombre de la vista con el listado de instancias de la clase
	   * Clientes.
	   */
	  @GetMapping("/clientes")
	  public String showGrid() {
	      LOGGER.debug("Mostrando el grid de Clientes.");
	      return GRID_VIEW;
	  }
	  
	  
	}

