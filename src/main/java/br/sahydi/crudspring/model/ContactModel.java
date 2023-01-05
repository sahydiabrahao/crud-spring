package br.sahydi.crudspring.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "contact")
public class ContactModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long contact_id;

    private String contact_name;

    @CPF(message= "CPF Inválido")
    private String contact_cpf;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso=ISO.DATE, pattern="yyyy-MM-dd")
    private Date contact_date;

    private String contact_zip_code;

    @Column(unique = true)
	private String email;

    //Referencia UsernameModel
	private Long contact_username_id;

    //Relacionamento PhoneModel  > phone_contact_id = FK 
    @OneToMany(mappedBy="phone_contact_id", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<PhoneModel> contact_phones = new ArrayList<PhoneModel>();

    public Long getContact_id() {
        return contact_id;
    }

    public void setContact_id(Long contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_cpf() {
        return contact_cpf;
    }

    public void setContact_cpf(String contact_cpf) {
        this.contact_cpf = contact_cpf;
    }

    public Date getContact_date() {
        return contact_date;
    }

    public void setContact_date(Date contact_date) {
        this.contact_date = contact_date;
    }

    public String getContact_zip_code() {
        return contact_zip_code;
    }

    public void setContact_zip_code(String contact_zip_code) {
        this.contact_zip_code = contact_zip_code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getContact_username_id() {
        return contact_username_id;
    }

    public void setContact_username_id(Long contact_username_id) {
        this.contact_username_id = contact_username_id;
    }

    public List<PhoneModel> getContact_phones() {
        return contact_phones;
    }

    public void setContact_phones(List<PhoneModel> contact_phones) {
        this.contact_phones = contact_phones;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contact_id == null) ? 0 : contact_id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ContactModel other = (ContactModel) obj;
        if (contact_id == null) {
            if (other.contact_id != null)
                return false;
        } else if (!contact_id.equals(other.contact_id))
            return false;
        return true;
    }



    
}
