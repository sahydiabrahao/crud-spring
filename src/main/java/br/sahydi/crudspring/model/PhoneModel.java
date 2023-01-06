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
	private Long phone_id;

	private String phone_number;

    //Relacionamento UserModel > FK = contact_id
    @JsonIgnore //Evita repetição na cunsulta da lista (33.24)
    @org.hibernate.annotations.ForeignKey(name = "contact_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER) //Não cadastra telefone sem pai
    private ContactModel phone;

    public Long getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(Long phone_id) {
        this.phone_id = phone_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public ContactModel getPhone() {
        return phone;
    }
    public void setPhone(ContactModel phone) {
        this.phone = phone;
    }
}
