package com.itu.visabackoffice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "demandes_pieces_justificatives")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandePieceJustificative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_demande_piece_justificative")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piece_justificative_id", nullable = false)
    private PieceJustificative pieceJustificative;

    @Column(name = "path", length = 255)
    private String path;

    @Column(name = "nom_fichier", length = 255)
    private String nomFichier;

    @Column(name = "date_ajout", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateAjout;
}
