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
@Table(name = "users")
public class UserModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
    
    @Column(unique = true)
	private String email;

	private String password;

    private String token;

    //:::::SPRING BOOT SECURITY::::: 
    //Relacionamento RoleModel > user_id = FK (UM Usuario pode ter MUITAS Roles(acessos))
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", uniqueConstraints = @UniqueConstraint (
			         columnNames = {"user_id","role_id"}, name = "unique_role_user"), 
	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", table = "users", unique = false,
	foreignKey = @ForeignKey(name = "user_fk", value = ConstraintMode.CONSTRAINT)), 
    
	inverseJoinColumns = @JoinColumn (name = "role_id", referencedColumnName = "id", table = "role", unique = false, updatable = false,
	   foreignKey = @ForeignKey (name="role_fk", value = ConstraintMode.CONSTRAINT)))
	private List<RoleModel> user_roles = new ArrayList<RoleModel>(); 

    //UserDetails
	public Collection<RoleModel> getAuthorities() {
		return user_roles;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        UserModel other = (UserModel) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

   




}
