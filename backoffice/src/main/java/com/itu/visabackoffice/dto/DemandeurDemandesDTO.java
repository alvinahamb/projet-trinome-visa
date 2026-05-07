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
public class DemandeurDemandesDTO {

  private DemandeurInfoDTO demandeur;
  private List<DemandeVisaCplDTO> demandes;
}
