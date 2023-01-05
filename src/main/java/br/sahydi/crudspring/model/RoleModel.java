package br.sahydi.crudspring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
@SequenceGenerator(name = "seq_role", sequenceName = "seq_role", allocationSize = 1, initialValue = 1)
public class RoleModel implements GrantedAuthority {
    
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role")
	private Long role_id;

    //Papel Ex: ROLE_GERENTE  
    private String role_name;            
    
    //Método nome do papel Ex:ROLE_GERENTE 
    @Override
    public String getAuthority() {      
        return this.role_name;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

}
