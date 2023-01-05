package br.sahydi.crudspring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.sahydi.crudspring.service.UDSuserdatailsservice;

@Configuration
@EnableWebSecurity
public class JWTweb extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UDSuserdatailsservice udsUserDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Ativa proteção contra usuários não validados por token
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		// URI com acesso sem email
		.disable().authorizeRequests().antMatchers("/").permitAll()		
		.antMatchers("/index", "/login/**").permitAll()
		
		// Liberação do CORS
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		
		// URL de logout - Redireciona após deslogar do sistema
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		// Mapeia URL de logout e invalida o usuário
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		// Filtra requisições de email para autenticação
		.and().addFilterBefore(new JWTlogin("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
		
		// Filtra demais requisições para verificar a presença do TOKEN JWT no HEADER HTTP
		.addFilterBefore(new JWTautentication(), UsernamePasswordAuthenticationFilter.class);		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		/*Service que irá consultar o usuário no banco de dados*/	
		auth.userDetailsService(udsUserDetailsService)
		
		/*Padrão de codificação de senha*/
		.passwordEncoder(new BCryptPasswordEncoder());
	}

}
