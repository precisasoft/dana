package ec.com.vipsoft.ce.backend.service;

import java.io.Serializable;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import net.sf.jasperreports.engine.JasperReport;

@Singleton
@Startup
public class ContenedorReportesRide implements Serializable{

	private static final long serialVersionUID = 7191867833399382806L;
	private JasperReport rideFactura;
	private JasperReport rideRetencion;
	private JasperReport rideNotaCredito;
	private JasperReport rideNotaDebito;
	private JasperReport rideGuiaRemision;
	public JasperReport getRideFactura() {
		return rideFactura;
	}
	public void setRideFactura(JasperReport rideFactura) {
		this.rideFactura = rideFactura;
	}
	public JasperReport getRideRetencion() {
		return rideRetencion;
	}
	public void setRideRetencion(JasperReport rideRetencion) {
		this.rideRetencion = rideRetencion;
	}
	public JasperReport getRideNotaCredito() {
		return rideNotaCredito;
	}
	public void setRideNotaCredito(JasperReport rideNotaCredito) {
		this.rideNotaCredito = rideNotaCredito;
	}
	public JasperReport getRideNotaDebito() {
		return rideNotaDebito;
	}
	public void setRideNotaDebito(JasperReport rideNotaDebito) {
		this.rideNotaDebito = rideNotaDebito;
	}
	public JasperReport getRideGuiaRemision() {
		return rideGuiaRemision;
	}
	public void setRideGuiaRemision(JasperReport rideGuiaRemision) {
		this.rideGuiaRemision = rideGuiaRemision;
	}
	
}
