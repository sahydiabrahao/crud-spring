package br.sahydi.crudspring.repository;

import br.sahydi.crudspring.model.UserModel;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>{
    
    //Pesquisa Usuario por Email
    @Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
    UserModel userFindByEmail(String user_email);

    //Cadastra novo token no BD para o email
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE UserModel SET user_token = ?1 WHERE user_email = ?2")
	void updateTokenUser(String user_token, String user_email);

    //Define Role Ex: ROLE_ADMIN ROLE_USER 
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_role (user_id, role_id) values (?1, (SELECT id FROM role WHERE name = 'ROLE_USER')); ", nativeQuery = true)
    void insertRoleUser(Long user_id);
    
    //Recuperação de senha 
    @Transactional
    @Modifying
    @Query(value = " UPDATE users SET password = ?1 WHERE id = ?2", nativeQuery = true)
    void updatePassword(String user_password, Long user_id);

    //Consulta nome das constraints na tabela Usuarios_Role 
    @Query(value = "SELECT constraint_name FROM information_schema.constraint_column_usage WHERE table_name = 'user_role' AND column_name = 'role_id' AND constraint_name <> 'unique_role_user'", nativeQuery = true)
    String searchConstraintRoleName();
}
