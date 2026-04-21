package com.itu.visabackoffice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "historiques_statuts_demandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueStatutDemande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique_statut")
    private Integer id;

    @Column(name = "date_changement", nullable = false)
    private LocalDateTime dateChangement;

    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statut_demande_id", nullable = false)
    private StatutDemande statutDemande;

    @PrePersist
    protected void onCreate() {
        dateChangement = LocalDateTime.now();
    }
}
