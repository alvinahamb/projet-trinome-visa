package com.itu.visabackoffice.controller;

import com.itu.visabackoffice.dto.ApiResponse;
import com.itu.visabackoffice.dto.DonneesDemandeVisaDTO;
import com.itu.visabackoffice.dto.DemandeVisaSaisieDTO;
import com.itu.visabackoffice.dto.DemandeVisaCplDTO;
import com.itu.visabackoffice.dto.DemandeurDemandesDTO;
import java.util.List;
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

    @GetMapping("/ok")
    public String getDemandeurAvecDemandes(){
        System.out.println("ok");
        return "ok";
    }

  /**
   * Endpoint pour récupérer un demandeur et la liste de ses demandes.
   * Si le numero commence par "PASS", il est traité comme numero de passeport,
   * sinon comme numero de demande.
   * @param numero le numero de passeport (PASS...) ou le numero de demande
   * @return ApiResponse avec le demandeur et ses demandes
   */
  @GetMapping("/demandes")
  public ResponseEntity<ApiResponse<DemandeurDemandesDTO>> getDemandeurAvecDemandes(
      @RequestParam(name = "numero", required = false) String numero,
      HttpServletRequest request) {
    try {
      String inputNumero = numero;
      if (inputNumero == null || inputNumero.isBlank()) {
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isBlank() && !queryString.contains("=")) {
          inputNumero = queryString.trim();
        }
      }

      if (inputNumero == null || inputNumero.isBlank()) {
        throw new IllegalArgumentException("Le numero est obligatoire");
      }

      log.info("Recherche demandeur et demandes par numero: {}", inputNumero);

      DemandeurDemandesDTO resultat = demandeVisaService.getDemandeurAvecDemandesParNumero(inputNumero);

      if (resultat != null) {
        ApiResponse<DemandeurDemandesDTO> response = ApiResponse.success(
            resultat,
            "Demandeur et demandes récupérés avec succès"
        );
        return ResponseEntity.ok(response);
      }

      ApiResponse<DemandeurDemandesDTO> response = ApiResponse.success(
          null,
          "Aucun demandeur trouvé avec ce numero"
      );
      return ResponseEntity.ok(response);

    } catch (RuntimeException e) {
      log.error("Erreur métier recherche demandeur: {}", e.getMessage());

      ApiResponse<DemandeurDemandesDTO> response = ApiResponse.error(
          400,
          e.getMessage(),
          "Erreur lors de la recherche du demandeur"
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

}