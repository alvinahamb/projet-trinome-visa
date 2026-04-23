package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.Demandeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeurRepository extends JpaRepository<Demandeur, Integer> {
    List<Demandeur> findByNom(String nom);
    List<Demandeur> findByPrenom(String prenom);
    List<Demandeur> findByNomAndPrenom(String nom, String prenom);
    Optional<Demandeur> findByEmail(String email);
    List<Demandeur> findByNationaliteId(Integer nationaliteId);
}
