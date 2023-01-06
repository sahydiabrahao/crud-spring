package br.sahydi.crudspring.controller;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.sahydi.crudspring.exception.ObjectErrorModel;
import br.sahydi.crudspring.service.SendEmailService;
import br.sahydi.crudspring.model.UserModel;
import br.sahydi.crudspring.repository.UserRepository;
import br.sahydi.crudspring.service.UDSuserdatailsservice;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController 
@RequestMapping(value = "/login")public class LoginController {
    
	@Autowired 
	private UDSuserdatailsservice userDatailsService;

    @Autowired 
	private UserRepository userRepository;

	@Autowired 
	private SendEmailService sendEmailService;

	//Pesquisar Usuário por Email
	@GetMapping(value = "/{user_email}", produces = "application/json")
	public ResponseEntity<UserModel> userfindByEmail(@PathVariable (value = "user_email") String user_email) {
		
		UserModel user = userRepository.userFindByEmail(user_email);
		return new ResponseEntity<UserModel>(user, HttpStatus.OK);
	}

	//Registrar Usuário
	@PostMapping(value = "/register", produces = "application/json")
	public ResponseEntity<UserModel> userRegister(@RequestBody UserModel user) throws IOException {

		//Salvar senha criptografada
		String user_password_encrypted = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(user_password_encrypted );

		UserModel userSave = userRepository.save(user);

		//Inserir Role Ex: 1 ROLE_ADMIN 2 ROLE_USER
		userDatailsService.defaultROLE(userSave.getId());

		return new ResponseEntity<UserModel>(userSave, HttpStatus.OK);
	}
   
	//Recuperar Senha do Usuario
	@PostMapping(value = "/recover-password")
	public ResponseEntity<ObjectErrorModel> userRecoverPassword (@RequestBody UserModel user) throws Exception{

		ObjectErrorModel objectErrorModel = new ObjectErrorModel();

		UserModel user_email = userRepository.userFindByEmail(user.getEmail());

		//Não tem cadastro
		if(user_email == null){	
			objectErrorModel.setCode("404");
			objectErrorModel.setError("Usuário não encontrado");
		}

		//Envia email de recuperação
		else{
			//Cria senha nova
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String user_new_password = dateFormat.format(Calendar.getInstance().getTime());
			String user_new_password_encrypted = new BCryptPasswordEncoder().encode(user_new_password);

			//Salva no BD
			userRepository.updatePassword(user_new_password_encrypted , user.getId());

			//Envia senha por email
			sendEmailService.sendEmail(user.getEmail(), "Recuperação de Senha", "Nova Senha: " + user_new_password );

			objectErrorModel.setCode("200");
			objectErrorModel.setError("Nova senha enviada por email");
		}

		return new ResponseEntity<ObjectErrorModel>(objectErrorModel, HttpStatus.OK);
	}



}
