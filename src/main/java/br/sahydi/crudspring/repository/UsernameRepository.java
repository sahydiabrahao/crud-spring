package br.sahydi.crudspring.repository;

import br.sahydi.crudspring.model.UsernameModel;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsernameRepository extends JpaRepository<UsernameModel, Long>{
    
    //Pesquisa por Email
    @Query(value = "SELECT * FROM username WHERE username_email = ?1", nativeQuery = true)
    UsernameModel usernameFindByEmail(String username_email);

    //Cadastra novo token no BD para o email
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE UsernameModel SET username_token = ?1 WHERE username_email = ?2")
	void updateTokenUser(String username_token, String username_email);

    //Consulta nome das constraints na tabela Usuarios_Role 
    @Query(value = "SELECT constraint_name FROM information_schema.constraint_column_usage WHERE table_name = 'usuarios_role' AND column_name = 'role_id' AND constraint_name <> 'unique_role_user'", nativeQuery = true)
    String searchConstraintRoleName();

    //Define Role Ex: ROLE_ADMIN ROLE_USER 
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO username_role (username_id, role_id) values (?1, (SELECT role_id FROM role WHERE role_nome = 'ROLE_USER')); ", nativeQuery = true)
    void insertRoleUsername(Long username_id);
    
    //Recuperação de senha 
    @Transactional
    @Modifying
    @Query(value = " UPDATE username SET username_password = ?1 WHERE username_id = ?2", nativeQuery = true)
    void updatePassword(String username_password, Long username_id);

}
