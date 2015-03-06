package ec.com.vipsoft.ce.services.recepcionComprobantesNeutros;

import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.w3c.dom.Document;

import ec.com.vipsoft.ce.utils.UtilClaveAcceso;
import ec.com.vipsoft.erp.abinadi.dominio.ComprobanteElectronico;
import ec.com.vipsoft.erp.abinadi.dominio.DocumentoFirmado;
import ec.com.vipsoft.erp.abinadi.procesos.RespuestaRecepcionDocumento;

@Stateless
public class ProcesoEnvioEJB {
	
	@PersistenceContext
	private EntityManager em;
	@EJB
	private FirmadorDocumentoEJB firmador;
	@EJB
	private EnviadorSRIEJB enviador;
	@Inject
	private UtilClaveAcceso utilClaveAcceso;
	public void lanzarProcesoEnvio(Map<String,Object> parametros){
		
		String rucEntidad=(String) parametros.get("rucEmisor");
		byte[] bytes=(byte[]) parametros.get("archivop12");
		String contrasena=(String) parametros.get("contrasena");
		String claveAcceso=(String) parametros.get("claveAcceso");
		String documentoFirmado=(String) parametros.get("documentoFirmado");
		try {
		//	documentoFirmado = firmador.firmarDocumento(documentoAFirmar, rucEntidad, bytes, contrasena);
			RespuestaRecepcionDocumento respuestaRecepcion = enviador.enviarComprobanteAlSRI(documentoFirmado, utilClaveAcceso.esEnPruebas(claveAcceso));
			
			ComprobanteElectronico comprobante = new ComprobanteElectronico();
			comprobante.setClaveAcceso((String)parametros.get("claveAcceso"));
			comprobante.setPuntoEMision((String)parametros.get("codigoPuntoVenta"));		
			comprobante.setEstablecimiento((String)parametros.get("establecimiento"));
			comprobante.setSecuencia((String) parametros.get("secuenciaDocumento"));
			comprobante.setAutorizado(false);
			//boolean enviado = (boolean) proccessInstance.getProcessVariables().get("enviado");
//			comprobante.setEnviado(enviado);
//			if (!enviado) {
			if(respuestaRecepcion.getEstado().equalsIgnoreCase("devuelta")){
				comprobante.setCodigoError(respuestaRecepcion.getDetalle().get(0).getCodigo());
				comprobante.setMensajeError(respuestaRecepcion.getDetalle().get(0).getMensaje());
			}			
				DocumentoFirmado documentoFi = new DocumentoFirmado();
				documentoFi.setConvertidoEnXML(documentoFirmado);
				comprobante.setDocumentoFirmado(documentoFi);
//			}	
			em.persist(comprobante);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
