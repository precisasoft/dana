package ec.com.vipsoft.ce.ui;

import org.apache.shiro.SecurityUtils;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
@CDIView("portal")
public class PortalView extends VerticalLayout implements View{

	private Button botonSalir;
	public PortalView() {
		super();
		setMargin(true);
		setSpacing(true);
		botonSalir=new Button("salir");
		botonSalir.addClickListener(event -> SecurityUtils.getSubject().logout());
		addComponent(botonSalir);
	}
	@Override
	public void enter(ViewChangeEvent event) {
//		if(SecurityUtils.getSubject().isAuthenticated()){
//			if(!SecurityUtils.getSubject().hasRole("lector")){
//				UI.getCurrent().getNavigator().navigateTo("login");
//			}
//		}else{
//			UI.getCurrent().getNavigator().navigateTo("login");
//		}
		
	}

}
