package ec.com.vipsoft.ce.ui;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

import ec.com.vipsoft.ce.backend.managedbean.UserInfo;
import ec.com.vipsoft.ce.backend.service.ListarComprobantesEmitidos;

@CDIView("comprobantes")
public class ComponentesEmitidos extends VerticalLayout implements View{

	@EJB
	private ListarComprobantesEmitidos listadoComprobantesEmitidos;
	@Inject 
	private UserInfo userInfo;
	private static final long serialVersionUID = 5820107472515714341L;
	private Grid grid;
	private BeanItemContainer<ComprobanteRideXmlBean>beanItemContainer;
	@Override
	public void enter(ViewChangeEvent event) {
		actualizarVista();
		
	}
	public ComponentesEmitidos() {
		super();
		beanItemContainer=new BeanItemContainer<ComprobanteRideXmlBean>(ComprobanteRideXmlBean.class);	
		grid=new Grid(beanItemContainer);
		addComponent(grid);
	}
	@PostConstruct
	public void actualizarVista(){
		Long ultimo=0l;
		List<ComprobanteEmitido> listarSiguientes = listadoComprobantesEmitidos.listarSiguientes(userInfo.getRucEmisor(),userInfo.getMinSearch());
		for(ComprobanteEmitido c:listarSiguientes){
			ultimo=c.getId();
			ComprobanteRideXmlBean bean=new ComprobanteRideXmlBean();
			Link claveAcceso=new Link(c.getClaveAcceso(),new ExternalResource(VaadinServlet.getCurrent().getServletContext().getContextPath()+"VisorRide?claveAcceso="+c.getClaveAcceso()));
			claveAcceso.setTargetName("_blank");
			bean.setClaveAcceso(claveAcceso);
			bean.setNumeroDocumento(c.getNumeroDocumento());
			Link autorizacion=new Link(c.getClaveAcceso(),new ExternalResource(VaadinServlet.getCurrent().getServletContext().getContextPath()+"VisorRide?claveAcceso="+c.getClaveAcceso()));
			autorizacion.setTargetName("_blank");
			bean.setAutorizacion(autorizacion);
			bean.setTipo(c.getTipo());
			if(c.getFechaAutorizacion()!=null)
				bean.setFechaAprobacion(c.getFechaAutorizacion());
			
			
			
			
			
			beanItemContainer.addBean(bean);
			
		}
	}
	

	

}
