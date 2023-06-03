package com.racheldev.orders.type.grid;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Un POJO que contiene los estatus de una acción y una Lista de mensajes. Se
 * utiliza principalmente como un DTO para la capa de presentación
 */
public class InfoResponse {

    /**
     * El atributo status.
     */
    private String status;

    /**
     * El atributo message.
     */
    private String message;

    /**
     * Genera una nueva instancia de la clase InfoResponse en caso de éxito.
     */
    public InfoResponse() {
        this.status = "success";
        this.message = null;
    }

    /**
     * Genera una nueva instancia de la clase InfoResponse.
     *
     * @param error el parámetro message
     */
    public InfoResponse(String error) {
        this.status = "error";
        this.message = error;
    }

    /**
     * Genera una nueva instancia de InfoResponse inicializando el estatus y el
     * mensaje.
     *
     * @param status Estatus a reportar.
     * @param message Mensaje a reportar.
     */
    public InfoResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Método de Acceso del atributo status.
     *
     * @return El atributo status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Método modificador del atributo status.
     *
     * @param status Valor que se desea asignar al atributo status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Método de Acceso del atributo message.
     *
     * @return El atributo message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Método modificador del atributo message.
     *
     * @param message Valor que se desea asignar al atributo message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Método que para manejo de mensaje en caso de exito.
     *
     * @param message Mensaje para el cliente.
     * @return La instancia de esta clase.
     */
    public InfoResponse OK(String message) {
        this.status = "success";
        this.message = message;
        return this;
    }

    /**
     * Método que para manejo de mensaje en caso de error.
     *
     * @param message Mensaje para el cliente.
     * @return La instancia de esta clase.
     */
    public InfoResponse ERROR(String message) {
        this.status = "error";
        this.message = message;
        return this;
    }

    /**
     * Método genérico para la conversión de objetos a String.
     *
     * @return Representación del Objeto en formato String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
