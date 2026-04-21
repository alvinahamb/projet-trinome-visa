package com.itu.visabackoffice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "visas_transformables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisaTransformable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_visa_transformable")
    private Integer id;

    @Column(name = "date_entree")
    private LocalDate dateEntree;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "lieu_entree", length = 150)
    private String lieuEntree;
}
