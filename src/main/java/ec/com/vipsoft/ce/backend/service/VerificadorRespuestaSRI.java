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

	
	//@Schedule(dayOfMonth="*",hour="*",minute="*/2",second="0",year="*",month="*")
	public void verificarAutorizacionesPendientes(){
		if(!verificadorIndisponibilidad.estamosEnContingencia()){
			JAXBContext contexto=null;
			Marshaller marshaller=null;
			Query q=em.createQuery("select c from ComprobanteElectronico c where c.enviado=?1 and c.autorizacionConsultadoAlSRI=?2 and c.fechaEnvio<=?3");		
			q.setParameter(1, Boolean.TRUE);
			q.setParameter(2, Boolean.FALSE);
			Calendar ahora=GregorianCalendar.getInstance();
			ahora.add(Calendar.SECOND, -3);
			q.setParameter(3, ahora.getTime());					

			List<ComprobanteElectronico>lista=q.getResultList();		
			if(!lista.isEmpty()){
				Logger.getLogger(VerificadorRespuestaSRI.class.getCanonicalName()).finer("vamos a verficar respuesta de "+lista.size()+" comprobantes recibidos pero no autorizados");
				
			}	
		}



	}
}
