package com.racheldev.orders.type.grid;

import java.util.Arrays;
import java.util.List;

/**
 * Un POJO que contiene los datos a desplegar en un grid.
 *
 * @param <T> Tipo de datos que manejará el grid.
 */
public class GridResponse<T> {

    /**
     * El atributo status.
     */
    private final String status;

    private final Long total;

    /**
     * Lista con los datos del Grid.
     */
    private final List<T> records;

    /**
     * Genera una nueva instancia de la clase InfoResponse en caso de éxito, sin
     * elementos.
     */
    public GridResponse() {
        this.status = "success";
        this.total = 0l;
        this.records = Arrays.asList();
    }

    /**
     * Genera una nueva instancia de la clase InfoResponse en caso de éxito.
     *
     * @param records Lista de registros.
     */
    public GridResponse(List<T> records) {
        this.status = "success";
        this.total = ((Integer) records.size()).longValue();
        this.records = records;
    }

    /**
     * Genera una nueva instancia de la clase InfoResponse en caso de éxito.
     *
     * @param records Lista de registros en la página.
     * @param total Total de registros.
     */
    public GridResponse(List<T> records, Long total) {
        this.status = "success";
        this.total = total;
        this.records = records;
    }

    /**
     * Genera una nueva instancia de la clase InfoResponse en caso de éxito.
     *
     * @param record Registro único.
     */
    public GridResponse(T record) {
        this.status = "success";
        this.total = 1l;
        this.records = Arrays.asList(record);
    }

    /**
     * Método de acceso al atributo status.
     *
     * @return El valor del atributo status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Método de acceso al atributo total.
     *
     * @return El valor del atributo total.
     */
    public Long getTotal() {
        return total;
    }

    /**
     * Método de acceso al atributo records.
     *
     * @return El valor del atributo records.
     */
    public List<T> getRecords() {
        return records;
    }

}

