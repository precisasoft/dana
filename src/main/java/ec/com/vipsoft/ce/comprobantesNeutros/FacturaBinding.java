package ec.com.vipsoft.ce.comprobantesNeutros;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class FacturaBinding extends BaseComprobanteElectronicoBinding {
	
	private boolean debeGenerarGuiaRemision;	
	@Valid
	private List<FacturaDetalleBinding>detalles;
	@Size(max=300,message="dirección no debe tener mas de 300 carateres")
	private String direccionBeneficiario;
	@Size(max=17,message="el numero de guia de remision es muy largo")
	protected String guiaRemision;
	@Pattern(regexp="[0-9]{10,13}",message="la identificacion del transportista es invalida")
	protected String idTransportista;
	@Size(max=7,min=7,message="la placa vehicular debe tener 7 caracteres")
	protected String placa;
	@Size(max=300)
	protected String razonSocialTransportista;
	@Min(value = 0,message="valor no puede ser negativo")
	private BigDecimal subtotalDescuento;
	@Min(value = 0 ,message="valor no puede ser negativo")
	private BigDecimal subtotalIce;
	@Min(value = 0,message="valor no puede ser negativo")
	private BigDecimal subtotalIva0;
	@Min(value = 0,message="valor no puede ser negativo")
	private BigDecimal subtotalIva12;
	@Min(value = 0,message="valor no puede ser negativo")
	private BigDecimal total;
    @Pattern(regexp = "[0-9]{3}-[0-9]{3}-[0-9]{9}")
	private String numeroGuiaRemision;
    private String codigoEstablecimientoDestino;        
    private String formaPago;

    public String getCodigoEstablecimientoDestino() {
        return codigoEstablecimientoDestino;
    }

    public void setCodigoEstablecimientoDestino(String codigoEstablecimientoDestino) {
        this.codigoEstablecimientoDestino = codigoEstablecimientoDestino;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }
        

    public String getNumeroGuiaRemision() {
        return numeroGuiaRemision;
    }

    public void setNumeroGuiaRemision(String numeroGuiaRemision) {
        this.numeroGuiaRemision = numeroGuiaRemision;
    }
        
	public FacturaBinding() {
		super();
		detalles=new ArrayList<>();
	}
	public List<FacturaDetalleBinding> getDetalles() {
		return detalles;
	}
	public String getDireccionBeneficiario() {
		return direccionBeneficiario.replace("\r", "").replace("\n", "");
	}
	public String getGuiaRemision() {
		return guiaRemision.replace("\r", "").replace("\n", "");
	}
	public String getIdTransportista() {
		return idTransportista.replace("\r", "").replace("\n", "");
	}
	public String getPlaca() {
		return placa.replace("\r", "").replace("\n", "");
	}
	public String getRazonSocialTransportista() {
		return razonSocialTransportista.replace("\r", "").replace("\n", "");
	}
	public BigDecimal getSubtotalDescuento() {
		return subtotalDescuento;
	}
	public BigDecimal getSubtotalIce() {
		return subtotalIce;
	}
	public BigDecimal getSubtotalIva0() {
		return subtotalIva0;
	}
	public BigDecimal getSubtotalIva12() {
		return subtotalIva12;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public boolean isDebeGenerarGuiaRemision() {
		return debeGenerarGuiaRemision;
	}
	public void setDebeGenerarGuiaRemision(boolean debeGenerarGuiaRemision) {
		this.debeGenerarGuiaRemision = debeGenerarGuiaRemision;
	}
	public void setDetalles(List<FacturaDetalleBinding> detalles) {
		this.detalles = detalles;
	}
	public void setDireccionBeneficiario(String direccionBeneficiario) {
		this.direccionBeneficiario = direccionBeneficiario;
	}
	public void setGuiaRemision(String guiaRemision) {
		this.guiaRemision = guiaRemision;
	}
	public void setIdTransportista(String idTransportista) {
		this.idTransportista = idTransportista;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public void setRazonSocialTransportista(String razonSocialTransportista) {
		this.razonSocialTransportista = razonSocialTransportista;
	}
	public void setSubtotalDescuento(BigDecimal subtotalDescuento) {
		this.subtotalDescuento = subtotalDescuento;
	}
	public void setSubtotalIce(BigDecimal subtotalIce) {
		this.subtotalIce = subtotalIce;
	}
	public void setSubtotalIva0(BigDecimal subtotalIva0) {
		this.subtotalIva0 = subtotalIva0;
	}
	public void setSubtotalIva12(BigDecimal subtotalIva12) {
		this.subtotalIva12 = subtotalIva12;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}	

    public BigDecimal calculaDescuento() {
        BigDecimal totalDescuento=BigDecimal.ZERO;
        for(FacturaDetalleBinding detalle:detalles){
            totalDescuento=totalDescuento.add(detalle.getDescuento());
        }
        return totalDescuento.setScale(2,RoundingMode.HALF_UP);
    }
}
