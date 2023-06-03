package com.racheldev.orders.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductoController {

  /**
   * Variable que apunta a la bitácora de la clase.
   */
    private static final Logger LOGGER
    = LoggerFactory.getLogger(ProductoController.class);
	
  /**
   * Constante que hace referencia al grid que contiene el listado de
   * instancias de la clase Productos.
   */
  protected static final String GRID_VIEW = "productos_grid";

  /**
   * Procesa la petición al grid de Productos disponibles.
   *
   * @return El nombre de la vista con el listado de instancias de la clase
   * Productos.
   */
  @GetMapping("productos")
  public String showGrid() {
      LOGGER.debug("Mostrando el grid de Productos.");
      return GRID_VIEW;
  }
  
  
}
