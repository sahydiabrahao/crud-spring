package br.sahydi.crudspring.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "phone")
public class PhoneModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String number;

    //Relacionamento UserModel > FK = contact_id
    @JsonIgnore //Evita repetição na cunsulta da lista (33.24)
    @org.hibernate.annotations.ForeignKey(name = "contact_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER) //Não cadastra telefone sem pai
    private ContactModel contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ContactModel getContact() {
        return contact;
    }

    public void setContact(ContactModel contact) {
        this.contact = contact;
    }


}
