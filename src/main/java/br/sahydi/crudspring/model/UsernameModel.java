package br.sahydi.crudspring.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "username")
public class UsernameModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long username_id;
    
    @Column(unique = true)
	private String username_email;

	private String username_password;

    private String username_token;

    //:::::SPRING BOOT SECURITY::::: 
    //Relacionamento RoleModel > username_id = FK (UM Usuario pode ter MUITAS Roles(acessos))
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "username_role", uniqueConstraints = @UniqueConstraint (
			         columnNames = {"username_id","role_id"}, name = "unique_role_username"), 
	joinColumns = @JoinColumn(name = "username_id", referencedColumnName = "username_id", table = "username", unique = false,
	foreignKey = @ForeignKey(name = "username_fk", value = ConstraintMode.CONSTRAINT)), 
    
	inverseJoinColumns = @JoinColumn (name = "role_id", referencedColumnName = "role_id", table = "role", unique = false, updatable = false,
	   foreignKey = @ForeignKey (name="role_fk", value = ConstraintMode.CONSTRAINT)))
	private List<RoleModel> username_roles = new ArrayList<RoleModel>(); 

    //UserDetails
	public Collection<RoleModel> getAuthorities() {
		return username_roles;
	}

    public Long getUsername_id() {
        return username_id;
    }

    public void setUsername_id(Long username_id) {
        this.username_id = username_id;
    }

    public String getUsername_email() {
        return username_email;
    }

    public void setUsername_email(String username_email) {
        this.username_email = username_email;
    }

    public String getUsername_password() {
        return username_password;
    }

    public void setUsername_password(String username_password) {
        this.username_password = username_password;
    }

    public String getUsername_token() {
        return username_token;
    }

    public void setUsername_token(String username_token) {
        this.username_token = username_token;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username_id == null) ? 0 : username_id.hashCode());
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
        UsernameModel other = (UsernameModel) obj;
        if (username_id == null) {
            if (other.username_id != null)
                return false;
        } else if (!username_id.equals(other.username_id))
            return false;
        return true;
    }

}
