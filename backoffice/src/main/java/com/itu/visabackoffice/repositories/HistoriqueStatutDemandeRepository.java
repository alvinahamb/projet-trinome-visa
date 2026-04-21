package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.HistoriqueStatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueStatutDemandeRepository extends JpaRepository<HistoriqueStatutDemande, Integer> {
    List<HistoriqueStatutDemande> findByDemandeId(Integer demandeId);
    List<HistoriqueStatutDemande> findByStatutDemandeId(Integer statutDemandeId);
}
