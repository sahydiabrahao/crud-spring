package br.sahydi.crudspring.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.sahydi.crudspring.model.PhoneModel;

@Repository
public interface PhoneRepository extends JpaRepository<PhoneModel, Long> {

    //Atualizar Telefone do Contato do Usuário
    @Transactional
    @Modifying
    @Query(value = "UPDATE phone SET number=?1 WHERE id=?2", nativeQuery = true)
    void phoneUpdate(Long phone_id, Long contact_id);

}
