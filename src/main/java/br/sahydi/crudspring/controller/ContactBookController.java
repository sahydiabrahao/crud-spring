package br.sahydi.crudspring.controller;

import java.io.IOException;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
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
import br.sahydi.crudspring.repository.ContactRepository;
import br.sahydi.crudspring.repository.PhoneRepository;
import br.sahydi.crudspring.service.ReportService;

@RestController /* Arquitetura REST */
@RequestMapping(value = "/contactBook")
public class ContactBookController {

    @Autowired 
	private ContactRepository contatoRepository;

	@Autowired 
	private PhoneRepository telefoneRepository;
	
	@Autowired 
	private ReportService reportService;

	//Pesquisa Todos os Contatos do Usuário
	@GetMapping(value = "/{contact_user_id}", produces = "application/json")
	@CacheEvict(value = "varArmazenamentoCache", allEntries = true) //Salva e Limpa o cache
	@CachePut("varArmazenamentoCache")								//Atuliza o cache
	public ResponseEntity<List<ContactModel>> contatoUserFindAll (@PathVariable (value = "contact_user_id") Long contact_user_id) throws InterruptedException{
		
		List<ContactModel> contacts = (List<ContactModel>) contatoRepository.contactFindAll(contact_user_id);

		return new ResponseEntity<List<ContactModel>>(contacts, HttpStatus.OK);
	}

    //Pesquisa Contato por Id
    @GetMapping(value = "/contatoEdit/{contact_user_id}/{contact_id}", produces = "application/json")
    @CacheEvict(value = "varArmazenamentoCache", allEntries = true) //Salva e Limpa o cache
    @CachePut("varArmazenamentoCache")								//Atuliza o cache
    public ResponseEntity<ContactModel> contatoUserFindId 
        (@PathVariable (value = "contact_user_id") Long contact_user_id, 
        @PathVariable (value = "contact_id") Long contact_id) throws InterruptedException{
        
        ContactModel contact = contatoRepository.contactFindId(contact_user_id, contact_id);

        return new ResponseEntity<ContactModel>(contact, HttpStatus.OK);
    }

	//Pesquisa Contato por Nome 
	@GetMapping(value = "/{contact_user_id}/{contact_nome}", produces = "application/json")
	@CacheEvict(value = "varArmazenamentoCache", allEntries = true) //Salva e Limpa o cache
	@CachePut("varArmazenamentoCache")								//Atuliza o cache
	public ResponseEntity<List<ContactModel>> contatoUserFindName 
        (@PathVariable (value = "contact_user_id") Long contact_user_id, 
        @PathVariable (value = "contact_nome") String contact_nome) throws InterruptedException{
		
		List<ContactModel> contacts = (List<ContactModel>) contatoRepository.contactFindName(contact_user_id, contact_nome);

		return new ResponseEntity<List<ContactModel>>(contacts, HttpStatus.OK);
	}

	//Salvar Contato
	@PostMapping(value = "/save", produces = "application/json")
	public ResponseEntity<ContactModel> cadastrar(@RequestBody ContactModel contact) throws IOException {

		//Associar Contato com Telefone
		for (int pos = 0; pos < contact.getPhones().size(); pos ++) {
			contact.getPhones().get(pos).setContact(contact);
		}

		ContactModel contactSave = contatoRepository.save(contact);

		return new ResponseEntity<ContactModel>(contactSave, HttpStatus.OK);
	}

	//Atualizar Contato
	@PutMapping(value = "/update", produces = "application/json")
	public ResponseEntity<ContactModel> atualizar(@RequestBody ContactModel contact) {

		ContactModel contactSave = contatoRepository.save(contact);
		return new ResponseEntity<ContactModel>(contactSave, HttpStatus.OK);
	}


	//Deletar Contato por Id
	@DeleteMapping(value = "/contactDelete/{contact_id}", produces = "application/text")
	public String delete (@PathVariable (value = "contact_id") Long contact_id){
		
		contatoRepository.deleteById(contact_id);
		
		return "ok";
	}

	//Deletar Telefone do Contato por Id
	@DeleteMapping(value = "/phoneDelete/{phone_id}", produces = "application/text")
	public String deletePhone (@PathVariable("phone_id") Long phone_id){
		
		telefoneRepository.deleteById(phone_id);
		
		return "ok";
	}

	//Relatório de Todos os Contatos **ARRUMAR PARA PROCURAR POR USUÁRIO ESPECÍDFICO**
	@GetMapping(value = "/contactReport", produces = "application/text")
	public ResponseEntity<String> reportDownload(HttpServletRequest request) throws SQLException, JRException {
		
		//PDF em Base64
		byte[] pdf = reportService.reportGenerate("report_usuario", new HashMap(), request.getServletContext());

		//String em Base64
		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
	}

}
