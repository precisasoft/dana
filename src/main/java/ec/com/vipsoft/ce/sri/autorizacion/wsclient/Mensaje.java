package ec.com.vipsoft.ce.sri.autorizacion.wsclient;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "indentificador",
    "mensaje",
    "tipo"
})
public class Mensaje implements Serializable{
	private static final long serialVersionUID = 2405500375461938383L;
	@XmlElement(required=true)
	private String identificador;
	@XmlElement(required=true)
	private String mensaje;
	@XmlElement(required=true)
	private String tipo;
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
