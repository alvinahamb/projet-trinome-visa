package com.itu.visabackoffice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "demandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_demande")
    private Integer id;

    @Column(name = "date_demande", nullable = false)
    private LocalDateTime dateDemande;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demandeur_id", nullable = false)
    private Demandeur demandeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_visa_id", nullable = false)
    private TypeVisa typeVisa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_demande_id", nullable = false)
    private TypeDemande typeDemande;

    @OneToMany(mappedBy = "demande", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DemandePieceJustificative> pieceJustificatives;

    @OneToMany(mappedBy = "demande", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HistoriqueStatutDemande> historiques;

    @PrePersist
    protected void onCreate() {
        dateDemande = LocalDateTime.now();
    }
}
