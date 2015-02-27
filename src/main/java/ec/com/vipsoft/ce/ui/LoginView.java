package ec.com.vipsoft.ce.ui;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
@CDIView("login")
public class LoginView extends VerticalLayout implements View{

	private TextField usuario;
	private PasswordField password;
	private Button botonLogIn;
	public LoginView() {
		usuario=new TextField();
		password=new PasswordField();
		botonLogIn=new Button("LOG IN");
		addComponent(usuario);
		addComponent(password);
		addComponent(botonLogIn);
		
	}
	@Override
	public void enter(ViewChangeEvent event) {
		
		
	}

}
