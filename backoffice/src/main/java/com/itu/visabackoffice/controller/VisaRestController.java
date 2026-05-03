package com.itu.visabackoffice.controller;

import com.itu.visabackoffice.dto.ApiResponse;
import com.itu.visabackoffice.dto.TransfertVisaSaisieDTO;
import com.itu.visabackoffice.exceptions.ExpectedException;
import com.itu.visabackoffice.services.DemandeVisaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/visa")
@Slf4j
public class VisaRestController {

  @Autowired
  private DemandeVisaService demandeVisaService;

  /**
   * Endpoint pour rechercher un visa par sa référence - utilisé pour le transfert
   * visa
   * 
   * @param reference la référence du visa
   * @return ApiResponse avec les informations du visa et du demandeur si trouvé
   */
  @GetMapping("/search")
  public ResponseEntity<ApiResponse<Object>> searchVisaByReference(@RequestParam String reference) {
    try {
      log.info("Recherche visa par référence: {}", reference);

      Object resultat = demandeVisaService.searchVisaByReference(reference);

      if (resultat != null) {
        ApiResponse<Object> response = ApiResponse.success(
            resultat,
            "Visa trouvé avec succès");
        return ResponseEntity.ok(response);
      } else {
        ApiResponse<Object> response = ApiResponse.success(
            null,
            "Aucun visa trouvé avec cette référence");
        return ResponseEntity.ok(response);
      }

    } catch (RuntimeException e) {
      log.error("Erreur métier recherche visa: {}", e.getMessage());

      ApiResponse<Object> response = ApiResponse.error(
          400,
          e.getMessage(),
          "Erreur lors de la recherche du visa");

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    } catch (Exception e) {
      log.error("Erreur inattendue recherche visa: {}", e.getMessage(), e);

      ApiResponse<Object> response = ApiResponse.error(
          500,
          "Erreur serveur interne",
          "Une erreur inattendue s'est produite");

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * Endpoint pour créer une demande de transfert de visa
   * 
   * @param transfertSaisie les données du transfert (visa reference + new
   *                        passport data)
   * @return ApiResponse avec le résultat de la création
   */
  @PostMapping("/transfer-request")
  public ResponseEntity<ApiResponse<Object>> createTransfertVisa(@RequestBody TransfertVisaSaisieDTO transfertSaisie) {
    try {
      log.info("Création demande transfert visa - Référence: {}", transfertSaisie.getVisaReference());

      Object resultat = demandeVisaService.enregistrerTransfertVisa(transfertSaisie);

      ApiResponse<Object> response = ApiResponse.success(
          resultat,
          "Demande de transfert de visa enregistrée avec succès");

      return ResponseEntity.status(HttpStatus.CREATED).body(response);

    } catch (ExpectedException e) {
      log.warn("Erreur métier attendue transfert visa: {}", e.getMessage());

      if (e.getMessage().equals(DemandeVisaService.SANS_DONNEES_ANTERIEURS)) {
        // We send a specific response for the case where no visa is found with the
        // given reference
        ApiResponse<Object> response = ApiResponse.error(
            400,
            null,
            "Aucun visa trouvé avec cette référence");
        return ResponseEntity.ok(response);
      } else {
        ApiResponse<Object> response = ApiResponse.error(
            400,
            e.getMessage(),
            "Erreur lors de la création de la demande de transfert");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }
    } catch (RuntimeException e) {
      log.error("Erreur métier transfert visa: {}", e.getMessage());

      ApiResponse<Object> response = ApiResponse.error(
          400,
          e.getMessage(),
          "Erreur lors de la création de la demande de transfert");

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    } catch (Exception e) {
      log.error("Erreur inattendue transfert visa: {}", e.getMessage(), e);

      ApiResponse<Object> response = ApiResponse.error(
          500,
          "Erreur serveur interne",
          "Une erreur inattendue s'est produite");

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }
}
