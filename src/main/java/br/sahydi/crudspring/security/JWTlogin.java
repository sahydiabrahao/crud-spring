package br.sahydi.crudspring.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.sahydi.crudspring.model.UserModel;

// Estabelece gerenciador de token
public class JWTlogin extends AbstractAuthenticationProcessingFilter {

	// Configurando o gerenciador de autenticação
	protected JWTlogin(String url, AuthenticationManager authenticationManager) {
		
		// Obriga a autenticar a url
		super(new AntPathRequestMatcher(url));
		
		// Gerenciador de autenticação
		setAuthenticationManager(authenticationManager);
	}

	// Retorna o usuário ao processar a autenticação
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		// Está pegando o token para validar
		UserModel user = new ObjectMapper().readValue(request.getInputStream(), UserModel.class);
		
		// Retorna o usuário email, senha e acesso		
		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(
					user.getuser_email(), user.getuser_password()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
	
		new JWTtoken().addAuthentication(response, authResult.getName());
	}

}
