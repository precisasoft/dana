package ec.com.vipsoft.ce.ui;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme("mytheme")
@SuppressWarnings("serial")
@CDIUI("")
public class MiUI extends UI {

	@Inject
	private CDIViewProvider viewProvider;
	@Override
	protected void init(VaadinRequest request) {
		
		Navigator navigator=new Navigator(this, this);
		navigator.addProvider(viewProvider);
		navigator.setErrorView(ErrorView.class);
		setNavigator(navigator);
		if(!SecurityUtils.getSubject().isAuthenticated()){
			UI.getCurrent().getNavigator().navigateTo("login");
		}	else{
			if(SecurityUtils.getSubject().hasRole("operador")){
				
			}
			if(SecurityUtils.getSubject().hasRole("usuario")){
				UI.getCurrent().getNavigator().navigateTo("portal");
			}
		}

	}

}
