package br.sahydi.crudspring.repository;

import java.util.Date;
import java.util.List;

import br.sahydi.crudspring.model.ContactModel;
import br.sahydi.crudspring.model.PhoneModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ContactRepository extends JpaRepository<ContactModel, Long>{

    //Pesquisa Todos os Contatos do Usuário
    @Query(value = "SELECT * FROM contact WHERE user_id = ?1", nativeQuery = true)
    List<ContactModel> contactFindAll(Long user_id);
    
    //Pesquisa Contato do Usuário por Id
    @Query(value = "SELECT * FROM contact WHERE user_id = ?1 AND id = ?2", nativeQuery = true)
    ContactModel contactFindId(Long user_id, Long contact_id);
    
    //Pesquisa Contato do Usuário por Nome 
    @Query(value = "SELECT * FROM contact WHERE user_id = ?1 AND upper(trim(nome)) like %?2%", nativeQuery = true)
    List<ContactModel> contactFindName(Long user_id, String contact_name);
    
    //Atualizar Contato do Usuário
    @Transactional
    @Modifying
    @Query(value = "UPDATE contact SET name=?1, cpf=?2, date=?3, zip_code=?4, email=?5 WHERE id=?6", nativeQuery = true)
    void contactUpdate(String name, String cpf, Date date, String zip_code, String email, Long id);

    //Consulta nome das constraints na tabela Users_Role 
    @Query(value = "SELECT constraint_name FROM information_schema.constraint_column_usage WHERE table_name = 'contacts_role' AND column_name = 'role_id' AND constraint_name <> 'unique_role_user'", nativeQuery = true)
    String searchConstraintRoleName();
        
}
