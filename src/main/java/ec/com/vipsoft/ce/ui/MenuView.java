package ec.com.vipsoft.ce.ui;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

@CDIView("menu")
public class MenuView extends VerticalLayout implements View{
	private Button botonFactura;

	public MenuView() {
		super();
		setMargin(true);
		setSpacing(true);
		botonFactura=new Button("FA");
		addComponent(botonFactura);
		
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
	
		
	}
	

}
