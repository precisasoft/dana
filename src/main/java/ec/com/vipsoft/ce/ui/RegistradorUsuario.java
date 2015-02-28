package ec.com.vipsoft.ce.ui;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.shiro.authc.credential.DefaultPasswordService;

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
		
		em.persist(usuario);
		em.persist(rolUsuario);
		return retorno;
	}
}
