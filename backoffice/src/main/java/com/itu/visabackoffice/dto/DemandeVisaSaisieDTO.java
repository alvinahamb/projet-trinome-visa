package com.itu.visabackoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeVisaSaisieDTO {
    
    // État civil
    private String nom;
    private String prenom;
    private Integer genre;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String telephone;
    private String email;
    private String adresse;
    private Integer nationalite;
    private Integer situationFamiliale;
    
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
    private Integer visaType;
    
    // Duplicata flag - if true, will create 2 demandes (1 completed + 1 pending for duplicata)
    @Default
    private Boolean isDuplicata = false;
    
    // Pièces justificatives
    private List<Integer> pieces;
}
