package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.Nationalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NationaliteRepository extends JpaRepository<Nationalite, Integer> {
}
