package br.sahydi.crudspring.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.sahydi.crudspring.model.PhoneModel;

@Repository
public interface PhoneRepository extends JpaRepository<PhoneModel, Long> {


    //Salvar Telefone do Contato do Usuário
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO phone('number', contact_id) VALUES (?1, ?2)", nativeQuery = true)
    void phoneUpdateAdd(String number, Long contact_id);
}
