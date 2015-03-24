package ec.com.vipsoft.erp.abinadi.dominio;

import java.io.Serializable;
import java.lang.Long;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: BienEconomico
 *
 */
@Entity

public class BienEconomico implements Serializable {

	   
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private static final long serialVersionUID = 1L;
	@NotNull
	private String codigo;
	private String codigoIce;
	private String codigoIva;
	@NotNull
	private String descripcion;
	@NotNull
	@ManyToOne
	private Entidad entidad;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getCodigoIce() {
		return codigoIce;
	}
	public void setCodigoIce(String codigoIce) {
		this.codigoIce = codigoIce;
	}
	public String getCodigoIva() {
		return codigoIva;
	}
	public void setCodigoIva(String codigoIva) {
		this.codigoIva = codigoIva;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Entidad getEntidad() {
		return entidad;
	}
	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}
	public BienEconomico() {
		super();
	}   
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
   
}
