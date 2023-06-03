package com.racheldev.orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.racheldev.orders.entity.Orden;

public interface OrdenRepository extends AbstractInterface<Orden, Long> {
    
	@Query(value = "SELECT ISNULL(MAX(numero_orden),0) FROM orden", nativeQuery = true)
	public Long findMaxByNumeroOrden();
	
	public Orden findByNumeroOrden(Long numeroOrden);
	
	public List<Orden> findByEstatus(String estatus);
}
