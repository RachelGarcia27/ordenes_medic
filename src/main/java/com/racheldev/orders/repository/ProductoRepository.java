package com.racheldev.orders.repository;

import java.util.List;

import com.racheldev.orders.entity.Producto;

public interface ProductoRepository extends AbstractInterface<Producto, Long> {

	public List<Producto> findByCodProductoIn(List<String> listaCodProducto);
	
	public Producto findByCodProducto(String listaCodProducto);
	
}
