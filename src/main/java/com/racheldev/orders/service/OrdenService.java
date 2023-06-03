package com.racheldev.orders.service;

import com.racheldev.orders.entity.Orden;
import com.racheldev.orders.type.exception.ModelException;

import java.util.List;

public interface OrdenService {

    /**
     * Crea un nueva Orden.
     *
     * @param dto, DTO con la información de la Orden por crear.
     * @return la Orden creada.
     */
    public Orden create(Orden dto);

    /**
     * Borra una Orden.
     *
     * @param id, El id del Orden que se desea borrar de la BD.
     * @return Una copia de la entidad Orden borradoa de la BD.
     * @throws ModelException la excepción del Orden que no se encontró.
     */
    public Orden delete(Long id) throws ModelException;

    /**
     * Actualiza la información de una Orden. Respeta el valor que ya se tenían los campos
     * llave.
     *
     * @param dto, El objeto Orden DTO original.
     * @return Una copia de la entidad Orden actualizada en la BD.
     * @throws ModelException para indicar que no se localizó el registro.
     */
    public Orden update(Orden dto) throws ModelException;

    /**
     * Busca una Orden por su id.
     *
     * @param id El id de la Orden que buscamos.
     * @return La Orden encontrada.
     */
    public Orden findById(Long id);
	
    /**
     * Solicita al repositorio una lista de todos los Orden almacenados en
     * la BD.
     *
     * @param searchSpec Especificación de búsqueda.
     * @param sort Criterio de ordenamiento.
     * @return Una lista de Orden encontrados.
     */
    public List<Orden> findAll();
    
    public Long findMaxByNumeroOrden();
    
    public Orden enviarOrden(Long numeroOrden) throws ModelException;

}
