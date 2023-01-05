package br.sahydi.crudspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.sahydi.crudspring.model.PhoneModel;

@Repository
public interface PhoneRepository extends JpaRepository<PhoneModel, Long> {
    
}
