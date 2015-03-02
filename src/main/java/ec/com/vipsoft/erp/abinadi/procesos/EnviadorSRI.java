package ec.com.vipsoft.erp.abinadi.procesos;

import java.util.logging.Logger;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class EnviadorSRI implements JavaDelegate {

	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		Logger.getLogger(EnviadorSRI.class.getCanonicalName()).info("ingresando a metodo execute de EnviadorSRI");
		String timeoutrecepcion = (String) arg0.getVariable("timeputrecepcion");
		boolean enPruebas = (boolean) arg0.getVariable("enPruebas");

		// documentoxml
		String documentoXmlFirmado = (String) arg0.getVariable("documentoFirmado");
		if (enPruebas) {
			ec.gob.sri.canales.pruebas.recepcion.RecepcionComprobantesService sevice = new ec.gob.sri.canales.pruebas.recepcion.RecepcionComprobantesService();
			ec.gob.sri.canales.pruebas.recepcion.RecepcionComprobantes port = sevice.getRecepcionComprobantesPort();
			ec.gob.sri.canales.pruebas.recepcion.RespuestaSolicitud respuesta = port.validarComprobante(documentoXmlFirmado.getBytes());
			arg0.setVariable("respuestaRecepcion", respuesta.getEstado());
			Logger.getLogger(EnviadorSRI.class.getCanonicalName()).info("la respuesta fue " + respuesta.getEstado());
			if (!respuesta.getEstado().equals("RECIBIDA")) {
				arg0.setVariable("respuesta", "DEVUELTA");
				if(!respuesta.getComprobantes().getComprobante().isEmpty()){
					String codigoError = respuesta.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getIdentificador();
					String mensajeError = respuesta.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getMensaje();
					Logger.getLogger(EnviadorSRI.class.getCanonicalName()).info("DEVUELTA  ERROR " + codigoError + "    MENSAJE "+ mensajeError);
					arg0.setVariable("codigoError", codigoError);
					arg0.setVariable("mensajeError", mensajeError);
					arg0.setVariable("enviado", false);
				}else{
					arg0.setVariable("enviado", false);
					arg0.setVariable("codigoError", "VACIO");
					arg0.setVariable("mensajeError", "VACIO");
				}
				
			}else{
				arg0.setVariable("enviado", true);
				arg0.setVariable("respuesta", "ENVIADO");
			}
		} else {

			ec.gob.sri.canales.produccion.recepcion.RecepcionComprobantesService sevice = new ec.gob.sri.canales.produccion.recepcion.RecepcionComprobantesService();
			ec.gob.sri.canales.produccion.recepcion.RecepcionComprobantes port = sevice.getRecepcionComprobantesPort();
			ec.gob.sri.canales.produccion.recepcion.RespuestaSolicitud respuesta = port.validarComprobante(documentoXmlFirmado.getBytes());
			arg0.setVariable("respuestaRecepcion", respuesta.getEstado());
			Logger.getLogger(EnviadorSRI.class.getCanonicalName()).info("la respuesta fue " + respuesta.getEstado());
			if (!respuesta.getEstado().equals("RECIBIDA")) {
				arg0.setVariable("respuesta", "DEVUELTA");
				if(!respuesta.getComprobantes().getComprobante().isEmpty()){
					String codigoError = respuesta.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getIdentificador();
					String mensajeError = respuesta.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getMensaje();
					Logger.getLogger(EnviadorSRI.class.getCanonicalName()).info("DEVUELTA  ERROR " + codigoError + "    MENSAJE "+ mensajeError);
					arg0.setVariable("codigoError", codigoError);
					arg0.setVariable("mensajeError", mensajeError);	
					arg0.setVariable("enviado", false);
				}else{
					arg0.setVariable("enviado", false);
					arg0.setVariable("codigoError", "VACIO");
					arg0.setVariable("mensajeError", "VACIO");					
				}				
			}else{
				arg0.setVariable("enviado", true);
				arg0.setVariable("respuesta", "ENVIADO");
			}
		}
		Logger.getLogger(EnviadorSRI.class.getCanonicalName()).info("finalizando método execute de enviador SRI");
	}

}
