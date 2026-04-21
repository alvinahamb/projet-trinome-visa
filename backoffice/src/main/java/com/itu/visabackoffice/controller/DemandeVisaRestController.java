package com.itu.visabackoffice.controller;

import com.itu.visabackoffice.dto.ApiResponse;
import com.itu.visabackoffice.dto.DonneesDemandeVisaDTO;
import com.itu.visabackoffice.services.DemandeVisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/demande-visa")
@Slf4j
public class DemandeVisaRestController {

  @Autowired
  private DemandeVisaService demandeVisaService;

  @GetMapping("/demande-saisie-form")
  public ResponseEntity<ApiResponse<DonneesDemandeVisaDTO>> getDonneesDemandeVisa() {
    try {
      log.info("Récupération données demande visa");
      DonneesDemandeVisaDTO donnees = demandeVisaService.getDonneesDemandeVisa();
      
      ApiResponse<DonneesDemandeVisaDTO> response = ApiResponse.success(
          donnees, 
          "Toutes les données de demande de visa ont été récupérées avec succès"
      );
      
      return ResponseEntity.ok(response);
      
    } catch (RuntimeException e) {
      log.error("Erreur métier: {}", e.getMessage());
      
      ApiResponse<DonneesDemandeVisaDTO> response = ApiResponse.error(
          500,
          e.getMessage(),
          "Une erreur est survenue lors de la récupération des données"
      );
      
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
      
    } catch (Exception e) {
      log.error("Erreur inattendue: {}", e.getMessage(), e);
      
      ApiResponse<DonneesDemandeVisaDTO> response = ApiResponse.error(
          500,
          "Erreur serveur interne",
          "Une erreur inattendue s'est produite"
      );
      
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }
}
