package ec.com.vipsoft.ce.ui;

import org.apache.shiro.SecurityUtils;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@CDIView("menu")
public class MenuView extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5079435157736277851L;
	private Button botonFactura;
	private Button botonSalir;

	public MenuView() {
		super();
		setMargin(true);
		setSpacing(true);
		botonFactura=new Button("FA");
		addComponent(botonFactura);
		botonSalir=new Button("Salir");
		botonSalir.addClickListener(event -> {SecurityUtils.getSubject().logout();
			UI.getCurrent().getNavigator().navigateTo("login");
		});
		addComponent(botonSalir);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
	
//		if(SecurityUtils.getSubject().isAuthenticated()){
//			if(!SecurityUtils.getSubject().hasRole("admin")){
//				UI.getCurrent().getNavigator().navigateTo("portal");	
//			}
//		}else{
//			//UI.getCurrent().getNavigator().navigateTo("login");
//		}
	}
	

}
