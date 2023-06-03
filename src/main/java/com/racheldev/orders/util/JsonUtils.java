package com.racheldev.orders.util;

import com.racheldev.orders.sockjs.WSMessage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que facilita la conversión de mensajes JSON a WebSockets y otros tipos
 * de datos.
 *
 */
public class JsonUtils {

    /**
     * La propiedad LOGGER.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * La propiedad DATE_FORMAT.
     */
    public static String DATE_FORMAT = "dd/MM/yyyy";

    /**
     * La propiedad TIME_FORMAT.
     */
    public static String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Método que convierte un texto con JSON en un mensaje de WebSockets.
     *
     * @param jsonString Mensaje a convertir.
     * @return Mensaje de WebSockets.
     */
    public static WSMessage getMsgObject(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            WSMessage msgObject = mapper.readValue(jsonString, WSMessage.class);
            return msgObject;
        } catch (JsonGenerationException e) {
            LOGGER.info("JsonGenerationException: " + e.toString());
        } catch (JsonMappingException e) {
            LOGGER.info("JsonMappingException: " + e.toString());
        } catch (IOException e) {
            LOGGER.info("IOException: " + e.toString());
        }
        return null;
    }

    /**
     * Valida la conversión de valores Character a partir de un objeto.
     *
     * @param obj Objeto a procesar
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     */
    public static Character parseCharacter(Object obj) {
        if (obj instanceof Integer || obj instanceof Long) {
            Integer intToUse = ((Number) obj).intValue();
            return intToUse.toString().charAt(0);
        } else if (obj instanceof Float || obj instanceof Double) {
            Double doubleToUse = ((Number) obj).doubleValue();
            return doubleToUse.toString().charAt(0);
        } else if (obj instanceof String) {
            return ((String) obj).charAt(0);
        } else if (obj instanceof Character) {
            return (Character) obj;
        }
        return null;
    }

    /**
     * Valida la conversión de valores tipo Character a partir de diversos tipos
     * de datos.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     * @throws JSONException 
     */
    public static Character parseCharacter(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return parseCharacter(json.get(key));
        }
        return null;
    }

    /**
     * Valida la conversión de valores String a partir de un objeto.
     *
     * @param obj Objeto a procesar
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     */
    public static String parseString(Object obj) {
        if (obj instanceof Byte || obj instanceof Short
                || obj instanceof Integer || obj instanceof Long) {
            Integer intToUse = ((Number) obj).intValue();
            return intToUse.toString();
        } else if (obj instanceof Float || obj instanceof Double) {
            Double doubleToUse = ((Number) obj).doubleValue();
            return doubleToUse.toString();
        } else if (obj instanceof String) {
            return (String) obj;
        }
        return null;
    }

    /**
     * Valida la conversión de valores String a partir de diversos tipos de
     * datos.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     * @throws JSONException 
     */
    public static String parseString(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return parseString(json.get(key));
        }
        return null;
    }

    /**
     * Valida la conversión de valores Short a partir de un objeto.
     *
     * @param obj Objeto a procesar
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     */
    public static Short parseShort(Object obj) {
        if (obj instanceof Byte || obj instanceof Short
                || obj instanceof Integer || obj instanceof Long) {
            return ((Number) obj).shortValue();
        } else if (obj instanceof Float || obj instanceof Double) {
            Double doubleToUse = ((Number) obj).doubleValue();
            return new Long(Math.round(doubleToUse)).shortValue();
        } else if (obj instanceof String) {
            try {
                return Short.parseShort((String) obj);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    /**
     * Valida la conversión de valores Short a partir de diversos tipos de
     * datos.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     * @throws JSONException 
     */
    public static Short parseShort(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return parseShort(json.get(key));
        }
        return null;
    }

    /**
     * Valida la conversión de valores Integer a partir de un objeto.
     *
     * @param obj Objeto a procesar
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     */
    public static Integer parseInteger(Object obj) {
        if (obj instanceof Byte || obj instanceof Short
                || obj instanceof Integer || obj instanceof Long) {
            Integer intToUse = ((Number) obj).intValue();
            return intToUse;
        } else if (obj instanceof Float || obj instanceof Double) {
            Double doubleToUse = ((Number) obj).doubleValue();
            return new Long(Math.round(doubleToUse)).intValue();
        } else if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    /**
     * Valida la conversión de valores Integer a partir de diversos tipos de
     * datos.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     * @throws JSONException 
     */
    public static Integer parseInteger(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return parseInteger(json.get(key));
        }
        return null;
    }

    /**
     * Valida la conversión de valores Long a partir de un objeto.
     *
     * @param obj Objeto a procesar
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     */
    public static Long parseLong(Object obj) {
        if (obj instanceof Byte || obj instanceof Short
                || obj instanceof Integer || obj instanceof Long) {
            Long intToUse = ((Number) obj).longValue();
            return intToUse;
        } else if (obj instanceof Float || obj instanceof Double) {
            Double doubleToUse = ((Number) obj).doubleValue();
            return Math.round(doubleToUse);
        } else if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    /**
     * Valida la conversión de valores Long a partir de diversos tipos de datos.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     * @throws JSONException 
     */
    public static Long parseLong(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return parseLong(json.get(key));
        }
        return null;
    }

    /**
     * Valida la conversión de valores Long a partir de un objeto.
     *
     * @param obj Objeto a procesar
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     */
    public static BigInteger parseBigInteger(Object obj) {
        if (obj instanceof Byte || obj instanceof Short
                || obj instanceof Integer || obj instanceof Long) {
            Long intToUse = ((Number) obj).longValue();
            return BigInteger.valueOf(intToUse);
        } else if (obj instanceof Float || obj instanceof Double) {
            Double doubleToUse = ((Number) obj).doubleValue();
            return BigInteger.valueOf(Math.round(doubleToUse));
        } else if (obj instanceof String) {
            try {
                return new BigInteger((String) obj);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    /**
     * Valida la conversión de valores BigInteger a partir de diversos tipos de
     * datos.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     * @throws JSONException 
     */
    public static BigInteger parseBigInteger(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return parseBigInteger(json.get(key));
        }
        return null;
    }

    /**
     * Valida la conversión de valores Float a partir de un objeto.
     *
     * @param obj Objeto a procesar
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     */
    public static Float parseFloat(Object obj) {
        if (obj instanceof Byte || obj instanceof Short
                || obj instanceof Integer || obj instanceof Long) {
            Double doubleToUse = ((Number) obj).doubleValue();
            return new Float(doubleToUse);
        } else if (obj instanceof Float || obj instanceof Double) {
            Float floatToUse = ((Number) obj).floatValue();
            return floatToUse;
        } else if (obj instanceof String) {
            try {
                return Float.parseFloat((String) obj);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    /**
     * Valida la conversión de valores Float a partir de diversos tipos de
     * datos.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     * @throws JSONException 
     */
    public static Float parseFloat(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return parseFloat(json.get(key));
        }
        return null;
    }

    /**
     * Valida la conversión de valores Double a partir de un objeto.
     *
     * @param obj Objeto a procesar
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     */
    public static Double parseDouble(Object obj) {
        if (obj instanceof Byte || obj instanceof Short
                || obj instanceof Integer || obj instanceof Long) {
            Long longToUse = ((Number) obj).longValue();
            return new Double(longToUse);
        } else if (obj instanceof Float || obj instanceof Double) {
            Double doubleToUse = ((Number) obj).doubleValue();
            return doubleToUse;
        } else if (obj instanceof String) {
            try {
                return Double.parseDouble((String) obj);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    /**
     * Valida la conversión de valores Double a partir de diversos tipos de
     * datos.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     * @throws JSONException 
     */
    public static Double parseDouble(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return parseDouble(json.get(key));
        }
        return null;
    }

    /**
     * Valida la conversión de valores BigDecimal a partir de un objeto.
     *
     * @param obj Objeto a procesar
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     */
    public static BigDecimal parseBigDecimal(Object obj) {
        if (obj instanceof Byte || obj instanceof Short
                || obj instanceof Integer || obj instanceof Long) {
            Long longToUse = ((Number) obj).longValue();
            return new BigDecimal(longToUse);
        } else if (obj instanceof Float || obj instanceof Double) {
            return new BigDecimal(((Number) obj).doubleValue());
        } else if (obj instanceof String) {
            try {
                return new BigDecimal((String) obj);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    /**
     * Valida la conversión de valores BigDecimal a partir de diversos tipos de
     * datos.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     * @throws JSONException 
     */
    public static BigDecimal parseBigDecimal(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return parseBigDecimal(json.get(key));
        }
        return null;
    }

    /**
     * Valida la conversión de valores Date a partir de una fecha.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     * @throws JSONException 
     */
    public static Date parseDate(JSONObject json, String key) throws JSONException {
        Date dateToUse = null;
        String dateString = parseString(json, key);
        if (dateString != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            try {
                dateToUse = formatter.parse(dateString);
            } catch (ParseException ex) {
            }
        }
        return dateToUse;
    }

    /**
     * Valida la conversión de valores Date a partir de un formato de Fecha y
     * Hora.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     * @throws JSONException 
     */
    public static Date parseTimestamp(JSONObject json, String key) throws JSONException {
        Date dateToUse = null;
        String dateString = parseString(json, key);
        if (dateString != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            try {
                dateToUse = formatter.parse(dateString);
            } catch (ParseException ex) {
            }
        }
        return dateToUse;
    }

    /**
     * Valida la conversión de valores Date a partir de una fecha con formato
     * dado.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @param format Formato en que se recibe la fecha.
     * @return Valor recuperado o NULL si no fue posible recuperarlo.
     * @throws JSONException 
     */
    public static Date parseDate(JSONObject json, String key, String format) throws JSONException {
        Date dateToUse = null;
        String dateString = parseString(json, key);
        if (dateString != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            try {
                dateToUse = formatter.parse(dateString);
            } catch (ParseException ex) {
            }
        }
        return dateToUse;
    }

    /**
     * Valida la conversión de un arreglo de IDs tipo Long.
     *
     * @param json Objeto que contiene el valor recuperar.
     * @param key Llave del objeto.
     * @return Arreglo de IDs.
     * @throws JSONException 
     */
    public static Long[] parseIdsArray(JSONObject json, String key) throws JSONException {
        Long[] idsArray = new Long[0];
        if (json.has(key) && json.get(key) instanceof JSONArray) {
            JSONArray idsJsonArray = json.getJSONArray(key);
            idsArray = new Long[idsJsonArray.length()];
            for (int i = 0; i < idsJsonArray.length(); i++) {
                idsArray[i] = parseLong(idsJsonArray.get(i));
            }
        }
        return idsArray;
    }
}
