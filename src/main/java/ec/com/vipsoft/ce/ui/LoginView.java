package ec.com.vipsoft.ce.ui;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
@CDIView("login")
public class LoginView extends VerticalLayout implements View{

	private TextField usuario;
	private PasswordField password;
	private Button botonLogIn;
	private CheckBox rememberMe;
	public LoginView() {
		super();
		setMargin(true);
		setSpacing(true);
		usuario=new TextField();
		password=new PasswordField();
		botonLogIn=new Button("LOG IN");
		rememberMe=new CheckBox("remember me");
		addComponent(usuario);
		addComponent(password);
		addComponent(rememberMe);
		addComponent(botonLogIn);
		
	}
	@Override
	public void enter(ViewChangeEvent event) {
		if(SecurityUtils.getSubject().isAuthenticated()){
			SecurityUtils.getSubject().logout();
		}
		
	}
	@PostConstruct
	public void postconstruir(){
		
		botonLogIn.addClickListener(event -> {
			System.out.println("se ha hecho click");
			Subject currentUser = SecurityUtils.getSubject();
			if ( !currentUser.isAuthenticated() ) {
			    //collect user principals and credentials in a gui specific manner 
			    //such as username/password html form, X509 certificate, OpenID, etc.
			    //We'll use the username/password example here since it is the most common.
			    UsernamePasswordToken token = new UsernamePasswordToken(usuario.getValue(),password.getValue());

			    //this is all you have to do to support 'remember me' (no config - built in!):
			  //  
			    
			    	token.setRememberMe(rememberMe.getValue());
			    
			    try{
			    currentUser.login(token);
			    if(currentUser.isAuthenticated()){
			    	if(currentUser.hasRole("admin")){
			    		UI.getCurrent().getNavigator().navigateTo("menu");
			    	}
			    	if(currentUser.hasRole("guest")){
			    		UI.getCurrent().getNavigator().navigateTo("portal");
			    	}
			    }
			    }
			    catch ( UnknownAccountException uae ) {
			    	Notification.show("No existe la cuenta", Notification.TYPE_ERROR_MESSAGE);
			    	
			    } catch ( IncorrectCredentialsException ice ) {
			        Notification.show("Credenciales incorrectas", Notification.TYPE_ERROR_MESSAGE);
			    } catch ( LockedAccountException lae ) {
			    	Notification.show("Cuenta bloqueada", Notification.TYPE_ERROR_MESSAGE);
			  
			       
			    } catch ( AuthenticationException ae ) {
			    	Notification.show("error", Notification.TYPE_ERROR_MESSAGE);
			    }
			
			}
		});
	}
}
