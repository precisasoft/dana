package ec.com.vipsoft.ce.comprobantesNeutros;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class BaseComprobanteElectronicoBinding implements Serializable{
	
	private static final long serialVersionUID = 4041914625362290164L;
	protected SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	@NotNull(message="este campo no debe ser null")
	@Pattern(regexp="[0-9]{3}",message="el codigo de establecimiento no cumple con formato 999")
	protected String codigoEstablecimiento;

	@NotNull(message="este campo no debe ser null")
	@Pattern(regexp="[0-9]{3}",message="el codigo de punto de venta no cumple con formato 999")
	protected String codigoPuntoVenta;
	protected Date fechaEmision;
        
	@NotNull(message="identificacion del beneficiario no puede ser null")
	protected String identificacionBeneficiario;
	protected Map<String,Object>infoAdicional;
	@NotNull(message="este campo no debe ser null")
	@Size(max=300,message="la razon social no puede ser tan larga")
	protected String razonSocialBeneficiario;
	@NotNull(message="este campo no debe ser null")
	@Size(max=300,message="la razon social no puede ser tan larga")
	protected String razonSocialEmisor;
	@NotNull(message="este campo no debe ser null")
	@Pattern(regexp="[0-9]{13,13}")
	protected String rucEmisor;	
	protected String secuenciaDocumento;
	private InfoTributariaBinding infoTributaria;
	public InfoTributariaBinding getInfoTributaria() {
		if(infoTributaria==null){
			infoTributaria=new InfoTributariaBinding();
		}
		return infoTributaria;
	}
		
	public BaseComprobanteElectronicoBinding() {
		super();
		fechaEmision=new Date();
		infoAdicional=new HashMap<>();
	}
	public String getCodigoEstablecimiento() {
		if(codigoEstablecimiento==null){
			codigoEstablecimiento=infoTributaria.getEstablecimiento();
		}
		return codigoEstablecimiento.replace("\r", "").replace("\n", "");
	}
	public String getCodigoPuntoVenta() {
		if(codigoPuntoVenta==null){
			codigoPuntoVenta=infoTributaria.getPuntoEmision();
		}
		return codigoPuntoVenta.replace("\r", "").replace("\n", "");
	}
	
	public String getFechaEmisionTexto(){
		return sdf.format(getFechaEmision());
	}
	public Date getFechaEmision() {
		return fechaEmision;
	}
	public Map<String, Object> getInfoAdicional() {
		return infoAdicional;
	}
	public String getRazonSocialEmisor() {
		return razonSocialEmisor.replace("\r", "").replace("\n", "");
	}
	public String getRucEmisor() {
		if(rucEmisor==null){
			rucEmisor= infoTributaria.getRucEmisor();
		}
		return rucEmisor.replace("\r", "").replace("\n", "");
	}
	public void setCodigoEstablecimiento(String codigoEstablecimiento) {
		this.codigoEstablecimiento = sinCRLF(codigoEstablecimiento);
	}
	public void setCodigoPuntoVenta(String codigoPuntoVenta) {
		this.codigoPuntoVenta =sinCRLF(codigoPuntoVenta);
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public void setInfoAdicional(Map<String, Object> infoAdicional) {
		this.infoAdicional = infoAdicional;
	}
	public void setRazonSocialEmisor(String razonSocialEmisor) {
		this.razonSocialEmisor = sinCRLF(razonSocialEmisor);
	}
	
	public void setRucEmisor(String rucEmisor) {
		this.rucEmisor = sinCRLF(rucEmisor);
	}
	protected String sinCRLF(String contenido){
		return contenido.replace("\r", "").replace("\n", "");		
	}
	public void anadirInfoAdicional(@Size(max=300,min=1)String clave,@Size(max=300,min=1)String valor){
		if(infoAdicional.size()<15){
			String _clave=sinCRLF(clave);
			String _valor=sinCRLF(valor);
			if((_clave.length()>0)&&(_valor.length()>0)){
				infoAdicional.put(_clave, _valor);	
			}				
		}
		
	}
        public String getCodigoTipoIdentificacionBeneficiario(){
            String retorno=null;
            if(identificacionBeneficiario.length()==10){
                if(identificacionBeneficiario.equalsIgnoreCase("9999999999")){
                    retorno="07";
                    identificacionBeneficiario="9999999999999";
                }else{
                    retorno="05";    
                }                
            }
            if(identificacionBeneficiario.length()==13){
                if(identificacionBeneficiario.equalsIgnoreCase("999999999999")){
                    retorno="07";
                }else{
                    retorno="04";
                }
            }
            if(retorno==null){
                retorno="06";
            }
            return retorno;
        }

    public String getIdentificacionBeneficiario() {
        return identificacionBeneficiario;
    }

    public void setIdentificacionBeneficiario(String identificacionBeneficiario) {
        this.identificacionBeneficiario = identificacionBeneficiario;
    }

    public String getRazonSocialBeneficiario() {
        return razonSocialBeneficiario;
    }

    public void setRazonSocialBeneficiario(String razonSocialBeneficiario) {
        this.razonSocialBeneficiario = razonSocialBeneficiario;
    }

    public String getSecuenciaDocumento() {
        return secuenciaDocumento;
    }

    public void setSecuenciaDocumento(String secuenciaDocumento) {
        this.secuenciaDocumento = secuenciaDocumento;
    }
    

}
