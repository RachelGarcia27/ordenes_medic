package com.racheldev.orders.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;

@Entity
//@Table(name = "cliente", schema = "ordenes")
@Table(name = "cliente")
public class Cliente extends BaseDAO {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    @Column(name = "cve_cliente")
	private String cveCliente;
    
    @Column(name = "ape_pat")
	private String apellidoPaterno;
    
    @Column(name = "ape_mat")
	private String apellidoMaterno;
    
	private String nombre;
	private String telefono;
	private String direccion;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCveCliente() {
		return cveCliente;
	}
	public void setCveCliente(String cveCliente) {
		this.cveCliente = cveCliente;
	}
	public String getApellidoPaterno() {
		return apellidoPaterno;
	}
	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}
	public String getApellidoMaterno() {
		return apellidoMaterno;
	}
	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
    /**
     * El método que ejecuta la acción update.
     * @param src Una instancia de la clase Cliente.
     */
    public void update(final Cliente src) {
        this.cveCliente = src.getCveCliente();
        this.apellidoPaterno = src.getApellidoPaterno();
        this.apellidoMaterno = src.getApellidoMaterno();
        this.nombre = src.getNombre();
        this.telefono = src.getTelefono();
        this.direccion = src.getDireccion();
    }
    
    /**
    *
    * Constructor de un Cliente para procesar información de los formularios
    * recibido en un objeto json desde las vistas.
    *
    * @param jsonObj json con la informacion de un Cliente
    */
   public Cliente(JSONObject jsonObj) {
	   try {
       if (!jsonObj.isNull("id")) {
           this.id = ((Integer) jsonObj.getInt("id")).longValue();
       }
       this.cveCliente = jsonObj.getString("cveCliente");
       this.apellidoPaterno = jsonObj.getString("apellidoPaterno");
       this.apellidoMaterno = jsonObj.getString("apellidoMaterno");
       this.nombre = jsonObj.getString("nombre");
       this.telefono = jsonObj.getString("telefono");
       this.direccion = jsonObj.getString("direccion");
	   }catch(Exception e) {
		   e.getStackTrace();
	   }
   }

	public Cliente() {
		super();
	}
}
