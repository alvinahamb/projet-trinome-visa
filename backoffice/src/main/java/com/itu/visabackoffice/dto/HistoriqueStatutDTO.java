package com.itu.visabackoffice.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriqueStatutDTO {
  private Integer id;
  private String statut;
  private Integer statutId;
  private String commentaire;
  private LocalDateTime dateChangement;
}
