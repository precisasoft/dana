package ec.com.vipsoft.ce.backend.service;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.com.vipsoft.ce.utils.LlenadorNumeroComprobante;
import ec.com.vipsoft.erp.abinadi.dominio.ComprobanteElectronico;
import ec.com.vipsoft.erp.abinadi.dominio.ComprobanteElectronico.TipoComprobante;

@Stateless
@WebService
public class ConversorNumdocClaveAcceso implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8539455864752924622L;
	@PersistenceContext
	private EntityManager em;
	@Inject
	private LlenadorNumeroComprobante llenadorNumeroComprobante;
	/**
	 * 
	 * @param tipo  01,04,05,06,07
	 * @param numeroDocumento
	 * @return clave de acceso
	 */
	public String obtenerClaveAcceso(String rucEmisor,String tipo,String numeroDocumento){
		StringBuilder sb=new StringBuilder();		
		String numd=llenadorNumeroComprobante.llenarNumeroDocumento(numeroDocumento);
		StringTokenizer st=new StringTokenizer(numd,"-");
		TipoComprobante tipoc=null;
		Query q=em.createQuery("select c from ComprobanteElectronico c where c.tipo=?1 and c.establecimiento=?2 and c.puntoEMision=?3 and c.secuencia=?4" );
		if((tipo.equalsIgnoreCase("01"))||(tipo.equalsIgnoreCase("FA"))){
			tipoc=TipoComprobante.factura;
		}
		if(tipo.equalsIgnoreCase("04")||(tipo.equalsIgnoreCase("NC"))){
			tipoc=TipoComprobante.notaCredito;
		}
		if(tipo.equalsIgnoreCase("05")||(tipo.equalsIgnoreCase("ND"))){
			tipoc=TipoComprobante.notaDebito;
		}
		if(tipo.equalsIgnoreCase("06")||(tipo.equalsIgnoreCase("GR"))){
			tipoc=TipoComprobante.guiaRemision;
		}
		if(tipo.equalsIgnoreCase("07")||(tipo.equalsIgnoreCase("RE"))){
			tipoc=TipoComprobante.retencion;
		}
		q.setParameter(1, tipoc);
		q.setParameter(2, st.nextToken());
		q.setParameter(3, st.nextToken());
		q.setParameter(4, st.nextToken());
		List<ComprobanteElectronico>listado=q.getResultList();
		if(!listado.isEmpty()){
			sb.append(listado.get(0).getClaveAcceso());
		}		
		return sb.toString();
	}
}
