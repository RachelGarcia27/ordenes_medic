package com.racheldev.orders.rest;

import com.racheldev.orders.entity.BaseDAO;
import com.racheldev.orders.type.exception.ModelException;
import com.racheldev.orders.type.grid.GridResponse;
import com.racheldev.orders.type.grid.InfoResponse;
import com.racheldev.orders.util.JsonUtils;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * La definición de clase del Controlador Abstracto que provee métodos a
 * utilizar por otras clases concretas de controladores.
 */
public abstract class AbstractRest {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(AbstractRest.class);
    /**
     * La constante VIEW_REDIRECT_PREFIX.
     */
    private static final String VIEW_REDIRECT_PREFIX = "redirect:";

    /**
     * Crea una ruta para redireccionar la vista para una acción específica del
     * controlador.
     *
     * @param path La ruta precedida del método del controlador.
     * @return Una ruta de la vista para una método dado del controlador.
     */
    protected String createRedirectViewPath(String path) {
        StringBuilder builder = new StringBuilder();
        builder.append(VIEW_REDIRECT_PREFIX);
        builder.append(path);
        return builder.toString();
    }

    /**
     * Acción index Procesa la petición de desplegar la lista completa de
     * A02TipoServicio en JSON.
     *
     * @param request Comando a procesar, enviado por el grid.
     * @return La lista completa de A02TipoServicio en JSON.
     * @throws ModelException para indicar algun problema al procesar el
     * comando.
     * @throws JSONException 
     */
    @RequestMapping(value = "/cmd", method = RequestMethod.POST)
    @ResponseBody
    public GridResponse<? extends BaseDAO> doCmd(String request)
            throws ModelException, JSONException {
        JSONObject jsonObj = new JSONObject(request);
        String cmd = JsonUtils.parseString(jsonObj, "cmd");
        switch (cmd) {
            case "save-record":
            case "save":
                if (jsonObj.has("record")) {
                    return new GridResponse<>(
                            updateFromJSON(jsonObj.getJSONObject("record")));
                } else {
                    return applyChangesFromJSON(jsonObj.getJSONArray("changes"));
                }
            case "delete-records":
            case "delete":
                deleteAll(jsonObj);
                break;
            case "get-records":
            case "get":
                return index(jsonObj);
            default:
                throw new ModelException("Comando desconocido: [" + cmd + "]");
        }
        return new GridResponse<>(new ArrayList<>());
    }

    /**
     * Definición abstracta de Método que procesa las peticiones del grid w2ui
     * para obtener el listado de datos para el grid.
     *
     * @param jsonObj Objeto json con la petición del grid.
     * @return Regresa el conjunto de datos solicitado.
     */
    protected GridResponse<? extends BaseDAO> index(JSONObject jsonObj) {
        return new GridResponse<>(new ArrayList<>());
    }

    /**
     * Definición abstracta de Método que procesa las peticiones del grid w2ui
     * para actualizar o crear un registro.
     *
     * @param jsonObj Objeto json con la petición del grid.
     * @return Regresa la instancia creada o actualizada.
     * @throws ModelException para indicar algun problema al procesar el
     * comando.
     */
    protected BaseDAO updateFromJSON(JSONObject jsonObj)
            throws ModelException {
        return null;
    }

    /**
     * Definición abstracta de Método que procesa las peticiones del grid w2ui
     * para guardar cambios a multiples registros.
     *
     * @param jsonArray el arreglo json con la infomacion de Z03Permisos.
     * @return Regresa la lista de instancias actualizadas.
     * @throws ModelException para indicar algun problema al procesar el
     * comando.
     */
    protected GridResponse<? extends BaseDAO> applyChangesFromJSON(JSONArray jsonArray)
            throws ModelException {
        return null;
    }

    /**
     * Definición abstracta de Método que procesa las peticiones del grid w2ui
     * para borrar un conjunto de registros.
     *
     * @param jsonObj Objeto json con la petición del grid.
     * @throws ModelException para indicar algun problema al procesar el
     * comando.
     */
    protected void deleteAll(JSONObject jsonObj) throws ModelException {
    }

    /**
     * Manejador de excepciones generadas por errores para informar al cliente.
     *
     * @param ex Excepción lanzada al momento del error.
     * @return InfoResponse Detalle del error.
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public InfoResponse handleServerErrors(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        if (ex.getMessage().endsWith("could not execute statement")) {
            return new InfoResponse("Existe Problema en Base de Datos, <br>Verificarlo con el Administrador");
        } else {
            return new InfoResponse(ex.getMessage());
        }
    }

}
