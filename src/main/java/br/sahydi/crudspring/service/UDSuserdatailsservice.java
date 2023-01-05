package br.sahydi.crudspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.sahydi.crudspring.model.UsernameModel;
import br.sahydi.crudspring.repository.UsernameRepository;

//VALIDAÇÃO DE USUARIO
@Service
public class UDSuserdatailsservice implements UserDetailsService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UsernameRepository usernameRepository;

	@Override
	public UserDetails loadUserByUsername(String username_name) throws UsernameNotFoundException {
		
		/*Consulta no banco o usuario*/
		UsernameModel username = usernameRepository.usernameFindByEmail(username_name);
		
		if (username == null) {
			throw new UsernameNotFoundException("Usuário não foi encontrado");
		}
		
		return new User(
            username.getUsername_email(),
            username.getUsername_password(),
            username.getAuthorities() //Roles
        );
	}

    public void defaultROLE(Long username_id) {
		//Nome da constraint
		String constraintRoleName = usernameRepository.searchConstraintRoleName();
		//Deleta constraint
		if(constraintRoleName != null) {
			jdbcTemplate.execute(" ALTER table usernames_role DROP CONSTRAINT " + constraintRoleName);
		}
		
		//Define ROLE_USER para o ID
		usernameRepository.insertRoleUsername(username_id);
    }

}
