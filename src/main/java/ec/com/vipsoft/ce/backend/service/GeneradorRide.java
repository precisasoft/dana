package ec.com.vipsoft.ce.backend.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import ec.com.vipsoft.ce.utils.UtilClaveAcceso;

@Stateless
public class GeneradorRide {
	@Inject
	private UtilClaveAcceso utilClaveAcceso;
	public byte[] obtenerRide(String claveAcceso){
		byte[] retorno=null;
		String tipoComprobante=utilClaveAcceso.obtenerTipoDocumento(claveAcceso);
		//
		
		
		
		switch (tipoComprobante) {
		case "01":
			
			break;
		case "04":
			break;
		case "05":
			break;
		case "06":
			break;
		case "07":
			break;			
		}
		
		
		return retorno;
		
	}

}
