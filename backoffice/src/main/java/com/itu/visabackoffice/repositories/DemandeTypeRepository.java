package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.DemandeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DemandeTypeRepository extends JpaRepository<DemandeType, Integer> {
    Optional<DemandeType> findByCode(String code);
}
