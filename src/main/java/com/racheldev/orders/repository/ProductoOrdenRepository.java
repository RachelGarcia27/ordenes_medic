package com.racheldev.orders.repository;

import java.util.List;

import com.racheldev.orders.entity.ProductoOrden;

public interface ProductoOrdenRepository extends AbstractInterface<ProductoOrden, Long> {
	
	public List<ProductoOrden> findByNumeroOrden(Long numeroOrden);

}
