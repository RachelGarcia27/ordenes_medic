package com.racheldev.orders.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;

@Entity
//@Table(name = "producto", schema = "ordenes")
@Table(name = "producto")
public class Producto extends BaseDAO {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @Column(name = "cod_producto")
	private String codProducto;
	
	private String nombre;
	
	private Integer existencia;
	
	@Column(name = "precio_compra")
	private BigDecimal precioCompra;
	
	@Column(name = "precio_venta")
	private BigDecimal precioVenta;
	
	@Column(name = "cve_proveedor")
	private String cveProveedor;

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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getExistencia() {
		return existencia;
	}

	public void setExistencia(Integer existencia) {
		this.existencia = existencia;
	}

	public BigDecimal getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(BigDecimal precioCompra) {
		this.precioCompra = precioCompra;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public String getCveProveedor() {
		return cveProveedor;
	}

	public void setCveProveedor(String cveProveedor) {
		this.cveProveedor = cveProveedor;
	}

    /**
     * El método que ejecuta la acción update.
     * @param src Una instancia de la clase Producto.
     */
    public void update(final Producto src) {
        this.codProducto = src.getCodProducto();
        this.nombre = src.getNombre();
        this.existencia = src.getExistencia();
        this.precioCompra = src.getPrecioCompra();
        this.precioVenta = src.getPrecioVenta();
        this.cveProveedor = src.getCveProveedor();
    }
    
    /**
    *
    * Constructor de un Producto para procesar información de los formularios
    * recibido en un objeto json desde las vistas.
    *
    * @param jsonObj json con la informacion de un Producto
    */
   public Producto(JSONObject jsonObj) {
	   try {
       if (!jsonObj.isNull("id")) {
           this.id = ((Integer) jsonObj.getInt("id")).longValue();
       }
       this.codProducto = jsonObj.getString("codProducto");
       this.nombre = jsonObj.getString("nombre");
       this.existencia = jsonObj.getInt("existencia");
       this.nombre = jsonObj.getString("nombre");
       this.precioCompra = new BigDecimal(jsonObj.getDouble("precioCompra"));
       this.precioVenta = new BigDecimal(jsonObj.getDouble("precioVenta"));
       this.cveProveedor = jsonObj.getString("cveProveedor");
	   }catch(Exception e) {
		   e.getStackTrace();
	   }
   }

	public Producto() {
		super();
	}
   
   
}
