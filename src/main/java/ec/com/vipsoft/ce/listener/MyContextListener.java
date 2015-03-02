package ec.com.vipsoft.ce.listener;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;

/**
 * Application Lifecycle Listener implementation class MyContextListener
 *
 */
@WebListener
public class MyContextListener extends org.apache.shiro.web.env.EnvironmentLoaderListener implements ServletContextListener {

	 private static final Logger LOG = Logger.getLogger(MyContextListener.class.getName());
	private ProcessEngine defaultProcessEngine;
    /**
     * Default constructor. 
     */
    public MyContextListener() {
    	super();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         super.contextDestroyed(arg0);
         ProcessEngines.destroy();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         super.contextInitialized(arg0);
         defaultProcessEngine= ProcessEngines.getDefaultProcessEngine();        
         RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
         LOG.info("Number of process definitions :"+repositoryService.createProcessDefinitionQuery().count());
         LOG.info("<------------------------  PROCESOS DESPLEGADOS ---------------------->");
         List<ProcessDefinition> lista = repositoryService.createProcessDefinitionQuery().list();
 		for(ProcessDefinition d:lista){
 				LOG.info("deployment id "+d.getDeploymentId()+"   key  "+d.getKey());				
 		}
 		LOG.info("<------------------------  FIN DE LISTA DE PROCESOS DESPLEGADOS ---------------------->");
    }
	
}
