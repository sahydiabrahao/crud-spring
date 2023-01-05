package br.sahydi.crudspring.repository;

import java.util.List;

import br.sahydi.crudspring.model.ContactModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactModel, Long>{

    //Pesquisa Todos por Usuário
    @Query(value = "SELECT * FROM contact WHERE username_id = ?1", nativeQuery = true)
    List<ContactModel> contactUserFindAll(Long username_id);

    //Pesquisa Nome por Usuário
    @Query(value = "SELECT * FROM contact WHERE username_id = ?1 AND upper(trim(nome)) like %?2%", nativeQuery = true)
    List<ContactModel> contactUserFindName(Long username_id, String username_name);

    //Pesquisa Id por Usuário
    @Query(value = "SELECT * FROM contact WHERE contact_username_id = ?1 AND contact_id = ?2", nativeQuery = true)
    ContactModel contactUserFindId(Long username_id, Long contact_id);

    //Pesquisa por Email
    @Query("SELECT u FROM contact u WHERE u.email = ?1")
    ContactModel contactFindByEmail(String username_email);

    //Pesquisa por Nome
    @Query("SELECT u FROM contact u WHERE u.nome like %?1%")
    List<ContactModel> contactFindByName(String username_name);

    //Consulta nome das constraints na tabela Usernames_Role 
    @Query(value = "SELECT constraint_name FROM information_schema.constraint_column_usage WHERE table_name = 'contacts_role' AND column_name = 'role_id' AND constraint_name <> 'unique_role_username'", nativeQuery = true)
    String searchConstraintRoleName();
        
}
