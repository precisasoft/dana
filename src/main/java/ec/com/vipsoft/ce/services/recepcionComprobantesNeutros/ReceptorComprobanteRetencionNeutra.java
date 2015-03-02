/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.vipsoft.ce.services.recepcionComprobantesNeutros;

import java.math.RoundingMode;
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
import ec.com.vipsoft.ce.comprobantesNeutros.ComprobanteRetencionBinding;
import ec.com.vipsoft.ce.comprobantesNeutros.ComprobanteRetencionDetalleBinding;
import ec.com.vipsoft.ce.utils.UtilClaveAcceso;
import ec.com.vipsoft.erp.abinadi.dominio.ComprobanteElectronico;
import ec.com.vipsoft.erp.abinadi.dominio.DocumentoFirmado;
import ec.com.vipsoft.erp.abinadi.dominio.Entidad;
import ec.com.vipsoft.sri.comprobanteRetencion._v1_0.ComprobanteRetencion;
import ec.com.vipsoft.sri.comprobanteRetencion._v1_0.Impuesto;
import ec.com.vipsoft.sri.comprobanteRetencion._v1_0.ObligadoContabilidad;

/**
 *
 * @author chrisvv
 */
@Stateless
@WebService
public class ReceptorComprobanteRetencionNeutra {            
	@EJB
	private GeneradorClaveAccesoPorEntidad generadorClaveAcceso;
	@Inject
	private UtilClaveAcceso utilClaveAccesl;
	@PersistenceContext
	private EntityManager em;
	@WebMethod
	@WebResult(name = "claveAcceso")
	public String receptarComprobanteRetencion(@WebParam(name="retencion")ComprobanteRetencionBinding retencion){       
		String claveAcceso = generadorClaveAcceso.generarClaveAccesoComprobanteRetencion(retencion.getRucEmisor(), retencion.getCodigoEstablecimiento(), retencion.getCodigoPuntoVenta());        
		String rucEmisor = retencion.getRucEmisor();
		String puntoEmision=utilClaveAccesl.obtenerCodigoPuntoEmision(claveAcceso);
		String establecimiento=utilClaveAccesl.obtenerCodigoEstablecimiento(claveAcceso);
		String secuenciaDocumento=utilClaveAccesl.obtenerSecuanciaDocumento(claveAcceso);
		String ambiente=utilClaveAccesl.obtenerAmbiente(claveAcceso);
		String codigoDocumento=utilClaveAccesl.obtenerTipoDocumento(claveAcceso);
		Query q = em.createQuery("select e from Entidad e where e.ruc=?1 and e.habilitado=?2");
		q.setParameter(1, rucEmisor);
		q.setParameter(2, Boolean.TRUE);
		List<Entidad> lista = q.getResultList();
		if (!lista.isEmpty()) {
			Entidad entidad=em.find(Entidad.class, lista.get(0).getId());
			ComprobanteRetencion comprobanteRetencion=new ComprobanteRetencion();
			comprobanteRetencion.getInfoTributaria().setRuc(rucEmisor);
			comprobanteRetencion.getInfoTributaria().setAmbiente(ambiente);
			comprobanteRetencion.getInfoTributaria().setClaveAcceso(claveAcceso);
			comprobanteRetencion.getInfoTributaria().setCodDoc(codigoDocumento);
			comprobanteRetencion.getInfoTributaria().setDirMatriz(entidad.getDireccionMatriz());
			comprobanteRetencion.getInfoTributaria().setEstab(establecimiento);
			comprobanteRetencion.getInfoTributaria().setNombreComercial(entidad.getNombreComercial());
			comprobanteRetencion.getInfoTributaria().setPtoEmi(puntoEmision);
			comprobanteRetencion.getInfoTributaria().setRazonSocial(entidad.getRazonSocial());
			comprobanteRetencion.getInfoTributaria().setSecuencial(secuenciaDocumento);
			if (utilClaveAccesl.esEnContingencia(claveAcceso)) {
				comprobanteRetencion.getInfoTributaria().setTipoEmision("2");
			} else {
				comprobanteRetencion.getInfoTributaria().setTipoEmision("1");
			}
			////////////////////////////////////////////////////////////////////////////////
			if(entidad.isObligadoContabilidad()){
				comprobanteRetencion.getInfoCompRetencion().setObligadoContabilidad(ObligadoContabilidad.SI);
				if(entidad.getResolucionContribuyenteEspecial()!=null){
					comprobanteRetencion.getInfoCompRetencion().setContribuyenteEspecial(entidad.getResolucionContribuyenteEspecial());
				}                           
			}else{
				comprobanteRetencion.getInfoCompRetencion().setObligadoContabilidad(ObligadoContabilidad.NO);
			}
			comprobanteRetencion.getInfoCompRetencion().setDirEstablecimiento(entidad.getDireccionMatriz());
			comprobanteRetencion.getInfoCompRetencion().setFechaEmision(retencion.getFechaEmisionTexto());
			comprobanteRetencion.getInfoCompRetencion().setIdentificacionSujetoRetenido(retencion.getIdentificacionBeneficiario());
			comprobanteRetencion.getInfoCompRetencion().setTipoIdentificacionSujetoRetenido(retencion.getCodigoTipoIdentificacionBeneficiario());
			comprobanteRetencion.getInfoCompRetencion().setPeriodoFiscal(retencion.getPeriodo());
			comprobanteRetencion.getInfoCompRetencion().setRazonSocialSujetoRetenido(retencion.getRazonSocialBeneficiario());
			comprobanteRetencion.getInfoCompRetencion().setTipoIdentificacionSujetoRetenido(retencion.getCodigoTipoIdentificacionBeneficiario());
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
			//////////////////////////////7
			for(ComprobanteRetencionDetalleBinding d : retencion.getDetalles()){
				Impuesto impuesto=new Impuesto();
				impuesto.setBaseImponible(d.getBaseImponible());
				impuesto.setCodDocSustento(d.getCodigoDocumento());
				impuesto.setCodigo(d.getTipoImpuesto());
				impuesto.setCodigoRetencion(d.getCodigoImpuesto());
				impuesto.setFechaEmisionDocSustento(d.getFechaEmisionTexto());
				impuesto.setNumDocSustento(d.getNumeroDocumento());
				impuesto.setPorcentajeRetener(d.getPorcentajeRetencion().setScale(2, RoundingMode.HALF_UP));
				impuesto.setValorRetenido(d.getValorRetenido());        	           	   
				comprobanteRetencion.getImpuestos().getImpuesto().add(impuesto);
			}



			//////////////////////////////
			try {
				Document convertidoEnDOM = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				JAXBContext contexto= JAXBContext.newInstance(ComprobanteRetencion.class);
				Marshaller marshaller = contexto.createMarshaller();
				marshaller.marshal(comprobanteRetencion, convertidoEnDOM);
				Map<String, Object> parametros = new HashMap<>();
				// parametros.put("documentoOriginal",factura);
				parametros.put("documentoAFirmar", convertidoEnDOM);
				parametros.put("rucEmisor", rucEmisor);
				parametros.put("establecimiento",retencion.getCodigoEstablecimiento());
				parametros.put("codigoPuntoVenta",retencion.getCodigoPuntoVenta());
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
