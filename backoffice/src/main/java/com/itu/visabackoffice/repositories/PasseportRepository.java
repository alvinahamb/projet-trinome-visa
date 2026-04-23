package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.Passeport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasseportRepository extends JpaRepository<Passeport, Integer> {
    Optional<Passeport> findByNumero(String numero);
    List<Passeport> findByDemandeurId(Integer demandeurId);
}
