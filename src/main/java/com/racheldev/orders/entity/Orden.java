package com.racheldev.orders.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.json.JSONObject;

import com.racheldev.orders.util.JsonUtils;

@Entity
//@Table(name = "orden", schema = "ordenes")
@Table(name = "orden")
public class Orden extends BaseDAO {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    @Column(name = "numero_orden")
    private Long numeroOrden;
    
    @Column(name = "cve_cliente")
    private String cveCliente;
    
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Column(name = "subtotal")
    private BigDecimal subTotal;
    private BigDecimal iva;
    private BigDecimal total;
    private String estatus;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getCveCliente() {
		return cveCliente;
	}
	public void setCveCliente(String cveCliente) {
		this.cveCliente = cveCliente;
	}
	public Long getNumeroOrden() {
		return numeroOrden;
	}
	public void setNumeroOrden(Long numeroOrden) {
		this.numeroOrden = numeroOrden;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

    /**
     * El método que ejecuta la acción update.
     * @param src Una instancia de la clase Orden.
     */
    public void update(final Orden src) {
    	this.numeroOrden = src.getNumeroOrden();
    	this.cveCliente = src.getCveCliente();
    	this.fecha = src.getFecha();
        this.subTotal = src.getSubTotal();
        this.iva = src.getIva();
        this.total = src.getTotal();
        this.estatus = src.getEstatus();
    }
    
    /**
    *
    * Constructor de un Orden para procesar información de los formularios
    * recibido en un objeto json desde las vistas.
    *
    * @param jsonObj json con la informacion de un Orden
    */
   public Orden(JSONObject jsonObj) {
	   try {
	       if (!jsonObj.isNull("id")) {
		   this.id = ((Integer) jsonObj.getInt("id")).longValue();
	   }
	   this.numeroOrden = jsonObj.getLong("numeroOrden");
	   this.cveCliente = JsonUtils.parseString(jsonObj, "cveCliente");
	   this.fecha = JsonUtils.parseDate(jsonObj, "fecha");
	   this.subTotal = JsonUtils.parseBigDecimal(jsonObj, "subTotal");
	   this.iva = JsonUtils.parseBigDecimal(jsonObj, "iva");
	   this.total = JsonUtils.parseBigDecimal(jsonObj, "total");
	   this.estatus = JsonUtils.parseString(jsonObj, "estatus");
	   }catch(Exception e) {
		   e.getStackTrace();
	   }
   }

	public Orden() {
		super();
	}
}
