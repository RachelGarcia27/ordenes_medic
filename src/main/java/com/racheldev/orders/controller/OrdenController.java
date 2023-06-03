package com.racheldev.orders.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class OrdenController {

    /**
     * Variable que apunta a la bitácora de la clase.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdenController.class);

    /**
     * Constante que hace referencia al grid que contiene el listado de
     * instancias de la clase Orden.
     */
    protected static final String GRID_VIEW = "ordenes_grid";

    /**
     * Procesa la petición al grid de Ordenes disponibles.
     *
     * @return El nombre de la vista con el listado de instancias de la clase
     * Orden.
     */
    @GetMapping("/ordenes")
    public String showGrid() {
        LOGGER.debug("Mostrando el grid de Ordenes.");
        return GRID_VIEW;
    }
    
    
}
