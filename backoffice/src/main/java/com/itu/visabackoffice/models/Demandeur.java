package com.itu.visabackoffice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "demandeurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demandeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_demandeur")
    private Integer id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance", length = 150)
    private String lieuNaissance;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "adresse", columnDefinition = "TEXT")
    private String adresse;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nationalite_id", nullable = false)
    private Nationalite nationalite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @OneToMany(mappedBy = "demandeur", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Passeport> passeports;

    @OneToMany(mappedBy = "demandeur", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Demande> demandes;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
}
