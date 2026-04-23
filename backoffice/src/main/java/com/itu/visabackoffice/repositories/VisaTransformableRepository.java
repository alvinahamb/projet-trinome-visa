package com.itu.visabackoffice.repositories;

import com.itu.visabackoffice.models.VisaTransformable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisaTransformableRepository extends JpaRepository<VisaTransformable, Integer> {
}
