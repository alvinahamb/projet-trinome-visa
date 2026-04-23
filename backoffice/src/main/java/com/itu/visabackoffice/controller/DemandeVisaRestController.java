package com.itu.visabackoffice.controller;

import com.itu.visabackoffice.dto.ApiResponse;
import com.itu.visabackoffice.dto.DonneesDemandeVisaDTO;
import com.itu.visabackoffice.dto.DemandeVisaSaisieDTO;
import com.itu.visabackoffice.dto.DemandeVisaCplDTO;
import java.util.List;
import com.itu.visabackoffice.services.DemandeVisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/demande-visa")
@Slf4j
public class DemandeVisaRestController {

  @Autowired
  private DemandeVisaService demandeVisaService;

  @GetMapping("/demande-saisie-form")
  public ResponseEntity<ApiResponse<DonneesDemandeVisaDTO>> demandeVisaSaisie() {
    try {
      log.info("Récupération données formulaire demande visa");
      DonneesDemandeVisaDTO donnees = demandeVisaService.getDonneesDemandeVisa();
      
      ApiResponse<DonneesDemandeVisaDTO> response = ApiResponse.success(
          donnees, 
          "Données formulaire chargées avec succès"
      );
      
      return ResponseEntity.ok(response);
      
    } catch (RuntimeException e) {
      log.error("Erreur métier: {}", e.getMessage());
      
      ApiResponse<DonneesDemandeVisaDTO> response = ApiResponse.error(
          500,
          e.getMessage(),
          "Une erreur est survenue lors du chargement du formulaire"
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

  /**
   * Endpoint pour créer une nouvelle demande de visa
   * @param demandeSaisie les données saisies du formulaire
   * @return ApiResponse avec le résultat de la création
   */
  @PostMapping("/demande-saisie-ajout")
  public ResponseEntity<ApiResponse<Object>> ajouterDemandeVisa(@RequestBody DemandeVisaSaisieDTO demandeSaisie) {
    try {
      log.info("Création demande visa - Demandeur: {} {}", demandeSaisie.getNom(), demandeSaisie.getPrenom());
      
      Object resultat = demandeVisaService.enregistrerDemandeVisa(demandeSaisie);
      
      ApiResponse<Object> response = ApiResponse.success(
          resultat,
          "Demande de visa enregistrée avec succès"
      );
      
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
      
    } catch (RuntimeException e) {
      log.error("Erreur métier création demande: {}", e.getMessage());
      
      ApiResponse<Object> response = ApiResponse.error(
          400,
          e.getMessage(),
          "Erreur lors de la création de la demande"
      );
      
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      
    } catch (Exception e) {
      log.error("Erreur inattendue création demande: {}", e.getMessage(), e);
      
      ApiResponse<Object> response = ApiResponse.error(
          500,
          "Erreur serveur interne",
          "Une erreur inattendue s'est produite"
      );
      
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * Endpoint pour récupérer la liste de toutes les demandes au format affichage
   * @return ApiResponse avec la liste des demandes
   */
  @GetMapping("/demande-list")
  public ResponseEntity<ApiResponse<List<DemandeVisaCplDTO>>> getListeDemandes() {
    try {
      log.info("Récupération de la liste des demandes");
      
      List<DemandeVisaCplDTO> demandes = demandeVisaService.getListeDemandesComprises();
      
      ApiResponse<List<DemandeVisaCplDTO>> response = ApiResponse.success(
          demandes,
          "Liste des demandes récupérée avec succès"
      );
      
      return ResponseEntity.ok(response);
      
    } catch (RuntimeException e) {
      log.error("Erreur métier récupération demandes: {}", e.getMessage());
      
      ApiResponse<List<DemandeVisaCplDTO>> response = ApiResponse.error(
          500,
          e.getMessage(),
          "Une erreur est survenue lors de la récupération des demandes"
      );
      
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
      
    } catch (Exception e) {
      log.error("Erreur inattendue récupération demandes: {}", e.getMessage(), e);
      
      ApiResponse<List<DemandeVisaCplDTO>> response = ApiResponse.error(
          500,
          "Erreur serveur interne",
          "Une erreur inattendue s'est produite"
      );
      
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * Endpoint pour mettre à jour une demande (seulement si statut = En attente)
   * @param demandeId l'ID de la demande à mettre à jour
   * @param demandeSaisie les nouvelles données
   * @return ApiResponse avec le résultat de la mise à jour
   */
  @PutMapping("/demande-update/{id}")
  public ResponseEntity<ApiResponse<Object>> updateDemandeVisa(
      @PathVariable("id") Integer demandeId,
      @RequestBody DemandeVisaSaisieDTO demandeSaisie) {
    try {
      log.info("Mise à jour demande visa ID: {}", demandeId);
      
      Object resultat = demandeVisaService.updateDemandeVisa(demandeId, demandeSaisie);
      
      ApiResponse<Object> response = ApiResponse.success(
          resultat,
          "Demande de visa mise à jour avec succès"
      );
      
      return ResponseEntity.ok(response);
      
    } catch (RuntimeException e) {
      log.error("Erreur métier mise à jour demande: {}", e.getMessage());
      
      ApiResponse<Object> response = ApiResponse.error(
          400,
          e.getMessage(),
          "Erreur lors de la mise à jour de la demande"
      );
      
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      
    } catch (Exception e) {
      log.error("Erreur inattendue mise à jour demande: {}", e.getMessage(), e);
      
      ApiResponse<Object> response = ApiResponse.error(
          500,
          "Erreur serveur interne",
          "Une erreur inattendue s'est produite"
      );
      
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }
}
