package br.sahydi.crudspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.sahydi.crudspring.model.UserModel;
import br.sahydi.crudspring.repository.UserRepository;

//VALIDAÇÃO DE USUARIO
@Service
public class UDSuserdatailsservice implements UserDetailsService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String user_name) throws UsernameNotFoundException {
		
		/*Consulta no banco o usuario*/
		UserModel user = userRepository.userFindByEmail(user_name);
		
		if (user == null) {
			throw new UsernameNotFoundException("Usuário não foi encontrado");
		}
		
		return new User(
            user.getuser_email(),
            user.getuser_password(),
            user.getAuthorities() //Roles
        );
	}

    public void defaultROLE(Long user_id) {
		//Nome da constraint
		String constraintRoleName = userRepository.searchConstraintRoleName();
		//Deleta constraint
		if(constraintRoleName != null) {
			jdbcTemplate.execute(" ALTER table usernames_role DROP CONSTRAINT " + constraintRoleName);
		}
		
		//Define ROLE_USER para o ID
		userRepository.insertRoleUser(user_id);
    }

}
