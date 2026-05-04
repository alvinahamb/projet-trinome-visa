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
   * Endpoint pour créer une nouvelle demande de visa avec fichiers
non je eids download les fichiers deja uploader   * @param demandeData les données saisies du formulaire
   * @param files les fichiers des pièces justificatives
   * @return ApiResponse avec le résultat de la création
   */
  @PostMapping(value = "/demande-saisie-ajout", consumes = "multipart/form-data")
  public ResponseEntity<ApiResponse<Object>> ajouterDemandeVisa(
      DemandeVisaSaisieDTO demandeData,
      MultipartHttpServletRequest request) {
    try {
      log.info("Création demande visa avec fichiers - Demandeur: {} {}", 
          demandeData.getNom(), demandeData.getPrenom());
      
      // Passer les données et fichiers au service
      Object resultat = demandeVisaService.enregistrerDemandeVisaAvecFichiers(demandeData, request.getFileMap());
      
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
  @PutMapping(value = "/demande-update/{id}", consumes = "multipart/form-data")
  public ResponseEntity<ApiResponse<DemandeVisaCplDTO>> updateDemandeVisa(
      @PathVariable("id") Integer demandeId,
      DemandeVisaSaisieDTO demandeSaisie,
      MultipartHttpServletRequest request) {
    try {
      log.info("Mise à jour demande visa ID: {}", demandeId);
      
      DemandeVisaCplDTO resultat = demandeVisaService.updateDemandeVisaAvecFichiers(
          demandeId, demandeSaisie, request.getFileMap());
      
      ApiResponse<DemandeVisaCplDTO> response = ApiResponse.success(
          resultat,
          "Demande de visa mise à jour avec succès"
      );
      
      return ResponseEntity.ok(response);
      
    } catch (RuntimeException e) {
      log.error("Erreur métier mise à jour demande: {}", e.getMessage());
      
      ApiResponse<DemandeVisaCplDTO> response = ApiResponse.error(
          400,
          e.getMessage(),
          "Erreur lors de la mise à jour de la demande"
      );
      
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      
    } catch (Exception e) {
      log.error("Erreur inattendue mise à jour demande: {}", e.getMessage(), e);
      
      ApiResponse<DemandeVisaCplDTO> response = ApiResponse.error(
          500,
          "Erreur serveur interne",
          "Une erreur inattendue s'est produite"
      );
      
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * Endpoint pour rechercher un VISA par sa référence - utilisé pour la fonctionnalité duplicata
   * @param reference la référence du visa
   * @return ApiResponse avec les informations du visa et du demandeur si trouvé
   */
  @GetMapping("/search")
  public ResponseEntity<ApiResponse<Object>> searchVisaByReference(@RequestParam String reference) {
    try {
      log.info("Recherche VISA par référence: {}", reference);
      
      Object resultat = demandeVisaService.searchVisaByReference(reference);
      
      if (resultat != null) {
        ApiResponse<Object> response = ApiResponse.success(
            resultat,
            "Visa trouvé avec succès"
        );
        return ResponseEntity.ok(response);
      } else {
        ApiResponse<Object> response = ApiResponse.success(
            null,
            "Aucun visa trouvé avec cette référence"
        );
        return ResponseEntity.ok(response);
      }
      
    } catch (RuntimeException e) {
      log.error("Erreur métier recherche visa: {}", e.getMessage());
      
      ApiResponse<Object> response = ApiResponse.error(
          400,
          e.getMessage(),
          "Erreur lors de la recherche du visa"
      );
      
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      
    } catch (Exception e) {
      log.error("Erreur inattendue recherche visa: {}", e.getMessage(), e);
      
      ApiResponse<Object> response = ApiResponse.error(
          500,
          "Erreur serveur interne",
          "Une erreur inattendue s'est produite"
      );
      
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * Endpoint pour télécharger un fichier d'une pièce justificative
   * @param pieceId l'ID de la pièce justificative
   * @return le fichier en téléchargement
   */
  @GetMapping("/download-piece/{pieceId}")
  public ResponseEntity<?> downloadPiece(@PathVariable Integer pieceId) {
    try {
      log.info("Téléchargement pièce justificative ID: {}", pieceId);
      
      // Récupérer les informations du fichier depuis la base de données
      com.itu.visabackoffice.models.DemandePieceJustificative pieceInfo = 
          demandeVisaService.getPieceJustificativeInfo(pieceId);
      
      if (pieceInfo == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pièce non trouvée");
      }
      
      // Construire le chemin du fichier
      Path filePath = Paths.get(pieceInfo.getPath());
      
      // Vérifier que le fichier existe
      if (!Files.exists(filePath)) {
        log.warn("Fichier non trouvé: {}", filePath);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fichier non trouvé");
      }
      
      // Lire le contenu du fichier
      byte[] fileContent = Files.readAllBytes(filePath);
      
      // Déterminer le type de contenu
      String contentType = "application/pdf";
      if (pieceInfo.getNomFichier().endsWith(".pdf")) {
        contentType = "application/pdf";
      } else if (pieceInfo.getNomFichier().endsWith(".doc") || pieceInfo.getNomFichier().endsWith(".docx")) {
        contentType = "application/msword";
      }
      
      // Retourner le fichier
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, 
              "attachment; filename=\"" + pieceInfo.getNomFichier() + "\"")
          .contentType(MediaType.parseMediaType(contentType))
          .body(fileContent);
      
    } catch (Exception e) {
      log.error("Erreur téléchargement pièce: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erreur lors du téléchargement");
    }
  }
}
