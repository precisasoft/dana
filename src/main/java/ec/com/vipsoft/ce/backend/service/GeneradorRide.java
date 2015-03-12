package ec.com.vipsoft.ce.backend.service;

import java.io.StringReader;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ec.com.vipsoft.ce.sri.autorizacion.wsclient.Autorizacion;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ec.com.vipsoft.ce.utils.UtilClaveAcceso;
import ec.com.vipsoft.erp.abinadi.dominio.ComprobanteElectronico;
import ec.com.vipsoft.web.ReporteFacturaBean;

@Stateless
public class GeneradorRide {
	@Inject
	private UtilClaveAcceso utilClaveAcceso;
	@EJB
	private ContenedorReportesRide contenedorReportesRide;
	@PersistenceContext
	private EntityManager em;
	
	public byte[] obtenerRide(String claveAcceso) throws JRException{
		byte[] retorno=null;
		String tipoComprobante=utilClaveAcceso.obtenerTipoDocumento(claveAcceso);
		//
		
		Query qComprobante=em.createQuery("select c from ComprobanteElectronico c where c.claveAcceso=?1");
		qComprobante.setParameter(1,claveAcceso);
		List<ComprobanteElectronico>lista=qComprobante.getResultList();
		if(!lista.isEmpty()){
			StringReader fis=new StringReader(new String(lista.get(0).getComprobanteAutorizado().getEnXML()));
			JAXBContext contextoFactura;
			try {
				contextoFactura = JAXBContext.newInstance(Autorizacion.class);
				Unmarshaller unmarshaller=contextoFactura.createUnmarshaller();
				Autorizacion respuesta=(Autorizacion) unmarshaller.unmarshal(fis);
				JasperReport reporte=null;
				switch (tipoComprobante) {
				case "01":
					reporte=contenedorReportesRide.getRideFactura();
					ReporteFacturaBean reporteBean=new ReporteFacturaBean();
					reporteBean.prepararDatosSegunFacturaProduccion(respuesta);
					JasperPrint print=JasperFillManager.fillReport(reporte, reporteBean.getMapaReportes(),new JRBeanCollectionDataSource(reporteBean.getDetalles()));
					retorno = JasperExportManager.exportReportToPdf(print);
					break;
				case "04":
					reporte=contenedorReportesRide.getRideNotaCredito();				
					break;
				case "05":
					reporte=contenedorReportesRide.getRideNotaDebito();
					break;
				case "06":
					reporte=contenedorReportesRide.getRideGuiaRemision();
					break;
				case "07":
					reporte=contenedorReportesRide.getRideRetencion();
					
					break;			
				}
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
				
		
		return retorno;
		
	}

}
