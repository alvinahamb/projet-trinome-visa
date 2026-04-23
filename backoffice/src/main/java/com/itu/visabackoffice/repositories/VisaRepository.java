package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisaRepository extends JpaRepository<Visa, Integer> {
    Optional<Visa> findByReference(String reference);
}
