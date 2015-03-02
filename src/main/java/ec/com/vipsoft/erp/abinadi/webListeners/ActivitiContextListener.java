/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.vipsoft.erp.abinadi.webListeners;

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
 * Web application lifecycle listener.
 *
 * @author chrisvv
 */
@WebListener
public class ActivitiContextListener implements ServletContextListener {

	private ProcessEngine defaultProcessEngine;
//    @Inject
//    AlcanceAplicacion alcanceAplicacion;
    @Override
    public void contextInitialized(ServletContextEvent sce) {                
       LOG.info("<------------------------  INICIANDO LA APPLICACION ABINADI_ERP ---------------------->");
       LOG.info("<------------------------  INICIANDO LA APPLICACION ABINADI_ERP ---------------------->");
         defaultProcessEngine= ProcessEngines.getDefaultProcessEngine();        
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();                   
        repositoryService.createDeployment().addClasspathResource("procesoEnvio.bpmn").deploy();
        LOG.info("Number of process definitions :"+repositoryService.createProcessDefinitionQuery().count());
        LOG.info("<------------------------  PROCESOS DESPLEGADOS ---------------------->");
        List<ProcessDefinition> lista = repositoryService.createProcessDefinitionQuery().list();
		for(ProcessDefinition d:lista){
				LOG.info("deployment id "+d.getDeploymentId()+"   key  "+d.getKey());				
		}
		LOG.info("<------------------------  FIN DE LISTA DE PROCESOS DESPLEGADOS ---------------------->");
//      alcanceAplicacion.setProcessEngine(defaultProcessEngine);
    }
    private static final Logger LOG = Logger.getLogger(ActivitiContextListener.class.getName());

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ProcessEngines.destroy();
    }
}
