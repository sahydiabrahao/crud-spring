package br.sahydi.crudspring.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi.ecCVCDSA224;
import org.hibernate.mapping.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;
import br.sahydi.crudspring.model.ContactModel;
import br.sahydi.crudspring.model.PhoneModel;
import br.sahydi.crudspring.repository.ContactRepository;
import br.sahydi.crudspring.repository.PhoneRepository;
import br.sahydi.crudspring.service.ReportService;

@RestController /* Arquitetura REST */
@RequestMapping(value = "/contact-book")
public class ContactBookController {

    @Autowired 
	private ContactRepository contactRepository;

	@Autowired 
	private PhoneRepository phoneRepository;
	
	@Autowired 
	private ReportService reportService;

	//Pesquisa Todos os Contatos do Usuário
	@GetMapping(value = "/find/{user_id}", produces = "application/json")
	@CacheEvict(value = "varArmazenamentoCache", allEntries = true) //Salva e Limpa o cache
	@CachePut("varArmazenamentoCache")								//Atuliza o cache
	public ResponseEntity<List<ContactModel>> contactFindAll (@PathVariable (value = "user_id") Long user_id) throws InterruptedException{
		
		List<ContactModel> contacts = (List<ContactModel>) contactRepository.contactFindAll(user_id);

		return new ResponseEntity<List<ContactModel>>(contacts, HttpStatus.OK);
	}

    //Pesquisa Contato por Id
    @GetMapping(value = "/find-id/{user_id}/{contact_id}", produces = "application/json")
    @CacheEvict(value = "varArmazenamentoCache", allEntries = true) //Salva e Limpa o cache
    @CachePut("varArmazenamentoCache")								//Atuliza o cache
    public ResponseEntity<ContactModel> contactFindId 
        (@PathVariable (value = "user_id") Long user_id, 
        @PathVariable (value = "contact_id") Long contact_id) throws InterruptedException{
        
        ContactModel contact = contactRepository.contactFindId(user_id, contact_id);

        return new ResponseEntity<ContactModel>(contact, HttpStatus.OK);
    }

	//Pesquisa Contato por Nome 
	@GetMapping(value = "/find-name/{user_id}/{contact_nome}", produces = "application/json")
	@CacheEvict(value = "varArmazenamentoCache", allEntries = true) //Salva e Limpa o cache
	@CachePut("varArmazenamentoCache")								//Atuliza o cache
	public ResponseEntity<List<ContactModel>> contactFindName 
        (@PathVariable (value = "user_id") Long user_id, 
        @PathVariable (value = "contact_nome") String contact_nome) throws InterruptedException{
		
		List<ContactModel> contacts = (List<ContactModel>) contactRepository.contactFindName(user_id, contact_nome);

		return new ResponseEntity<List<ContactModel>>(contacts, HttpStatus.OK);
	}

	//Salvar Contato
	@PostMapping(value = "/save", produces = "application/json")
	public ResponseEntity<ContactModel> contactSave(@RequestBody ContactModel contact) throws IOException {

		//Associar Contato com Telefone
		for (int pos = 0; pos < contact.getPhones().size(); pos ++) {
			contact.getPhones().get(pos).setContact(contact);
		}

		ContactModel contactSave = contactRepository.save(contact);

		return new ResponseEntity<ContactModel>(contactSave, HttpStatus.OK);
	}

	//Atualizar Contato
	@PutMapping(value = "/updateContact", produces = "application/json")
	public ResponseEntity<ContactModel> contactUpdate(@RequestBody ContactModel contact) {

		Long 		id 			= contact.getId();
		String 		name		= contact.getName();
		String 		cpf			= contact.getCpf();
		Date 		date		= contact.getDate();
		String 		zip_code	= contact.getZip_code();
		String 		email		= contact.getEmail();

		contactRepository.contactUpdate(name, cpf, date, zip_code, email, id);
		return new ResponseEntity<ContactModel>(HttpStatus.OK);
	}

	//Atualizar Telefone do Contato
	@PutMapping(value = "/phoneUpdate", produces = "application/json")
	public ResponseEntity<PhoneModel> phoneUpdate(@RequestBody ArrayList<Any> array_data) {
		
		Long 	contact_id 		= Long.parseLong((array_data.get(0).toString()));
		Long 	phone_id 		= Long.parseLong((array_data.get(1).toString()));
		String 	phone_number 	= (array_data.get(2).toString());

		phoneRepository.phoneUpdate(phone_number, phone_id, contact_id);
		return new ResponseEntity<PhoneModel>(HttpStatus.OK);
	}


	//Deletar Contato por Id
	@DeleteMapping(value = "/delete/{contact_id}", produces = "application/text")
	public String contactDelete (@PathVariable (value = "contact_id") Long contact_id){
		
		contactRepository.deleteById(contact_id);
		
		return "ok";
	}

	//Deletar Telefone do Contato por Id
	@DeleteMapping(value = "/delete-phone/{phone_id}", produces = "application/text")
	public String phoneDelete (@PathVariable("phone_id") Long phone_id){
		
		phoneRepository.deleteById(phone_id);
		
		return "ok";
	}

	//Relatório de Todos os Contatos **ARRUMAR PARA PROCURAR POR USUÁRIO ESPECÍDFICO**
	@GetMapping(value = "/report", produces = "application/text")
	public ResponseEntity<String> contactReport(HttpServletRequest request) throws SQLException, JRException {
		
		//PDF em Base64
		byte[] pdf = reportService.reportGenerate("contact_book_report", new HashMap(), request.getServletContext());

		//String em Base64
		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
	}

}
