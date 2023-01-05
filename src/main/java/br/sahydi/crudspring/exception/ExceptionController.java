package br.sahydi.crudspring.exception;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
	
	//TRATAMENTO DE ERROS GERAIS
	@Override
	@ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		String msg = "";

		//Pegando mensagem do erro 
		if (ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
			for (ObjectError objectError : list) {
				msg += objectError.getDefaultMessage() + "\n";
			}

		}else{
			msg = ex.getMessage();
		}

		ObjectErrorModel objectErrorModel = new ObjectErrorModel();
		objectErrorModel.setError(ex.getMessage());
		objectErrorModel.setCode(status.value() + " ==> " + status.getReasonPhrase());
		
		return new ResponseEntity<>(objectErrorModel, headers, status);
	}	

	//TRATAMENTO DE ERROS DO BANCO DE DADOS
	@ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, PSQLException.class, SQLException.class})
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {
		
		String msg = "";

		//Pegando mensagem do erro 
		if (ex instanceof DataIntegrityViolationException) {
			msg = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();

		}else if (ex instanceof ConstraintViolationException) {
			msg = ((ConstraintViolationException) ex).getCause().getCause().getMessage();

		}else if (ex instanceof PSQLException) {
			msg = ((PSQLException) ex).getCause().getCause().getMessage();

		}else if (ex instanceof SQLException) {
			msg = ((SQLException) ex).getCause().getCause().getMessage();

		}else{
			msg = ex.getMessage();
		}
		
		ObjectErrorModel objectErrorModel = new ObjectErrorModel();
		objectErrorModel.setError(msg);
		objectErrorModel.setCode(HttpStatus.INTERNAL_SERVER_ERROR + " ==> " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		
		return new ResponseEntity<>(objectErrorModel, HttpStatus.INTERNAL_SERVER_ERROR);
	}	

}
