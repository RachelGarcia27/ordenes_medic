package com.racheldev.orders.service;

import java.util.List;

import com.racheldev.orders.entity.Cliente;
import com.racheldev.orders.type.exception.ModelException;

public interface ClienteService {

    /**
     * Crea un nueva Producto.
     *
     * @param dto, DTO con la información de la Cliente por crear.
     * @return la Cliente creada.
     */
    public Cliente create(Cliente dto);

    /**
     * Borra una Producto.
     *
     * @param id, El id del B01Itinerarios que se desea borrar de la BD.
     * @return Una copia de la entidad Producto borradoa de la BD.
     * @throws ModelException la excepción del Producto que no se encontró.
     */
    public Cliente delete(Long id) throws ModelException;

    /**
     * Actualiza la información de una Producto. Respeta el valor que ya se tenían los campos
     * llave.
     *
     * @param dto, El objeto Cliente DTO original.
     * @return Una copia de la entidad Cliente actualizada en la BD.
     * @throws ModelException para indicar que no se localizó el registro.
     */
    public Cliente update(Cliente dto) throws ModelException;

    /**
     * Busca una Cliente por su id.
     *
     * @param id El id de la Producto que buscamos.
     * @return La Producto encontrada.
     */
    public Cliente findById(Long id);
	
    /**
     * Solicita al repositorio una lista de todos los Cliente almacenados en
     * la BD.
     *
     * @param searchSpec Especificación de búsqueda.
     * @param sort Criterio de Productoamiento.
     * @return Una lista de Cliente encontrados.
     */
    public List<Cliente> findAll();

}