package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.DemandePieceJustificative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandePieceJustificativeRepository extends JpaRepository<DemandePieceJustificative, Integer> {
    List<DemandePieceJustificative> findByDemandeId(Integer demandeId);
    List<DemandePieceJustificative> findByPieceJustificativeId(Integer pieceJustificativeId);
    Optional<DemandePieceJustificative> findByDemandeIdAndPieceJustificativeId(Integer demandeId, Integer pieceJustificativeId);
}
