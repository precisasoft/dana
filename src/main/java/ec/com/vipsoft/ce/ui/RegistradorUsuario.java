package ec.com.vipsoft.ce.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.shiro.authc.credential.DefaultPasswordService;

import ec.com.vipsoft.ce.backend.managedbean.UserInfo;
import ec.com.vipsoft.erp.abinadi.dominio.Entidad;
import ec.com.vipsoft.erp.abinadi.dominio.Establecimiento;
import ec.com.vipsoft.erp.abinadi.dominio.PuntoVenta;

@Stateless
public class RegistradorUsuario {

	
	@PersistenceContext
	private EntityManager em;
	public boolean registrarUsuario(String userName,String password,String nombres,String apellidos){
		boolean retorno=false;
		User usuario=new User();
		usuario.setUsername(userName);
		DefaultPasswordService dpasswordService=new DefaultPasswordService();
		usuario.setPassword(dpasswordService.encryptPassword(password));
		usuario.setNombre(nombres);
		usuario.setApellidos(apellidos);
		usuario.setActivo(false);
		
		UserRolePK useRole=new UserRolePK();
		useRole.setUsername(userName);
		useRole.setRoleName("usuario");
		UserRole rolUsuario=new UserRole();
		rolUsuario.setId(useRole);
		
		Query q=em.createQuery("select r from RolesPermission r ");
		List<RolesPermission>lista=q.getResultList();
		if(lista.isEmpty()){
			RolesPermissionPK rolPermision=new RolesPermissionPK();
			rolPermision.setRoleName("usuario");
			rolPermision.setRoleper("portal");
			RolesPermission rolpermision=new RolesPermission();
			rolpermision.setId(rolPermision);
			em.persist(rolpermision);
		}
		//verificar si exisite usuario en empresa ..
		StringTokenizer stringTokenizer=new StringTokenizer(userName,"@");
		String user=stringTokenizer.nextToken();
		if(stringTokenizer.hasMoreTokens()){
			String dominio=stringTokenizer.nextToken();
			if(dominio!=null){
				Query qporDominio=em.createQuery("select e from Entidad e where e.dominioInternet=?1");
				qporDominio.setParameter(1, dominio);
				List<Entidad>listadoEntidad=qporDominio.getResultList();
				if(!listadoEntidad.isEmpty()){
					Entidad entidac=em.find(Entidad.class,listadoEntidad.get(0).getId());
					if((entidac.getUsuarioAdministrador()==null)||(entidac.getUsuarioAdministrador().length()<2)){
						UserRolePK rolAdministrador=new UserRolePK();
						rolAdministrador.setRoleName("administrador");
						rolAdministrador.setUsername(userName);
						UserRole rol2=new UserRole();
						rol2.setId(rolAdministrador);
						em.persist(rol2);
						UserRolePK rolOperador=new UserRolePK();
						rolOperador.setRoleName("operador");
						rolOperador.setUsername(userName);
						UserRole rol3=new UserRole();
						rol3.setId(rolOperador);
						em.persist(rol3);
						entidac.setUsuarioAdministrador(userName);
						
					}else{
						UserRolePK rolOperador=new UserRolePK();
						rolOperador.setRoleName("operador");
						rolOperador.setUsername(userName);
						UserRole rol3=new UserRole();
						rol3.setId(rolOperador);
						em.persist(rol3);
					}
				}				
			}
		}
		em.persist(usuario);
		em.persist(rolUsuario);
		return retorno;
	}
	public Map<String,String> llenarUserInfo(String userName){
		Map<String,String>mapa=new HashMap<String,String>();
		Query q=em.createQuery("select p from PuntoVenta p where p.usuarioPorDefecto=?1");
		q.setParameter(1, userName);
		List<PuntoVenta>listaPuntoVenta=q.getResultList();
		if(!listaPuntoVenta.isEmpty()){
			PuntoVenta pos=em.find(PuntoVenta.class, listaPuntoVenta.get(0).getId());
			if(pos!=null){
				Establecimiento establecimiento=pos.getEstablecimiento();
				mapa.put("idEstablecimiento",String.valueOf(establecimiento.getId()));
				mapa.put("codigoEstablecimiento", establecimiento.getCodigo());
				mapa.put("puntoEmision", pos.getCodigoPuntoVenta());
				mapa.put("idPuntoEmision", String.valueOf(pos.getId()));
				Entidad e=em.find(Entidad.class, establecimiento.getEntidad().getId());
				mapa.put("rucEmisor", e.getRuc());
						
				
			}
		}
		return mapa;
		
	}
}
