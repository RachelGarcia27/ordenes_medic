package com.racheldev.orders.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;

import com.racheldev.orders.util.JsonUtils;

@Entity
//@Table(name = "productos_orden", schema = "ordenes")
@Table(name = "productos_orden")
public class ProductoOrden extends BaseDAO {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    @Column(name = "cod_producto")
    private String codProducto;
    
    @Column(name = "numero_orden")
    private Long numeroOrden;
    
    private Integer cantidad;

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

	public Long getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(Long numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
    
    /**
     * El método que ejecuta la acción update.
     * @param src Una instancia de la clase Orden.
     */
    public void update(final ProductoOrden src) {
    	this.codProducto = src.getCodProducto();
    	this.numeroOrden = src.getNumeroOrden();
    	this.cantidad = src.getCantidad();
    }
    
    /**
    *
    * Constructor de un Orden para procesar información de los formularios
    * recibido en un objeto json desde las vistas.
    *
    * @param jsonObj json con la informacion de un Orden
    */
   public ProductoOrden(JSONObject jsonObj) {
	   try {
	       if (!jsonObj.isNull("id")) {
		   this.id = ((Integer) jsonObj.getInt("id")).longValue();
	   }
	   this.codProducto = JsonUtils.parseString(jsonObj, "codProducto");
	   this.numeroOrden = JsonUtils.parseLong(jsonObj, "numeroOrden");
	   this.cantidad = JsonUtils.parseInteger(jsonObj, "cantidad");
	   }catch(Exception e) {
		   e.getStackTrace();
	   }
   }

	public ProductoOrden() {
		super();
	}
}
