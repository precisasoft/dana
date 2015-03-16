package ec.com.vipsoft.ce.visorride;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPException;

import org.xml.sax.InputSource;

import ec.com.vipsoft.ce.backend.service.ContenedorReportesRide;
import ec.com.vipsoft.ce.backend.service.VerificadorRespuestaIndividual;
import ec.com.vipsoft.ce.sri.autorizacion.wsclient.Autorizacion;
import ec.com.vipsoft.ce.sri.autorizacion.wsclient.ConsultaAutorizacion;
import ec.com.vipsoft.ce.sri.autorizacion.wsclient.RespuestaAutorizacionComprobante;
import ec.com.vipsoft.ce.utils.UtilClaveAcceso;
import ec.com.vipsoft.erp.abinadi.dominio.ComprobanteAutorizado;
import ec.com.vipsoft.erp.abinadi.dominio.ComprobanteElectronico;
import ec.com.vipsoft.erp.abinadi.dominio.Entidad;
import ec.com.vipsoft.sri.comprobanteRetencion._v1_0.ComprobanteRetencion;
import ec.com.vipsoft.sri.factura._v1_1_0.Factura;
import ec.com.vipsoft.sri.guiaremision._v1_1_0.GuiaRemision;
import ec.com.vipsoft.sri.notaDebito.v_1_0.NotaDebito;
import ec.com.vipsoft.sri.notacredito._v1_1_0.NotaCredito;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
@Stateless
public class CreadorRide {
	
	@Inject
	private UtilClaveAcceso utilClaveAcceso;
	@EJB
	private ContenedorReportesRide contenedorRIDE;
	
	@PersistenceContext
	private EntityManager em;
	@Inject
	private ConsultaAutorizacion consultaAutorizacion;
	@EJB
	private VerificadorRespuestaIndividual verificadorRespuestaIndividual;
	
	public byte[] obtenerPDF(String claveAcceso){
		JasperPrint print=obtenerPrint(claveAcceso);
		byte[] enPDF = null;
		try {
			enPDF = JasperExportManager.exportReportToPdf(print);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return enPDF;
	}
	
	
	private JasperPrint obtenerPrint(String claveAcceso){		
		String ca = utilClaveAcceso.obtenerTipoDocumento(claveAcceso);
		String ruc = utilClaveAcceso.obtemerRucEmisor(claveAcceso);
		Query qEntidad = em.createQuery("select e from Entidad e where e.ruc=?1");
		qEntidad.setParameter(1, ruc);
		List<Entidad> listadoEntidad = qEntidad.getResultList();
		Autorizacion autorizacion = null;
		boolean tieneLogo = true;
		if (listadoEntidad.isEmpty()) {
			tieneLogo = true;
			Entidad entidad = listadoEntidad.get(0);
			tieneLogo = entidad.isTieneLogo();

			// verificar si tenemos en bd la autorización ... si no buscarla
			// ahora.
			Query qcomprobante = em.createQuery("select c from ComprobanteElectronico c where c.claveAcceso=?1");
			qcomprobante.setParameter(1, claveAcceso);
			List<ComprobanteElectronico> listadoComprobante = qcomprobante.getResultList();
			JAXBContext contexto;
			try {
				contexto = JAXBContext.newInstance(Autorizacion.class);
				Unmarshaller unmarshaller = contexto.createUnmarshaller();
				if (!listadoComprobante.isEmpty()) {
					ComprobanteElectronico _comprobante = em.find(ComprobanteElectronico.class, listadoComprobante.get(0).getId());
					if (_comprobante.getComprobanteAutorizado() != null) {
						ComprobanteAutorizado cautorizado = _comprobante.getComprobanteAutorizado();
						StringReader reader = new StringReader(new String(cautorizado.getEnXML()));
						autorizacion = (Autorizacion) unmarshaller.unmarshal(new InputSource(reader));
					} else {
						String verificacionComprobante = verificadorRespuestaIndividual.verificarAutorizacionComprobante(claveAcceso);
						autorizacion=(Autorizacion)unmarshaller.unmarshal(new InputSource(new StringReader(verificacionComprobante)));
						ComprobanteAutorizado cautorizado=new ComprobanteAutorizado();						
						cautorizado.setEnXML(verificacionComprobante.getBytes());		
						_comprobante.setComprobanteAutorizado(cautorizado);
					}
				} else {
					try {
						RespuestaAutorizacionComprobante _consultarAutorizacion = consultaAutorizacion.consultarAutorizacion(claveAcceso);
						if (!_consultarAutorizacion.getAutorizaciones().isEmpty()) {
							autorizacion = _consultarAutorizacion.getAutorizaciones().get(0);
						}
					} catch (SOAPException e) {
						e.printStackTrace();
					}
				}
			} catch (JAXBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			tieneLogo = false;
		}		
		Map<String,Object>parametros=new HashMap<>();
		if(autorizacion.getAmbiente().equalsIgnoreCase("PRUEBAS")){
			parametros.put("ambiente", "PRUEBAS");
		}else{
			parametros.put("ambiente", "PRODUCCION");
		}
		parametros.put("numeroAutorizacion","NO AUTORIZADO");
		if(autorizacion.getNumeroAutorizacion().length()>0){
			parametros.put("numeroAutorizacion",autorizacion.getNumeroAutorizacion());
		}
		parametros.put("fechaHoraAutorizacion", autorizacion.getFechaAutorizacion());				
		JRBeanCollectionDataSource datos=null;				
		JasperReport reporte=obtenerReporte(claveAcceso,tieneLogo);
		
		
		try {
		
		switch (ca) {  
		case "01":	//factura
		{
			JAXBContext contextoFactura = JAXBContext.newInstance(Factura.class);			
			Unmarshaller unmarshallerFactura=contextoFactura.createUnmarshaller();
			Factura comprobante=(Factura)unmarshallerFactura.unmarshal(new InputSource(autorizacion.getComprobante()));
		}	
			break;
		case "04":  //nota de credito
		{
			JAXBContext contextoNc=JAXBContext.newInstance(NotaCredito.class);
			Unmarshaller unmarshallerNC=contextoNc.createUnmarshaller();
			NotaCredito comprobante=(NotaCredito)unmarshallerNC.unmarshal(new InputSource(autorizacion.getComprobante()));
		}	
			break;
		case "05":  //nota de debito
		{
			JAXBContext contextoND=JAXBContext.newInstance(NotaDebito.class);
			Unmarshaller unmarshalleND=contextoND.createUnmarshaller();
			NotaDebito comprobante=(NotaDebito)unmarshalleND.unmarshal(new InputSource(autorizacion.getComprobante()));
		}
			break;
		case "06": //guia remision
		{
			JAXBContext contextoGuiaRemision=JAXBContext.newInstance(GuiaRemision.class);
			Unmarshaller unmarshallerGR=contextoGuiaRemision.createUnmarshaller();
			GuiaRemision comprobante=(GuiaRemision)unmarshallerGR.unmarshal(new InputSource(autorizacion.getComprobante()));
		}
			break;
		case "07":  //retencion
		{
			JAXBContext contextoRetencion=JAXBContext.newInstance(ComprobanteRetencion.class);
			Unmarshaller unmarshaller=contextoRetencion.createUnmarshaller();
			ComprobanteRetencion comprobante=(ComprobanteRetencion)unmarshaller.unmarshal(new InputSource(autorizacion.getComprobante()));			
		}
			break;
			
		default:
			break;
		}
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = JasperFillManager.fillReport(reporte, parametros,datos);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jasperPrint;		
	}
	
	
	private JasperReport obtenerReporte(String claveAcceso,boolean tiene__logo) {
		JasperReport reporte = null;	
		String tipoDocumento = utilClaveAcceso.obtenerTipoDocumento(claveAcceso);
		boolean tieneLogo = tiene__logo;
		switch (tipoDocumento) {
		case "01": // factura
			if (tieneLogo) {
				reporte = contenedorRIDE.getRideFactura();
			} else {
				reporte = contenedorRIDE.getRideFacturaSinLogo();
			}
			break;
		case "04": // nota de credito
			if (tieneLogo) {
				reporte = contenedorRIDE.getRideNotaCredito();
			} else {
				reporte = contenedorRIDE.getRideNotaCreditoSinLogo();
			}
			break;
		case "05":// nota de debito
			if (tieneLogo) {
				reporte = contenedorRIDE.getRideNotaDebito();
			} else {
				reporte = contenedorRIDE.getRideNotaDebitoSinLogo();
			}

			break;
		case "06":// guia de remision
			if (tieneLogo) {
				reporte = contenedorRIDE.getRideGuiaRemision();
			} else {
				reporte = contenedorRIDE.getRideGuiaRemisionSinLogo();
			}

			break;
		case "07":// comprobante de retencion
			if (tieneLogo) {
				reporte = contenedorRIDE.getRideRetencion();
			} else {
				reporte = contenedorRIDE.getRideRetencionSinLogo();
			}

			break;
		default:
			reporte = contenedorRIDE.getRideFactura();
			break;
		}
		return reporte;
	}
}
