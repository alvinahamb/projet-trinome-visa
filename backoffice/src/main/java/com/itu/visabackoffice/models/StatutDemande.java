package com.itu.visabackoffice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "statuts_demandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatutDemande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut_demande")
    private Integer id;

    @Column(name = "libelle", nullable = false, length = 100)
    private String libelle;
}
