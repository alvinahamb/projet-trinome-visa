package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutDemandeRepository extends JpaRepository<StatutDemande, Integer> {
}
