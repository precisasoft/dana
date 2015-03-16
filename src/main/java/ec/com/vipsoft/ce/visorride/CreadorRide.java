package ec.com.vipsoft.ce.visorride;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.com.vipsoft.ce.backend.service.ContenedorReportesRide;
import ec.com.vipsoft.ce.utils.UtilClaveAcceso;
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
		
		String ca=utilClaveAcceso.obtenerTipoDocumento(claveAcceso);
		Map<String,Object>parametros=new HashMap<>();
		JRBeanCollectionDataSource datos=null;
		boolean tieneLogo=true;
		
		JasperReport reporte=obtenerReporte(claveAcceso,tieneLogo);
		switch (ca) {  
		case "01":	//factura		
			break;
		case "04":  //nota de credito
			break;
		case "05":  //nota de debito
			break;
		case "06": //guia remision
			break;
		case "07":  //retencion
			break;
			
		default:
			break;
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
