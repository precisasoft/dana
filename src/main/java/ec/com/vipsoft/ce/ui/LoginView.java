package ec.com.vipsoft.ce.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.subject.Subject;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@CDIView("login")
public class LoginView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3823202018387876166L;
	private TextField usuario;
	private PasswordField password;
	private Button botonLogIn;
	private CheckBox rememberMe;
	@Inject
	private RegistradorUsuario registradorUsuario;

	public LoginView() {
		super();
		setMargin(true);
		setSpacing(true);
		usuario = new TextField();
		usuario.setInputPrompt("email@domain.com");
		password = new PasswordField();
		botonLogIn = new Button("LOG IN");
		rememberMe = new CheckBox("registrame");
		addComponent(usuario);
		addComponent(password);
		addComponent(rememberMe);
		addComponent(botonLogIn);
		// Sha256Hash hasher=new Sha256Hash();

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// if(SecurityUtils.getSubject().isAuthenticated()){
		// SecurityUtils.getSubject().logout();
		// }

	}

	@PostConstruct
	public void postconstruir() {

		botonLogIn.addClickListener(event -> {
			System.out.println("se ha hecho click");
			if (rememberMe.getValue()) {
				registradorUsuario.registrarUsuario(usuario.getValue(),	password.getValue(), "", "");
			} else {
				UsernamePasswordToken uptoken=new UsernamePasswordToken();
				uptoken.setPassword(password.getValue().toCharArray());
				uptoken.setUsername(usuario.getValue());
				Subject currentUser=SecurityUtils.getSubject();
				try{
					currentUser.login(uptoken);
					if(currentUser.hasRole("operador")){
						UI.getCurrent().getNavigator().navigateTo("menu");
					}else{
						if(currentUser.hasRole("usuario")){
							UI.getCurrent().getNavigator().navigateTo("portal");
						}
					}
				}catch(AuthenticationException e){
					Notification.show("error", e.getMessage(),Type.ERROR_MESSAGE);
				}
			}
		});
	}
}
