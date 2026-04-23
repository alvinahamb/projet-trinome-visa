package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.PieceJustificative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PieceJustificativeRepository extends JpaRepository<PieceJustificative, Integer> {
}
