package com.racheldev.orders.service;

import java.util.List;

import com.racheldev.orders.entity.Producto;
import com.racheldev.orders.type.exception.ModelException;

public interface ProductoService {

    /**
     * Crea un nueva Producto.
     *
     * @param dto, DTO con la información de la Producto por crear.
     * @return la Producto creada.
     */
    public Producto create(Producto dto);

    /**
     * Borra una Producto.
     *
     * @param id, El id del B01Itinerarios que se desea borrar de la BD.
     * @return Una copia de la entidad Producto borradoa de la BD.
     * @throws ModelException la excepción del Producto que no se encontró.
     */
    public Producto delete(Long id) throws ModelException;

    /**
     * Actualiza la información de una Producto. Respeta el valor que ya se tenían los campos
     * llave.
     *
     * @param dto, El objeto Producto DTO original.
     * @return Una copia de la entidad Producto actualizada en la BD.
     * @throws ModelException para indicar que no se localizó el registro.
     */
    public Producto update(Producto dto) throws ModelException;

    /**
     * Busca una Producto por su id.
     *
     * @param id El id de la Producto que buscamos.
     * @return La Producto encontrada.
     */
    public Producto findById(Long id);
	
    /**
     * Solicita al repositorio una lista de todos los Producto almacenados en
     * la BD.
     *
     * @param searchSpec Especificación de búsqueda.
     * @param sort Criterio de Productoamiento.
     * @return Una lista de Producto encontrados.
     */
    public List<Producto> findAll();

}
