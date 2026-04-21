package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Integer> {
    List<Demande> findByDemandeurId(Integer demandeurId);
    List<Demande> findByTypeVisaId(Integer typeVisaId);
    List<Demande> findByTypeDemandeId(Integer typeDemandeId);
}
