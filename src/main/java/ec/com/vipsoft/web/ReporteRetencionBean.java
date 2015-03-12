package ec.com.vipsoft.web;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import ec.com.vipsoft.sri.comprobanteRetencion._v1_0.ComprobanteRetencion;
import ec.com.vipsoft.sri.comprobanteRetencion._v1_0.ComprobanteRetencion.InfoAdicional.CampoAdicional;



public class ReporteRetencionBean extends BaseReporteRide{

	private static final long serialVersionUID = 7211389322301818477L;

	@Override
	protected void iniciarJAXBContext() {
		try {
			jaxbContext=JAXBContext.newInstance(ComprobanteRetencion.class);
		} catch (JAXBException e) {		
			e.printStackTrace();
		}		
	}

	@Override
	protected void procesarDetalles() {
		try {
			ComprobanteRetencion comprobante=(ComprobanteRetencion)unmarshaller.unmarshal(reader);
			String _tipoEmision=comprobante.getInfoTributaria().getTipoEmision();
			if(_tipoEmision.equalsIgnoreCase("1")){
				mapaReportes.put("tipoEmision", "NORMAL");
			}
			if(_tipoEmision.equalsIgnoreCase("2")){
				mapaReportes.put("tipoEmision", "CONTINGENCIA");
			}
			mapaReportes.put("claveAcceso", comprobante.getInfoTributaria().getClaveAcceso());
			mapaReportes.put("razonSocialCliente", comprobante.getInfoCompRetencion().getRazonSocialSujetoRetenido());
			mapaReportes.put("identificacionCliente", comprobante.getInfoCompRetencion().getIdentificacionSujetoRetenido());
			mapaReportes.put("fechaEmision", comprobante.getInfoCompRetencion().getFechaEmision());
		//	mapaReportes.put("r")
			int i=1;
			for(CampoAdicional c:comprobante.getInfoAdicional().getCampoAdicional()){				
				StringBuilder sb=new StringBuilder();
				sb.append(c.getNombre());
				sb.append("  ");
				sb.append(c.getValue());
				mapaReportes.put("adicional"+i, sb.toString());
				i++;
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
