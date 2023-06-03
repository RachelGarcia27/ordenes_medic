package com.racheldev.orders.controller;

import java.util.List;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.racheldev.orders.entity.Producto;
import com.racheldev.orders.rest.AbstractRest;
import com.racheldev.orders.service.ProductoService;
import com.racheldev.orders.type.exception.ModelException;
import com.racheldev.orders.type.grid.GridResponse;

@RestController
@RequestMapping(value = "/producto/")
public class ProductoRestController extends AbstractRest {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(ProductoRestController.class);

    /**
     * Recurso que se encarga de todas las interacciones REST, mediante métodos
     * que reciben peticiones y responden a ellas vía mensajes JSON. Se utiliza
     * para las peticiones AJAX con el servidor.
     */
    @Resource
    private ProductoService productoService;

    /**
     * Acción index Procesa la petición de desplegar la lista completa de
     * B01DivisionDTO en JSON procesando los parámetros recibidos con la
     * petición al grid.
     *
     * @param jsonObj Parámetros recibidos con la petición al grid.
     * @return La lista completa de B01DivisionDTO en JSON.
     */
    @Override
    public GridResponse<Producto> index(JSONObject jsonObj) {
        LOGGER.debug("Mostrando Producto.");
        List<Producto> listaProductos = productoService.findAll();
        return new GridResponse<Producto>(listaProductos);
    }

    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Producto create(@RequestBody Producto dto) throws ModelException {
        LOGGER.debug("Creando Producto en JSON {}", dto);
        return productoService.create(dto);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Producto update(@PathVariable("id") Long id,
            @RequestBody Producto dto) throws ModelException {

        LOGGER.info("Actualizando Producto id {} con {}", id, dto);

        return productoService.update(dto);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Producto findOne(@PathVariable("id") Long id) throws ModelException {
        LOGGER.debug("Creando Producto en JSON {}");
        return productoService.findById(id);
    }
    
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws ModelException {
        LOGGER.info("Borrando D05Vacaciones con id {}", id);
        productoService.delete(id);
    }
    
    /**
     * Acción index Procesa la petición de desplegar la lista completa de
     * Producto en JSON procesando los parámetros recibidos con la
     * petición al grid.
     */
    @GetMapping("/productos")
    public List<Producto> findAll() {
        LOGGER.debug("Mostrando Producto.");
        List<Producto> listaProductoes = productoService.findAll();
        
        return listaProductoes;
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
	            LOGGER.info("Borrando Producto con id {}", id);
	            productoService.delete(id);
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public Producto updateFromJSON(JSONObject jsonObj) throws ModelException {
        LOGGER.debug("Creando Producto a partir de un JSON {}", jsonObj);
        Producto dto = new Producto(jsonObj);
        if (dto.getId() == null) {
            return create(dto);
        } else {
            return update(dto.getId(), dto);
        }
    }
    

    
}
