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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.racheldev.orders.entity.ProductoOrden;
import com.racheldev.orders.entity.ProductoOrdenDTO;
import com.racheldev.orders.rest.AbstractRest;
import com.racheldev.orders.service.ProductoOrdenService;
import com.racheldev.orders.type.exception.ModelException;
import com.racheldev.orders.type.grid.GridResponse;

@RestController
@RequestMapping(value = "/productoOrden/")
public class ProductoOrdenRestController extends AbstractRest {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(ProductoRestController.class);

    /**
     * Recurso que se encarga de todas las interacciones REST, mediante métodos
     * que reciben peticiones y responden a ellas vía mensajes JSON. Se utiliza
     * para las peticiones AJAX con el servidor.
     */
    @Resource
    private ProductoOrdenService productoOrdenService;

    /**
     * Acción index Procesa la petición de desplegar la lista completa de
     * B01DivisionDTO en JSON procesando los parámetros recibidos con la
     * petición al grid.
     *
     * @param jsonObj Parámetros recibidos con la petición al grid.
     * @return La lista completa de B01DivisionDTO en JSON.
     */
    @Override
    public GridResponse<ProductoOrden> index(JSONObject jsonObj) {
        LOGGER.debug("Mostrando Producto.");
        List<ProductoOrden> listaProductos = productoOrdenService.findAll();
        return new GridResponse<ProductoOrden>(listaProductos);
    }

    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ProductoOrden create(@RequestBody ProductoOrden dto) throws ModelException {
        LOGGER.debug("Creando ProductoOrden en JSON {}", dto);
        return productoOrdenService.create(dto);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ProductoOrden update(@PathVariable("id") Long id,
            @RequestBody ProductoOrden dto) throws ModelException {

        LOGGER.info("Actualizando ProductoOrden id {} con {}", id, dto);

        return productoOrdenService.update(dto);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ProductoOrden findOne(@PathVariable("id") Long id) throws ModelException {
        LOGGER.debug("Creando Producto en JSON {}");
        return productoOrdenService.findById(id);
    }
    
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws ModelException {
        LOGGER.info("Borrando D05Vacaciones con id {}", id);
        productoOrdenService.delete(id);
    }
    
    /**
     * Acción index Procesa la petición de desplegar la lista completa de
     * Producto en JSON procesando los parámetros recibidos con la
     * petición al grid.
     */
    @GetMapping("/productoOrdenes")
    public List<ProductoOrden> findAll() {
        LOGGER.debug("Mostrando ProductoOrden.");
        List<ProductoOrden> listaProductoes = productoOrdenService.findAll();
        
        return listaProductoes;
    }
    
    @GetMapping(value = "/productoOrdenesByOrden/{numeroOrden}")
    @ResponseBody
    public List<ProductoOrdenDTO> findByNumeroOrden(@PathVariable("numeroOrden") Long numeroOrden) {
        LOGGER.debug("Mostrando ProductoOrden por Orden.");
        List<ProductoOrdenDTO> listaProductoes = productoOrdenService.findByNumeroOrden(numeroOrden);
        
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
	            LOGGER.info("Borrando ProductoOrden con id {}", id);
	            productoOrdenService.delete(id);
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public ProductoOrden updateFromJSON(JSONObject jsonObj) throws ModelException {
        LOGGER.debug("Creando Producto a partir de un JSON {}", jsonObj);
        ProductoOrden dto = new ProductoOrden(jsonObj);
        if (dto.getId() == null) {
            return create(dto);
        } else {
            return update(dto.getId(), dto);
        }
    }
    

    
}
