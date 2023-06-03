package com.racheldev.orders.entity;

import java.math.BigDecimal;

public class ProductoOrdenDTO extends BaseDAO {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String codProducto;
	
	private String producto;
	
	private Integer cantidad;
	
	private BigDecimal precioUnitario;
	
	private BigDecimal subTotal;
	
	private BigDecimal iva;
	
	private BigDecimal total;
	
	private Integer existencia;
	
	public Integer getExistencia() {
		return existencia;
	}

	public void setExistencia(Integer existencia) {
		this.existencia = existencia;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodProducto() {
		return codProducto;
	}

	public void setCodProducto(String codProducto) {
		this.codProducto = codProducto;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	

	public ProductoOrdenDTO(Long id, String codProducto, String producto, Integer cantidad, BigDecimal precioUnitario,
			BigDecimal subTotal, BigDecimal iva, BigDecimal total, Integer existencia) {
		super();
		this.id = id;
		this.codProducto = codProducto;
		this.producto = producto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.subTotal = subTotal;
		this.iva = iva;
		this.total = total;
		this.existencia = existencia;
	}


}
