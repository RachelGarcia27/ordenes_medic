package com.racheldev.orders.repository;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Repositorio Básico con funcionalidad compartida por el resto de los
 * repositorios.
 *
 * @param <T> Tipo Al que se aplica la funcionalidad.
 * @param <ID> Tipo del Id de la Clase.
 */
@NoRepositoryBean
public abstract interface AbstractInterface<T, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
