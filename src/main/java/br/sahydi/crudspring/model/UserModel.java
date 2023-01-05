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
@Table(name = "user")
public class UserModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long user_id;
    
    @Column(unique = true)
	private String user_email;

	private String user_password;

    private String user_token;

    //:::::SPRING BOOT SECURITY::::: 
    //Relacionamento RoleModel > user_id = FK (UM Usuario pode ter MUITAS Roles(acessos))
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", uniqueConstraints = @UniqueConstraint (
			         columnNames = {"user_id","role_id"}, name = "unique_role_user"), 
	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id", table = "user", unique = false,
	foreignKey = @ForeignKey(name = "user_fk", value = ConstraintMode.CONSTRAINT)), 
    
	inverseJoinColumns = @JoinColumn (name = "role_id", referencedColumnName = "role_id", table = "role", unique = false, updatable = false,
	   foreignKey = @ForeignKey (name="role_fk", value = ConstraintMode.CONSTRAINT)))
	private List<RoleModel> user_roles = new ArrayList<RoleModel>(); 

    //UserDetails
	public Collection<RoleModel> getAuthorities() {
		return user_roles;
	}

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
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
        if (user_id == null) {
            if (other.user_id != null)
                return false;
        } else if (!user_id.equals(other.user_id))
            return false;
        return true;
    }




}
