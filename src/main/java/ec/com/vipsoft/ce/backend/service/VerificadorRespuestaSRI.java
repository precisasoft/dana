package ec.com.vipsoft.ce.backend.service;

import java.io.StringWriter;
import java.util.Calendar;
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

import ec.com.vipsoft.ce.sri.autorizacion.wsclient.Autorizacion;
import ec.com.vipsoft.ce.sri.autorizacion.wsclient.ConsultaAutorizacion;
import ec.com.vipsoft.ce.sri.autorizacion.wsclient.RespuestaAutorizacionComprobante;
import ec.com.vipsoft.erp.abinadi.dominio.ComprobanteAutorizado;
import ec.com.vipsoft.erp.abinadi.dominio.ComprobanteElectronico;

@Stateless
public class VerificadorRespuestaSRI {

	@PersistenceContext
	private EntityManager em;
	@Inject
	private ConsultaAutorizacion consultorAutorizacion;

	
	@Schedule(dayOfMonth="*",hour="*",minute="*",second="0",year="*",month="*")
	public void verificarAutorizacionesPendientes(){
		
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
