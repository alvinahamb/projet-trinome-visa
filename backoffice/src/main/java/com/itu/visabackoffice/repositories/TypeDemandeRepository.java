package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.TypeDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeDemandeRepository extends JpaRepository<TypeDemande, Integer> {
    Optional<TypeDemande> findByCode(String code);
}
