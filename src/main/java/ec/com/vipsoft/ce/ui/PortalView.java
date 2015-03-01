package ec.com.vipsoft.ce.ui;

import org.apache.shiro.SecurityUtils;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
@CDIView("portal")
public class PortalView extends VerticalLayout implements View{

	private Button botonSalir;
	private Button preferencias;
	public PortalView() {
		super();
		setMargin(true);
		setSpacing(true);
		
		HorizontalLayout layoutsessionPreferencias=new HorizontalLayout();
		layoutsessionPreferencias.setSpacing(true);
		
		
		preferencias=new Button("preferencias");
		botonSalir=new Button("salir");
		
		Label labelEspacio=new Label("  ");
		labelEspacio.setWidth("100px");
		botonSalir.addClickListener(event -> {SecurityUtils.getSubject().logout();
			UI.getCurrent().getNavigator().navigateTo("login");
		});
		
		layoutsessionPreferencias.addComponent(labelEspacio);
		layoutsessionPreferencias.addComponent(preferencias);
		layoutsessionPreferencias.addComponent(botonSalir);
		
		addComponent(layoutsessionPreferencias);
		setComponentAlignment(layoutsessionPreferencias, Alignment.TOP_RIGHT);
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
