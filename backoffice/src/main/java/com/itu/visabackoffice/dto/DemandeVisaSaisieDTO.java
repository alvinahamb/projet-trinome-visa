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
    
    // Pièces justificatives
    private List<Integer> pieces;
}
