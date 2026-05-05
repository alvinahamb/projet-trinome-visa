package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatutDemandeRepository extends JpaRepository<StatutDemande, Integer> {
	Optional<StatutDemande> findByLibelleIgnoreCase(String libelle);
}
