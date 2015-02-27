package ec.com.vipsoft.ce.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@SuppressWarnings("serial")
@CDIUI("")
public class MiUI extends UI {

	@Override
	protected void init(VaadinRequest request) {
		Button boton=new Button("boton");
		VerticalLayout layout=new VerticalLayout();
		layout.addComponent(boton);
		setContent(layout);

	}

}
