package com.itu.visabackoffice.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeurInfoDTO {

  private Integer id;
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
}
