package com.itu.visabackoffice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pieces_justificatives")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PieceJustificative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_piece_justificative")
    private Integer id;

    @Column(name = "libelle", nullable = false, length = 150)
    private String libelle;
}
