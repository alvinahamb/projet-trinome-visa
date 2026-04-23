package com.itu.visabackoffice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "visa_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisaType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_visa_type")
    private Integer id;

    @Column(name = "libelle", nullable = false, length = 100)
    private String libelle;
}
