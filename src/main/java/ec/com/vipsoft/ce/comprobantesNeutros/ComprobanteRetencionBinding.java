package ec.com.vipsoft.ce.comprobantesNeutros;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

public class ComprobanteRetencionBinding extends BaseComprobanteElectronicoBinding {
	
	@Valid
	private SortedSet<ComprobanteRetencionDetalleBinding>detalles;
	@Pattern(regexp="[0-9]{2}/[0-9]{4}")
	private String periodo;	
	
	public ComprobanteRetencionBinding() {
		super();
		detalles=new TreeSet<>();
	}
	public SortedSet<ComprobanteRetencionDetalleBinding> getDetalles() {
		return detalles;
	}
	public String getPeriodo() {
		if(periodo==null){
			setFechaPeriodoFiscal(new Date());
		}
		return periodo.replace("\r", "").replace("\n", "");
	}
	public void setDetalles(SortedSet<ComprobanteRetencionDetalleBinding> detalles) {
		this.detalles = detalles;
	}	
	public void setFechaPeriodoFiscal(Date fecha){
		SimpleDateFormat sdf=new SimpleDateFormat("MM/yyyy");
		periodo=sdf.format(fecha);
	}
	public void setPeriodo(String periodo) {
		this.periodo = sinCRLF(periodo);
	}
	

}
