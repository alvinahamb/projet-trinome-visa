package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.VisaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisaTypeRepository extends JpaRepository<VisaType, Integer> {
}
