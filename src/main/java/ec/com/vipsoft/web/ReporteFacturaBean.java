package ec.com.vipsoft.web;


import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ec.com.vipsoft.ce.sri.autorizacion.wsclient.Autorizacion;
import ec.com.vipsoft.sri.factura._v1_1_0.Factura;
import ec.com.vipsoft.sri.factura._v1_1_0.Factura.Detalles.Detalle;
import ec.com.vipsoft.sri.factura._v1_1_0.Factura.InfoAdicional.CampoAdicional;
import ec.com.vipsoft.sri.factura._v1_1_0.Impuesto;



public class ReporteFacturaBean implements Serializable{

	private static final long serialVersionUID = 938841712660879865L;
	public ReporteFacturaBean() {
		super();
		mapaReportes=new HashMap<>();
		detalles=new ArrayList<>();
	}
	
	public Map<String, Object> getMapaReportes() {
		return mapaReportes;
	}
	public void setMapaReportes(Map<String, Object> mapaReportes) {
		this.mapaReportes = mapaReportes;
	}
	public List<ReporteFacturaDetalleBean> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<ReporteFacturaDetalleBean> detalles) {
		this.detalles = detalles;
	}

	private Map<String,Object> mapaReportes;
	private List<ReporteFacturaDetalleBean> detalles;
	
	public void prepararDatosSegunFacturaProduccion(Autorizacion autorizacion){
		
		
		mapaReportes.put("numeroAutorizacion", autorizacion.getNumeroAutorizacion());
		mapaReportes.put("fechaHoraAutorizacion", autorizacion.getFechaAutorizacion());
		mapaReportes.put("ambiente", autorizacion.getAmbiente());
		mapaReportes.put("rucEmisor", autorizacion.getNumeroAutorizacion().substring(14,27));
		String elComprobante=autorizacion.getComprobante();
		try {
			JAXBContext jaxbContext=JAXBContext.newInstance(Factura.class);
			Unmarshaller unmarshaller=jaxbContext.createUnmarshaller();
			StringReader reader=new StringReader(elComprobante);
			Factura factura=(Factura)unmarshaller.unmarshal(reader);
			
			String _tipoEmision=factura.getInfoTributaria().getTipoEmision();
			if(_tipoEmision.equalsIgnoreCase("1")){
				mapaReportes.put("tipoEmision", "NORMAL");
			}
			if(_tipoEmision.equalsIgnoreCase("2")){
				mapaReportes.put("tipoEmision", "CONTINGENCIA");
			}
			mapaReportes.put("claveAcceso", factura.getInfoTributaria().getClaveAcceso());
			mapaReportes.put("razonSocialCliente", factura.getInfoFactura().getRazonSocialComprador());
			mapaReportes.put("identificacionCliente", factura.getInfoFactura().getIdentificacionComprador());
			mapaReportes.put("fechaEmision", factura.getInfoFactura().getFechaEmision());
			mapaReportes.put("guiaRemision", factura.getInfoFactura().getGuiaRemision());
			
			int i=1;
			for(CampoAdicional c:factura.getInfoAdicional().getCampoAdicional()){
				
				StringBuilder sb=new StringBuilder();
				sb.append(c.getNombre());
				sb.append("  ");
				sb.append(c.getValue());
				mapaReportes.put("adicional"+i, sb.toString());
				i++;
			}
		//	mapaReportes.put("ordenCompra", factura.getInfoAdicional().get )
			StringBuilder sbumeroDoc=new StringBuilder(factura.getInfoTributaria().getEstab());
			sbumeroDoc.append("-");
			sbumeroDoc.append(factura.getInfoTributaria().getPtoEmi());
			sbumeroDoc.append("-");
			sbumeroDoc.append(factura.getInfoTributaria().getSecuencial());
			
			mapaReportes.put("numeroDocumento", sbumeroDoc.toString());
			BigDecimal subtotal12=BigDecimal.ZERO;
			BigDecimal subtotalIva0=BigDecimal.ZERO;
			BigDecimal subtotalNoObjetoIVA=BigDecimal.ZERO;
			BigDecimal subtotalExentoIva=BigDecimal.ZERO;
			BigDecimal ice=BigDecimal.ZERO;
			BigDecimal iva12=BigDecimal.ZERO;
			BigDecimal totalDescuento=factura.getInfoFactura().getTotalDescuento();
			
			for( Detalle detalle :factura.getDetalles().getDetalle()){
				ReporteFacturaDetalleBean _detalle=new ReporteFacturaDetalleBean();
				_detalle.setCantidad(detalle.getCantidad());
				_detalle.setCodigoPrincipal(detalle.getCodigoPrincipal());
				_detalle.setCodigoAuxiliar(detalle.getCodigoAuxiliar());
				_detalle.setDescripcion(detalle.getDescripcion());
				_detalle.setDescuento(detalle.getDescuento());
				_detalle.setPrecioUnitario(detalle.getPrecioUnitario());
				_detalle.setPrecioTotal(detalle.getPrecioTotalSinImpuesto());
				if(detalle.getDetallesAdicionales()!=null){
					if(detalle.getDetallesAdicionales().getDetAdicional()!=null){
						if(!detalle.getDetallesAdicionales().getDetAdicional().isEmpty()){
							if(detalle.getDetallesAdicionales().getDetAdicional().get(0)!=null){
								_detalle.setDetalleAdicional(detalle.getDetallesAdicionales().getDetAdicional().get(0).getValor());
							}	
						}
							
					}
						
				}				
				//_detalle.setDetalleAdicional(detalle.getDetallesAdicionales().getDetAdicional().get(0).);
				
				for(Impuesto impuesto:detalle.getImpuestos().getImpuesto()){
					
					//iva
					if(impuesto.getCodigo().equalsIgnoreCase("2")){
						if(impuesto.getCodigoPorcentaje().equalsIgnoreCase("2")){
							subtotal12=subtotal12.add(impuesto.getBaseImponible());
							iva12=iva12.add(impuesto.getValor());
						}
						if(impuesto.getCodigoPorcentaje().equalsIgnoreCase("0")){
							subtotalIva0=subtotalIva0.add(impuesto.getBaseImponible());
						}
						if(impuesto.getCodigoPorcentaje().equalsIgnoreCase("6")){
							subtotalNoObjetoIVA=subtotalNoObjetoIVA.add(impuesto.getBaseImponible());
						}
						if(impuesto.getCodigoPorcentaje().equalsIgnoreCase("7")){
							subtotalExentoIva=subtotalExentoIva.add(impuesto.getBaseImponible());
						}
					}
					//ice
					if(impuesto.getCodigo().equalsIgnoreCase("3")){
						ice=ice.add(impuesto.getValor());
								
					}
					//IRBPNR
					if(impuesto.getCodigo().equalsIgnoreCase("5")){
						
					}
				}
				detalles.add(_detalle);
			}
		//totalDescuento=totalDescuento.add(detalle.getDescuento());
		mapaReportes.put("subtotal12",subtotal12);
		mapaReportes.put("subtotal0", subtotalIva0);
		mapaReportes.put("subtotalNoObjetoIVA", subtotalNoObjetoIVA);
		mapaReportes.put("subtotalExentoIVA", subtotalExentoIva);
		BigDecimal totalSinImpuestos=BigDecimal.ZERO;
		totalSinImpuestos=totalSinImpuestos.add(subtotal12);
		totalSinImpuestos=totalSinImpuestos.add(subtotalIva0);
		totalSinImpuestos=totalSinImpuestos.add(subtotalNoObjetoIVA);
		totalSinImpuestos=totalSinImpuestos.add(subtotalExentoIva);
		
		
		
		
		mapaReportes.put("subtotalSinImpuestos", totalSinImpuestos);
		mapaReportes.put("totalDescuento", totalDescuento);
		mapaReportes.put("ice",ice);
		mapaReportes.put("iva12",iva12);
		mapaReportes.put("irbpnr", BigDecimal.ZERO);
		mapaReportes.put("propina",BigDecimal.ZERO);
		BigDecimal valorTotal=factura.getInfoFactura().getImporteTotal();
		mapaReportes.put("valorTotal",valorTotal);
		
		
		
		
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mapaReportes.put("tipoEmision", autorizacon.get)  descrifrar del clave de acceso si es normal o contingencia
		
		
	}

}
