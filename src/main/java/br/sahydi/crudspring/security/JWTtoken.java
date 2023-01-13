package br.sahydi.crudspring.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.sahydi.crudspring.other.ContextLoad;
import br.sahydi.crudspring.model.UserModel;
import br.sahydi.crudspring.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTtoken {

	// Tempo de validade do TOKEN - 2 dias
	private static final long EXPIRATION_TIME = 172800000;
	
	// Senha única para compor a autenticação e ajudar na segurança
	private static final String SECRET = "SenhaExtremamenteSecreta";
	
	// Prefixo padrão de Token
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	// Gerando token de autenticação e adicionando ao cabeçalho e resposta Http
	public void addAuthentication(HttpServletResponse response, String user) throws IOException {
		
		// Montagem do Token
		String JWT = Jwts.builder() // gerador de token
				.setSubject(user) // adiciona usuário
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // tempo expiração
				.signWith(SignatureAlgorithm.HS512, SECRET) // algoritmo de geração de senha
				.compact(); // compactação String
		
		String token = TOKEN_PREFIX + " " + JWT; // Bearer 3498hih345jkh345ui53iu5hyi
		
		// adiciona token no cabeçalho http
		response.addHeader(HEADER_STRING, token); // Authorization: Bearer 3498hih345jkh345ui53iu5hyi
		
		liberarCORS(response);
		
		// adiciona token como resposta no corpo do http
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}
	
	// Retorna o usuário validado com token ou caso não seja válido retorna null
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		// Pega o token enviado no cabeçalho Http
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {			
			// Faz validação do Token do usuário na requisição
			String user_email = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody()
					.getSubject();	
			
			if (user_email != null) {					
				UserModel user = (UserModel) ContextLoad
						.getApplicationContext()
						.getBean(UserRepository.class)
						.userFindByEmail(user_email);
				
				if (user != null) {
					return new UsernamePasswordAuthenticationToken(
							user.getEmail(), 
							user.getPassword(),
							user.getAuthorities());
				}
			}
		}	
		liberarCORS(response);
		return null;
	}

	// CORS policy
	private void liberarCORS(HttpServletResponse response) {
		
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");		
		}
		
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");		
		}
		
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");		
		}
		
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");		
		}
	}

}
