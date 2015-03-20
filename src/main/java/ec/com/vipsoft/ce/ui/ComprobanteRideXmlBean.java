package ec.com.vipsoft.ce.ui;

import java.io.Serializable;

import com.vaadin.ui.Link;

public class ComprobanteRideXmlBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6882833204601880092L;
	private String tipo;
	private String fechaAprobacion;
	private Link autorizacion;
	private Link  claveAcceso;
	private String numeroDocumento;
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getFechaAprobacion() {
		return fechaAprobacion;
	}
	public void setFechaAprobacion(String fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	public Link getAutorizacion() {
		return autorizacion;
	}
	public void setAutorizacion(Link autorizacion) {
		this.autorizacion = autorizacion;
	}
	public Link getClaveAcceso() {
		return claveAcceso;
	}
	public void setClaveAcceso(Link claveAcceso) {
		this.claveAcceso = claveAcceso;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	
}
