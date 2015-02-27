package ec.com.vipsoft.ce.ui;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
@CDIView("portal")
public class PortalView extends VerticalLayout implements View{

	private Button botonLogout;
	public PortalView() {
		super();
		setMargin(true);
		setSpacing(true);
		botonLogout=new Button("LOGOUT");
		addComponent(botonLogout);
	}
	@Override
	public void enter(ViewChangeEvent event) {
	
		
	}

}
