package com.racheldev.orders.controller;

import com.racheldev.orders.entity.Orden;
import com.racheldev.orders.entity.ProductoOrden;
import com.racheldev.orders.rest.AbstractRest;
import com.racheldev.orders.service.OrdenService;
import com.racheldev.orders.type.exception.ModelException;
import com.racheldev.orders.type.grid.GridResponse;
import java.util.List;
import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orden/")
public class OrdenRestController extends AbstractRest {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(OrdenRestController.class);

    /**
     * Recurso que se encarga de todas las interacciones REST, mediante métodos
     * que reciben peticiones y responden a ellas vía mensajes JSON. Se utiliza
     * para las peticiones AJAX con el servidor.
     */
    @Resource
    private OrdenService ordenService;

    /**
     * Acción index Procesa la petición de desplegar la lista completa de
     * Orden en JSON procesando los parámetros recibidos con la
     * petición al grid.
     *
     * @param jsonObj Parámetros recibidos con la petición al grid.
     * @return La lista completa de Orden en JSON.
     */
    @Override
    public GridResponse<Orden> index(JSONObject jsonObj) {
        LOGGER.debug("Mostrando Orden.");
        List<Orden> listaOrdenes = ordenService.findAll();
        return new GridResponse<Orden>(listaOrdenes);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Orden create(@RequestBody Orden dto) throws ModelException {
        LOGGER.debug("Creando Orden en JSON {}", dto);
        return ordenService.create(dto);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Orden update(@PathVariable("id") Long id,
            @RequestBody Orden dto) throws ModelException {

        LOGGER.info("Actualizando Orden id {} con {}", id, dto);

        return ordenService.update(dto);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Orden findOne(@PathVariable("id") Long id) throws ModelException {
        LOGGER.debug("Creando Orden en JSON {}");
        return ordenService.findById(id);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws ModelException {
        LOGGER.info("Borrando Orden con id {}", id);
        ordenService.delete(id);
    }
    
    /**
     * Acción index Procesa la petición de desplegar la lista completa de
     * Orden en JSON procesando los parámetros recibidos con la
     * petición al grid.
     */
    @GetMapping("/ordenes")
    public List<Orden> findAll() {
        LOGGER.debug("Mostrando Orden.");
        List<Orden> listaOrdenes = ordenService.findAll();
        
        return listaOrdenes;
    }
    
    /**
     * Acción DELETE Procesa la petición de borrar de un conjunto de registros
     * en base a su id
     *
     * @param jsonObj Objeto json con las lista de ids a borrar.
     * @throws ModelException para indicar que no se localizó el registro.
     */
    @Override
    protected void deleteAll(JSONObject jsonObj) throws ModelException {
        JSONArray idsArray;
		try {
			idsArray = (JSONArray)jsonObj.getJSONArray("selected");
	        for (int i = 0; i < idsArray.length(); i++) {
	        	Long id = idsArray.getLong(i);
	            LOGGER.info("Borrando Orden con id {}", id);
	            ordenService.delete(id);
	        }
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }

    @PostMapping(value = "/enviarOrden/{numeroOrden}")
    @ResponseBody
    public Orden enviarOrden(@PathVariable("numeroOrden") Long numeroOrden) throws ModelException {
        LOGGER.debug("Enviarndo Orden {}", numeroOrden);
        return ordenService.enviarOrden(numeroOrden);
    }
    
    @Override
    public Orden updateFromJSON(JSONObject jsonObj) throws ModelException {
        LOGGER.debug("Creando Orden a partir de un JSON {}", jsonObj);
        Orden dto = new Orden(jsonObj);
        if (dto.getId() == null) {
            return create(dto);
        } else {
            return update(dto.getId(), dto);
        }
    }

    @GetMapping("/max_num_orden")
    public Long getMaxByNumeroOrden() {
    	LOGGER.debug("Devuelve el máximo número Orden.");
    	return ordenService.findMaxByNumeroOrden();
    }
}

