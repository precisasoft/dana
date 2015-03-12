package ec.com.vipsoft.web;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import ec.com.vipsoft.ce.backend.service.ConversorNumdocClaveAcceso;
import ec.com.vipsoft.ce.backend.service.GeneradorRide;

/**
 * Servlet implementation class VisorRide
 */
@WebServlet("/visorRide")
public class VisorRide extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @EJB   
	private GeneradorRide generadorRide;
    @EJB
    private ConversorNumdocClaveAcceso conversorNumeroClaveAcceso;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VisorRide() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void service(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
    	//String numdoc = request.getParameter("numdoc");
    	String numdoc=null;
    	String claveAcceso=null;
    	String tipo=null;
    	String ruc=null;
    	Map<String, String[]> parameterMap = request.getParameterMap();
    	Set<String> keySet = parameterMap.keySet();
    	for(String s:keySet){
    		if(s.equalsIgnoreCase("numdoc")){
    			numdoc=request.getParameter("numdoc");
    		}
    		if(s.equalsIgnoreCase("claveAcceso")){
    			claveAcceso=request.getParameter("claveAcceso");
    		}
    		if(s.equalsIgnoreCase("tipo")){
    			tipo=request.getParameter("tipo");
    		}
    		if(s.equalsIgnoreCase("ruc")){
    			ruc=request.getParameter("ruc");
    		}
    	}
    	if(tipo==null){
    		tipo="01";
    	}
    	//String claveAcceso=request.getParameter("claveAcceso");
    	
    	
    	//byte[] fichero=JasperExportManager.exportReportToPdf(print);
		//byte[] fichero = JasperRunManager.runReportToPdf(reporte, parametros, new JRBeanCollectionDataSource(lista));
		byte[] fichero = null;

		try {
			if (claveAcceso == null) {
				fichero = generadorRide.obtenerRide(conversorNumeroClaveAcceso.obtenerClaveAcceso(ruc, tipo, numdoc));
			} else {
				fichero = generadorRide.obtenerRide(claveAcceso);
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.setContentType ("application/pdf");
		StringBuilder nombreArchivo=new StringBuilder("inline; filename=C");
		nombreArchivo.append(claveAcceso);		
		nombreArchivo.append(".pdf");
		response.setHeader ("Content-disposition",nombreArchivo.toString());
		response.setHeader ("Cache-Control", "max-age=30");
		response.setHeader ("Pragma", "No-cache");
		response.setDateHeader ("Expires", 0);
		response.setContentLength (fichero.length);
		ServletOutputStream out;
		out = response.getOutputStream ();
//
		out.write (fichero, 0, fichero.length);
		out.flush ();
		out.close ();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request,response);
	}

}
