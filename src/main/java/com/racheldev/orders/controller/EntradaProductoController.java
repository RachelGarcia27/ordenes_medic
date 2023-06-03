package com.racheldev.orders.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EntradaProductoController {

  /**
   * Variable que apunta a la bitácora de la clase.
   */
    private static final Logger LOGGER
    = LoggerFactory.getLogger(EntradaProductoController.class);
	
  /**
   * Constante que hace referencia a la forma para entrada de Productos
   * 
   */
  protected static final String FORM_VIEW = "entrada_productos_form";

  /**
   * Muestra la forma de Entrada de Productos.
   *
   * @return El nombre de la vista.
   */
  @GetMapping("/entrada_productos")
  public String showGrid() {
      LOGGER.debug("Mostrando la forma de entrada de Productos.");
      return FORM_VIEW;
  }
  
  
}
