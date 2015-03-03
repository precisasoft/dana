package ec.com.vipsoft.ce.services.recepcionComprobantesNeutros;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.w3c.dom.Document;

import ec.com.vipsoft.ce.backend.service.GeneradorClaveAccesoPorEntidad;
import ec.com.vipsoft.ce.comprobantesNeutros.FacturaBinding;
import ec.com.vipsoft.ce.comprobantesNeutros.FacturaDetalleBinding;
import ec.com.vipsoft.ce.utils.UtilClaveAcceso;
import ec.com.vipsoft.erp.abinadi.dominio.ComprobanteElectronico;
import ec.com.vipsoft.erp.abinadi.dominio.DocumentoFirmado;
import ec.com.vipsoft.erp.abinadi.dominio.Entidad;
import ec.com.vipsoft.sri.factura._v1_1_0.Factura;
import ec.com.vipsoft.sri.factura._v1_1_0.Factura.Detalles.Detalle.Impuestos;
import ec.com.vipsoft.sri.factura._v1_1_0.Impuesto;
import ec.com.vipsoft.sri.factura._v1_1_0.ObligadoContabilidad;

/**
 * El objetivo de este bean es obtener una factura. anadirle información como la
 * clave de acceso,la secuencia de documento
 *
 * @author chrisvv
 *
 */
@Stateless
@WebService
public class ReceptorFacturaNeutra {
	@EJB
	private GeneradorClaveAccesoPorEntidad generadorClaveAcceso;
	@Inject
	private UtilClaveAcceso utilClaveAccesl;
	@PersistenceContext
	private EntityManager em;

	@WebMethod
	@WebResult(name = "claveAcceso")
	public String recibirFactura(@WebParam(name = "factura") FacturaBinding factura) {
		if((factura.getIdentificacionBeneficiario()==null)||(factura.getIdentificacionBeneficiario().length()<=1)){
			if(factura.getTotal().doubleValue()<=20d){
				factura.setIdentificacionBeneficiario("9999999999999");
				factura.setRazonSocialBeneficiario("USUARIO FINAL");
				factura.setDireccionBeneficiario("USUARIO FINAL");
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		String claveAcceso = generadorClaveAcceso.generarClaveAccesoFactura(factura.getRucEmisor(), factura.getCodigoEstablecimiento(),	factura.getCodigoPuntoVenta());
		String rucEmisor = factura.getRucEmisor();
		String puntoEmision = utilClaveAccesl.obtenerCodigoPuntoEmision(claveAcceso);
		String establecimiento = utilClaveAccesl.obtenerCodigoEstablecimiento(claveAcceso);
		String secuenciaDocumento = utilClaveAccesl	.obtenerSecuanciaDocumento(claveAcceso);
		String ambiente = utilClaveAccesl.obtenerAmbiente(claveAcceso);
		Query q = em.createQuery("select e from Entidad e where e.ruc=?1 and e.habilitado=?2");
		q.setParameter(1, rucEmisor);
		q.setParameter(2, Boolean.TRUE);
		List<Entidad> lista = q.getResultList();
		if (!lista.isEmpty()) {
			Entidad entidad = em.find(Entidad.class, lista.get(0).getId());
			Factura facturaxml = new Factura();
			facturaxml.setId("comprobante");
			facturaxml.setVersion("1.1.0");
			if (utilClaveAccesl.esEnPruebas(claveAcceso)) {
				facturaxml.getInfoTributaria().setAmbiente("1");
			} else {
				facturaxml.getInfoTributaria().setAmbiente("2");
			}
			facturaxml.getInfoTributaria().setAmbiente(ambiente);
			facturaxml.getInfoTributaria().setClaveAcceso(claveAcceso);
			facturaxml.getInfoTributaria().setCodDoc("04");
			facturaxml.getInfoTributaria().setDirMatriz(entidad.getDireccionMatriz());
			facturaxml.getInfoTributaria().setNombreComercial(entidad.getNombreComercial());
			facturaxml.getInfoTributaria().setRazonSocial(entidad.getRazonSocial());
			facturaxml.getInfoTributaria().setRuc(entidad.getRuc());
			facturaxml.getInfoTributaria().setPtoEmi(puntoEmision);
			facturaxml.getInfoTributaria().setEstab(establecimiento);
			facturaxml.getInfoTributaria().setSecuencial(secuenciaDocumento);

			if (entidad.isObligadoContabilidad()) {
				facturaxml.getInfoFactura().setObligadoContabilidad(ObligadoContabilidad.SI);
				if (entidad.getResolucionContribuyenteEspecial() != null) {
					facturaxml.getInfoFactura().setContribuyenteEspecial(entidad.getResolucionContribuyenteEspecial());
				}
			} else {
				facturaxml.getInfoFactura().setObligadoContabilidad(ObligadoContabilidad.NO);
			}
			if (utilClaveAccesl.esEnContingencia(claveAcceso)) {
				facturaxml.getInfoTributaria().setTipoEmision("2");
			} else {
				facturaxml.getInfoTributaria().setTipoEmision("1");
			}
			facturaxml.getInfoFactura().setImporteTotal(factura.getTotal());
			facturaxml.getInfoFactura().setPropina(new BigDecimal("0.00"));
			facturaxml.getInfoFactura().setDireccionComprador(factura.getDireccionBeneficiario());
			facturaxml.getInfoFactura().setDirEstablecimiento(entidad.getDireccionMatriz());
			facturaxml.getInfoFactura().setFechaEmision(factura.getFechaEmisionTexto());
			if (factura.getGuiaRemision() != null) {
				facturaxml.getInfoFactura().setGuiaRemision(factura.getGuiaRemision());
			}
			facturaxml.getInfoFactura().setIdentificacionComprador(rucEmisor);
			facturaxml.getInfoFactura().setTipoIdentificacionComprador(	factura.getCodigoTipoIdentificacionBeneficiario());
			facturaxml.getInfoFactura().setRazonSocialComprador(factura.getRazonSocialBeneficiario());
			facturaxml.getInfoFactura().setMoneda("DOLAR");
			facturaxml.getInfoFactura().setTotalSinImpuestos(factura.getSubtotalIva0());
			facturaxml.getInfoFactura().setTotalDescuento(factura.calculaDescuento());

			if (factura.getSubtotalIva0().doubleValue() > 0) {
				Factura.InfoFactura.TotalConImpuestos.TotalImpuesto iva0 = new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
				iva0.setBaseImponible(factura.getSubtotalIva0());
				iva0.setCodigo("2");
				iva0.setCodigoPorcentaje("0");
				iva0.setTarifa(new BigDecimal("0"));
				iva0.setValor(new BigDecimal("0.00"));
				facturaxml.getInfoFactura().getTotalConImpuestos().getTotalImpuesto().add(iva0);
			}

			if (factura.getSubtotalIva12().doubleValue() > 0) {
				Factura.InfoFactura.TotalConImpuestos.TotalImpuesto iva12 = new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
				iva12.setBaseImponible(factura.getSubtotalIva12());
				iva12.setCodigo("2");
				iva12.setCodigoPorcentaje("2");
				iva12.setTarifa(new BigDecimal("12.00"));
				iva12.setValor(factura.getSubtotalIva12());
				facturaxml.getInfoFactura().getTotalConImpuestos().getTotalImpuesto().add(iva12);
			}

			if (factura.getSubtotalIce().doubleValue() > 0) {

				Factura.InfoFactura.TotalConImpuestos.TotalImpuesto ice = new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
				ice.setTarifa(factura.getSubtotalIce());
				ice.setCodigo("3");
				facturaxml.getInfoFactura().getTotalConImpuestos().getTotalImpuesto().add(ice);
			}
			// if(factura.getExecntoiva().doubleValue()>0){
			// Factura.InfoFactura.TotalConImpuestos.TotalImpuesto
			// excentoiva=new
			// Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
			// excentoiva.setBaseImponible(_factura.getSubtotal12());
			// excentoiva.setCodigo("2");
			// excentoiva.setCodigoPorcentaje("7");
			// //excentoiva.setTarifa(new BigDecimal("12.00"));
			// excentoiva.setValor(_factura.getExecntoiva());
			// facturaRetorno.getInfoFactura().getTotalConImpuestos().getTotalImpuesto().add(excentoiva);
			// }
			// if(_factura.getNo_sujeto_impueesto().doubleValue()>0){
			// ec.com.vipsoft.sri.jaxbinding.factura.Factura.InfoFactura.TotalConImpuestos.TotalImpuesto
			// nosujetoAImpuesto=new
			// ec.com.vipsoft.sri.jaxbinding.factura.Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();
			// nosujetoAImpuesto.setCodigo("2");
			// nosujetoAImpuesto.setCodigoPorcentaje("6");
			// nosujetoAImpuesto.setValor(_factura.getNo_sujeto_impueesto());
			// facturaRetorno.getInfoFactura().getTotalConImpuestos().getTotalImpuesto().add(nosujetoAImpuesto);
			//
			for (FacturaDetalleBinding d : factura.getDetalles()) {
				Factura.Detalles.Detalle detalle = new Factura.Detalles.Detalle();
				detalle.setCantidad(d.getCantidad().setScale(2,
						RoundingMode.HALF_UP));
				// detalle.setCodigoPrincipal(d.getCodigoICE());
				// detalle.setCodigoAuxiliar(d.getCodigoInterno());
				detalle.setCodigoPrincipal(d.getCodigo());
				detalle.setDescripcion(d.getDescripcion());
				detalle.setDescuento(d.getDescuento().setScale(2,RoundingMode.HALF_UP));
				detalle.setPrecioUnitario(d.getValorUnitario().setScale(2,RoundingMode.HALF_UP));
				detalle.setPrecioTotalSinImpuesto(d.getValorTotal().setScale(2,	RoundingMode.HALF_UP));
				if (!d.getCodigoIVA().isEmpty()) {
					Impuesto impuesto = new Impuesto();
					impuesto.setBaseImponible(d.calculaBaeImponible());
					impuesto.setCodigo("2");
					impuesto.setCodigoPorcentaje(d.getCodigoIVA().trim());
					impuesto.setTarifa(new BigDecimal("12"));
					impuesto.setValor(d.getIva12().setScale(2,RoundingMode.HALF_DOWN));
					Impuestos impuestos = new Impuestos();
					detalle.setImpuestos(impuestos);
					detalle.getImpuestos().getImpuesto().add(impuesto);

					// detalle.getImpuestos().getImpuesto().add(e)
				}
				facturaxml.getDetalles().getDetalle().add(detalle);

			}

			try {
				Document convertidoEnDOM = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				JAXBContext contexto= JAXBContext.newInstance(Factura.class);
				Marshaller marshaller = contexto.createMarshaller();
				marshaller.marshal(facturaxml, convertidoEnDOM);
				Map<String, Object> parametros = new HashMap<>();
				// parametros.put("documentoOriginal",factura);
				parametros.put("documentoAFirmar", convertidoEnDOM);
				parametros.put("rucEmisor", rucEmisor);
				parametros.put("establecimiento",factura.getCodigoEstablecimiento());
				parametros.put("codigoPuntoVenta",factura.getCodigoPuntoVenta());
				ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
				RuntimeService runtimeService = processEngine.getRuntimeService();
				ProcessInstance proccessInstance = runtimeService.startProcessInstanceByKey("procesoEnvio", parametros);
				// proccessInstance.getProcessVariables().get("")
				ComprobanteElectronico comprobante = new ComprobanteElectronico();
				comprobante.setClaveAcceso(claveAcceso);
				comprobante.setPuntoEMision(puntoEmision);
				comprobante.setEstablecimiento(establecimiento);
				comprobante.setSecuencia(secuenciaDocumento);
				comprobante.setAutorizado(false);
				boolean enviado = (boolean) proccessInstance.getProcessVariables().get("enviado");
				comprobante.setEnviado(enviado);
				if (!enviado) {
					comprobante.setCodigoError((String) proccessInstance.getProcessVariables().get("codigoError"));
					comprobante.setMensajeError((String) proccessInstance.getProcessVariables().get("mensajeError"));
					DocumentoFirmado documentoFi = new DocumentoFirmado();
					documentoFi.setConvertidoEnXML((String) proccessInstance.getProcessVariables().get("documentoFirmado"));
					comprobante.setDocumentoFirmado(documentoFi);
				}
				comprobante.setProcessId(proccessInstance.getProcessInstanceId());
				em.persist(comprobante);
			} catch (JAXBException ex) {
				Logger.getLogger(ReceptorFacturaNeutra.class.getName()).log(
						Level.SEVERE, null, ex);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return claveAcceso;
	}
}
