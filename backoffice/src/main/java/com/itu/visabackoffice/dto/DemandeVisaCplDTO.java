package com.itu.visabackoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeVisaCplDTO {

    // ID de la demande
    private Integer id;

    // État civil
    private String nom;
    private String prenom;
    private String genre;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String telephone;
    private String email;
    private String adresse;
    private String nationalite;
    private String situationFamiliale;

    // Passeport
    private String numeroPasseport;
    private LocalDate dateDelivrance;
    private LocalDate dateExpiration;
    private String paysDelivrance;

    // Visa transformable actuel
    private String referenceVisa;
    private LocalDate dateEntree;
    private LocalDate dateFin;
    private String lieuEntree;

    // Type de visa
    private String visaType;

    // ID du type de visa
    private Integer visaTypeId;

    // Statut de la demande
    private String statut;

    // ID du statut de la demande
    private Integer statutId;

    // Pièces justificatives (libellés)
    private List<String> pieces;
    
    // IDs des pièces justificatives (pour téléchargement)
    private List<Integer> piecesIds;

    // IDs des pièces justificatives liées au type de visa
    private List<Integer> piecesJustificativeIds;
}
