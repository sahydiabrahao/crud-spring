package br.sahydi.crudspring.other;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import br.sahydi.crudspring.CrudSpringApplication;

public class Servlet extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CrudSpringApplication.class);
	}

}

