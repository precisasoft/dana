package ec.com.vipsoft.ce.backend.service;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.soap.SOAPException;

import ec.com.vipsoft.ce.services.recepcionComprobantesNeutros.EnviadorSRIEJB;
import ec.com.vipsoft.ce.sri.autorizacion.wsclient.Autorizacion;
import ec.com.vipsoft.ce.sri.autorizacion.wsclient.ConsultaAutorizacion;
import ec.com.vipsoft.ce.sri.autorizacion.wsclient.RespuestaAutorizacionComprobante;
import ec.com.vipsoft.ce.utils.UtilClaveAcceso;
import ec.com.vipsoft.erp.abinadi.dominio.ComprobanteAutorizado;
import ec.com.vipsoft.erp.abinadi.dominio.ComprobanteElectronico;
import ec.com.vipsoft.erp.abinadi.dominio.DocumentoFirmado;
import ec.com.vipsoft.erp.abinadi.procesos.RespuestaRecepcionDocumento;

@Stateless
public class VerificadorRespuestaSRI {

	public static Integer MAXREINTENTOS=50;
	@PersistenceContext
	private EntityManager em;
	@Inject
	private ConsultaAutorizacion consultorAutorizacion;
	@EJB
	private EnviadorSRIEJB enviadorSRI;
	@Inject
	private UtilClaveAcceso utilClaveAcceso;
	@EJB
	private VerificadorIndisponibilidad verificadorIndisponibilidad;

	
	@Schedule(dayOfMonth="*",hour="*",minute="*",second="0,10,20,30,40,50",year="*",month="*")
	public void verificarAutorizacionesPendientes(){
		if(!verificadorIndisponibilidad.estamosEnContingencia()){
			JAXBContext contexto=null;
			Marshaller marshaller=null;
			Query q=em.createQuery("select c from ComprobanteElectronico c where c.enviado=?1 and c.autorizado=?2 and c.fechaEnvio<=?3");		
			q.setParameter(1, Boolean.TRUE);
			q.setParameter(2, Boolean.FALSE);
			Calendar ahora=GregorianCalendar.getInstance();
			ahora.add(Calendar.SECOND, -3);
			q.setParameter(3, ahora.getTime());		
			
			List<ComprobanteElectronico>lista=q.getResultList();		
			if(!lista.isEmpty()){
				Logger.getLogger(VerificadorRespuestaSRI.class.getCanonicalName()).info("vamos a verficar respuesta de "+lista.size()+" comprobantes recibidos pero no autorizados");
				try {
					contexto=JAXBContext.newInstance(Autorizacion.class);
					marshaller=contexto.createMarshaller();
				} catch (JAXBException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for(ComprobanteElectronico c:lista){
					
				try {
					RespuestaAutorizacionComprobante respuesta = consultorAutorizacion.consultarAutorizacion(c.getClaveAcceso());
					if(!respuesta.getAutorizaciones().isEmpty()){
						for(Autorizacion a:respuesta.getAutorizaciones()){
							if(a.getEstado().equalsIgnoreCase("AUTORIZADO")){
								//aqui construir el pdf ... y xml ... 
								
								
								StringWriter swriter=new StringWriter();
								marshaller.marshal(respuesta.getAutorizaciones().get(0), swriter);				
								ComprobanteElectronico elcomprobante=em.find(ComprobanteElectronico.class, c.getId());
								elcomprobante.setAutorizado(true);
								elcomprobante.setNumeroAutorizacion(a.getNumeroAutorizacion());
								ComprobanteAutorizado ca=new ComprobanteAutorizado();
								ca.setEnXML(swriter.toString().getBytes());
								elcomprobante.setComprobanteAutorizado(ca);
								elcomprobante.setFechaAutorizacion(a.getFechaAutorizacion());
								c.setAutorizado(true);						
								
								//notificador.equals(elcomprobante.getIdentificacionBeneficiario().)
							}
						}
					}else{
							// si ha pasado media hora ...volver a enviarlo.
							Query qcomprobantes = em.createQuery("select c from ComprobanteElectronico c where c.claveAccesp=?1");
							qcomprobantes.setParameter(1,respuesta.getClaveAccesoConsultada());
							List<ComprobanteElectronico> listaComprobantes = qcomprobantes.getResultList();
							if (!listaComprobantes.isEmpty()) {
								Calendar ahora2 = new GregorianCalendar();
								ahora2.add(Calendar.MINUTE, -30);
								ComprobanteElectronico lazaro = em.find(ComprobanteElectronico.class,listaComprobantes.get(0).getId());
								if ((lazaro.getFechaEnvio().before(ahora2.getTime())&&(lazaro.getReintentos()<=MAXREINTENTOS))) {
									DocumentoFirmado dfirmado = em.find(DocumentoFirmado.class,	lazaro.getDocumentoFirmado());
									RespuestaRecepcionDocumento enviarComprobanteAlSRI = enviadorSRI.enviarComprobanteAlSRI(dfirmado.getConvertidoEnXML(),utilClaveAcceso.esEnPruebas(lazaro.getClaveAcceso()));
									lazaro.setFechaEnvio(new Date());
									lazaro.setReintentos(lazaro.getReintentos()+1);
								}

							}
						}

				} catch (SOAPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				}
			}	
		}
		
		
		
	}
}
