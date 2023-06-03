package com.racheldev.orders.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  /**
   * Variable que apunta a la bitácora de la clase.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

  /**
   * Constante que hace referencia a la página Home.
   */
  protected static final String GRID_VIEW = "home";

  /**
   * Procesa la petición de mostrar la página Home.
   *
   * @return La página inicial.
   */
  @GetMapping({"/","/home"})
  public String showGrid() {
      LOGGER.debug("Mostrando la página Home.");
      return GRID_VIEW;
  }
  
}