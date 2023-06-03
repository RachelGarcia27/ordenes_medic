package com.racheldev.orders.controller;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

import com.racheldev.orders.entity.Cliente;
import com.racheldev.orders.rest.AbstractRest;
import com.racheldev.orders.service.ClienteService;
import com.racheldev.orders.type.exception.ModelException;
import com.racheldev.orders.type.grid.GridResponse;

@RestController
@RequestMapping(value = "/cliente/")
public class ClienteRestController extends AbstractRest {


    private static final Logger LOGGER
            = LoggerFactory.getLogger(ClienteRestController.class);

    /**
     * Recurso que se encarga de todas las interacciones REST, mediante métodos
     * que reciben peticiones y responden a ellas vía mensajes JSON. Se utiliza
     * para las peticiones AJAX con el servidor.
     */
    @Resource
    private ClienteService clienteService;

    /**
     * Acción index Procesa la petición de desplegar la lista completa de
     * B01DivisionDTO en JSON procesando los parámetros recibidos con la
     * petición al grid.
     *
     * @param jsonObj Parámetros recibidos con la petición al grid.
     * @return La lista completa de B01DivisionDTO en JSON.
     */
    @Override
    public GridResponse<Cliente> index(JSONObject jsonObj) {
        LOGGER.debug("Mostrando Cliente.");
        List<Cliente> listaClientes = clienteService.findAll();
        return new GridResponse<Cliente>(listaClientes);
    }

    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Cliente create(@RequestBody Cliente dto) throws ModelException {
        LOGGER.debug("Creando Cliente en JSON {}", dto);
        return clienteService.create(dto);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Cliente update(@PathVariable("id") Long id,
            @RequestBody Cliente dto) throws ModelException {

        LOGGER.info("Actualizando Cliente id {} con {}", id, dto);

        return clienteService.update(dto);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Cliente findOne(@PathVariable("id") Long id) throws ModelException {
        LOGGER.debug("Creando Cliente en JSON {}");
        return clienteService.findById(id);
    }
    
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws ModelException {
        LOGGER.info("Borrando Cliente con id {}", id);
        clienteService.delete(id);
    }
    
    /**
     * Acción index Procesa la petición de desplegar la lista completa de
     * Cliente en JSON procesando los parámetros recibidos con la
     * petición al grid.
     */
    @GetMapping("/clientes")
    public List<Cliente> findAll() {
        LOGGER.debug("Mostrando Cliente.");
        List<Cliente> listaClientees = clienteService.findAll();

        return listaClientees;
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
	            LOGGER.info("Borrando Cliente con id {}", id);
	            clienteService.delete(id);
	        }
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public Cliente updateFromJSON(JSONObject jsonObj) throws ModelException {
        LOGGER.debug("Creando Cliente a partir de un JSON {}", jsonObj);
        Cliente dto = new Cliente(jsonObj);
        if (dto.getId() == null) {
            return create(dto);
        } else {
            return update(dto.getId(), dto);
        }
    }
    

    
}

