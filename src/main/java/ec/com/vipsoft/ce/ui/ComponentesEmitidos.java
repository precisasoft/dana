package ec.com.vipsoft.ce.ui;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

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
	//	actualizarVista();
		
	}
	public ComponentesEmitidos() {
		super();
		
		beanItemContainer=new BeanItemContainer<ComprobanteRideXmlBean>(ComprobanteRideXmlBean.class);	
		grid=new Grid(beanItemContainer);	
		grid.setColumnOrder("tipo","numeroDocumento","claveAcceso","autorizacion","fechaAprobacion");
		grid.getColumn("claveAcceso").setRenderer(new HtmlRenderer());
		grid.getColumn("autorizacion").setRenderer(new HtmlRenderer());
		grid.setSizeFull();
		grid.setSelectionMode(SelectionMode.NONE);
		
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		addComponent(grid);
	}
	@PostConstruct
	public void actualizarVista(){
		System.out.println("llamado postconstruct");
		Long ultimo=0l;
		Set<ComprobanteEmitido> listarSiguientes = listadoComprobantesEmitidos.listarSiguientes(userInfo.getRucEmisor(),userInfo.getMinSearch());
		for(ComprobanteEmitido c:listarSiguientes){
			System.out.println(c.getId());
			ultimo=c.getId();
			ComprobanteRideXmlBean bean=new ComprobanteRideXmlBean();
			StringBuilder sbca=new StringBuilder("<a href='");
			sbca.append(VaadinServlet.getCurrent().getServletContext().getContextPath());
			sbca.append("/VisorRide?claveAcceso=");
			sbca.append(c.getClaveAcceso());
			sbca.append("' target='_blank'>");
			sbca.append(c.getClaveAcceso());
			sbca.append("</a>");
			bean.setClaveAcceso(sbca.toString());
			
			
			
			bean.setNumeroDocumento(c.getNumeroDocumento());
			StringBuilder sba=new StringBuilder("<a href='");
			sba.append(VaadinServlet.getCurrent().getServletContext().getContextPath());
			sba.append("/VisorRide?claveAcceso=");
			sba.append(c.getNumeroAutorizacion());
			sba.append("' target='_blank'>");
			sba.append(c.getNumeroAutorizacion());
			sba.append("</a>");
			bean.setAutorizacion(sba.toString());
			bean.setTipo(c.getTipo());
			if(c.getFechaAutorizacion()!=null)
				bean.setFechaAprobacion(c.getFechaAutorizacion());
			
			
			beanItemContainer.addBean(bean);
			grid.setContainerDataSource(beanItemContainer);
		}
	}
	

	

}
