package ec.com.vipsoft.web;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ec.com.vipsoft.ce.sri.autorizacion.wsclient.Autorizacion;

public abstract class BaseReporteRide implements Serializable{


	public BaseReporteRide() {
		super();
	}
	private static final long serialVersionUID = -7347880560168476092L;
	protected Map<String,Object> mapaReportes;
	protected JAXBContext jaxbContext;
	protected Unmarshaller unmarshaller;
	protected String elComprobante;
	protected StringReader reader;
	protected abstract void iniciarJAXBContext();
	protected abstract void procesarDetalles();
	public void prepararMapa(Autorizacion autorizacion){
		mapaReportes.put("numeroAutorizacion", autorizacion.getNumeroAutorizacion());
		mapaReportes.put("fechaHoraAutorizacion", autorizacion.getFechaAutorizacion());
		mapaReportes.put("ambiente", autorizacion.getAmbiente());
		mapaReportes.put("rucEmisor", autorizacion.getNumeroAutorizacion().substring(14,27));
		elComprobante=autorizacion.getComprobante();
		iniciarJAXBContext();
		try {
			unmarshaller=jaxbContext.createUnmarshaller();
			reader=new StringReader(elComprobante);
			procesarDetalles();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
