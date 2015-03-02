package ec.com.vipsoft.ce.utils;


public class DefaultUtilClaveAcceso implements UtilClaveAcceso {

	/* (non-Javadoc)
	 * @see ec.com.vipsoft.ce.utils.UtilClaveAcceso#esEnPruebas(java.lang.String)
	 */
	@Override
	public boolean esEnPruebas(String claveAcceso){
		boolean retorno=false;
		if(claveAcceso.substring(23,24).equalsIgnoreCase("1")){
			retorno=true;
		}
		return retorno;		
	}

	@Override
	public String obtenerCodigoEstablecimiento(String claveAcceso) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String obtenerCodigoPuntoEmision(String claveAcceso) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String obtemerRucEmisor(String claveAcceso) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean esEnContingencia(String claveAcceso) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String obtenerSecuanciaDocumento(String claveACceso) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String obtenerAmbiente(String claveAcceso) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String obtenerTipoDocumento(String claveAcceso) {
		// TODO Auto-generated method stub
		return null;
	}
}
