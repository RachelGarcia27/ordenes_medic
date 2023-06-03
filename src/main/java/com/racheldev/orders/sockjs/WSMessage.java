package com.racheldev.orders.sockjs;

/**
 * Clase que define el cuerpo de un mensaje de websockets.
 *
 * @author rpacheco
 */
public class WSMessage {

    /**
     * Tipo del mensaje
     */
    private Integer type;
    /**
     * Estrategia de envio para el mensaje
     */
    private Integer strategy;
    /**
     * Texto del mensaje
     */
    private String text;
    /**
     * Destinatarios del mensaje
     */
    private String target;
    /**
     * Remitente del mensaje
     */
    private String origin;

    /**
     * Método de Acceso del Atributo type.
     *
     * @return El atributo type.
     */
    public Integer getType() {
        return type;
    }

    /**
     * Método de Actualización del Atributo type.
     *
     * @param type Valor de inicialización del atributo.
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * Método de Acceso del Atributo strategy.
     *
     * @return El atributo strategy
     */
    public Integer getStrategy() {
        return strategy;
    }

    /**
     * Método de Actualización del Atributo strategy.
     *
     * @param strategy Valor de inicialización del atributo.
     */
    public void setStrategy(Integer strategy) {
        this.strategy = strategy;
    }

    /**
     * Método de Acceso del Atributo text.
     *
     * @return El atributo text
     */
    public String getText() {
        return text;
    }

    /**
     * Método de Actualización del Atributo text.
     *
     * @param text Valor de inicialización del atributo.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Método de Acceso del Atributo target.
     *
     * @return El valor del atributo target
     */
    public String getTarget() {
        return target;
    }

    /**
     * Método de Actualización del Atributo target.
     *
     * @param target Valor de inicialización del atributo.
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Método de Acceso del Atributo origin.
     *
     * @return el valor del atributo origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Método de Actualización del Atributo origin.
     *
     * @param origin Valor de inicialización del atributo.
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Método encargado ejecutar la conversión a texto del objeto.
     *
     * @return Convierte las instancias a Texto.
     */
    @Override
    public String toString() {
        return "WSMessage{" + "type=" + type + ", strategy=" + strategy
                + ", text=" + text + ", target=" + target
                + ", origin=" + origin + '}';
    }

}

