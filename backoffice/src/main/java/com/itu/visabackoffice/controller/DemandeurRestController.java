package com.itu.visabackoffice.controller;

import com.itu.visabackoffice.dto.ApiResponse;
import com.itu.visabackoffice.dto.DemandeFicheDTO;
import com.itu.visabackoffice.dto.DemandeurDemandesDTO;
import com.itu.visabackoffice.services.DemandeVisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
// import org.springframework.stereotype.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/demandeur-visa")
@Slf4j
public class DemandeurRestController {

  @Autowired
  private DemandeVisaService demandeVisaService;

  // ================== GET: LECTURES ==================

  /**
   * Endpoint pour rechercher un demandeur et ses demandes par numero de passeport (PASSXXXX) ou ID de demande (XXXX)
   * @param numero format: PASSXXXX (passeport) ou XXXX (ID demande)
   * @return ApiResponse avec demandeur et liste de ses demandes
   */
  @GetMapping("/search")
  public ResponseEntity<ApiResponse<DemandeurDemandesDTO>> searchDemandeurByNumero(
      @RequestParam String numero) {
    try {
      log.info("Recherche demandeur par numero: {}", numero);

      if (numero == null || numero.trim().isEmpty()) {
        ApiResponse<DemandeurDemandesDTO> response = ApiResponse.error(
            400,
            "Le numero de passeport ou demande est obligatoire",
            "Format attendu: PASSXXXX ou XXXX"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }

      DemandeurDemandesDTO resultat = demandeVisaService.getDemandeurAvecDemandesParNumero(numero);

      if (resultat == null || resultat.getDemandes() == null || resultat.getDemandes().isEmpty()) {
        ApiResponse<DemandeurDemandesDTO> response = ApiResponse.error(
            404,
            "Aucune demande trouvée pour ce numero",
            "Veuillez verifier le numero saisi"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }

      ApiResponse<DemandeurDemandesDTO> response = ApiResponse.success(
          resultat,
          "Demandeur et ses demandes récupérées avec succès"
      );

      return ResponseEntity.ok(response);

    } catch (IllegalArgumentException e) {
      log.warn("Argument invalide pour recherche demandeur: {}", e.getMessage());

      ApiResponse<DemandeurDemandesDTO> response = ApiResponse.error(
          400,
          e.getMessage(),
          "Format attendu: PASSXXXX ou XXXX"
      );

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    } catch (Exception e) {
      log.error("Erreur inattendue recherche demandeur: {}", e.getMessage(), e);

      ApiResponse<DemandeurDemandesDTO> response = ApiResponse.error(
          500,
          "Erreur serveur interne",
          "Une erreur inattendue s'est produite"
      );

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * Endpoint pour récupérer les détails d'une demande (fiche) avec historique des statuts
   * @param demandeId l'ID de la demande
   * @return ApiResponse avec les détails de la demande et l'historique
   */
  @GetMapping("/demande-fiche/{id}")
  public ResponseEntity<ApiResponse<DemandeFicheDTO>> getDemandeFiche(
      @PathVariable("id") Integer demandeId) {
    try {
      log.info("Récupération fiche demande ID: {}", demandeId);

      DemandeFicheDTO resultat = demandeVisaService.getDemandeFiche(demandeId);

      ApiResponse<DemandeFicheDTO> response = ApiResponse.success(
          resultat,
          "Fiche demande récupérée avec succès"
      );

      return ResponseEntity.ok(response);

    } catch (RuntimeException e) {
      log.error("Erreur métier récupération fiche demande: {}", e.getMessage());

      ApiResponse<DemandeFicheDTO> response = ApiResponse.error(
          400,
          e.getMessage(),
          "Erreur lors de la récupération de la fiche demande"
      );

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    } catch (Exception e) {
      log.error("Erreur inattendue récupération fiche demande: {}", e.getMessage(), e);

      ApiResponse<DemandeFicheDTO> response = ApiResponse.error(
          500,
          "Erreur serveur interne",
          "Une erreur inattendue s'est produite"
      );

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

}