package com.itu.visabackoffice.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeFicheDTO {
  private DemandeVisaCplDTO demande;
  private List<HistoriqueStatutDTO> historiques;
}
