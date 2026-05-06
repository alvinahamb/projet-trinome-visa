package com.itu.visabackoffice.services;

import com.itu.visabackoffice.dto.DonneesDemandeVisaDTO;
import com.itu.visabackoffice.dto.DemandeVisaSaisieDTO;
import com.itu.visabackoffice.dto.DemandeVisaCplDTO;
import com.itu.visabackoffice.dto.TransfertVisaSaisieDTO;
import com.itu.visabackoffice.dto.DemandeurDemandesDTO;
import com.itu.visabackoffice.dto.DemandeurInfoDTO;
import com.itu.visabackoffice.dto.DemandeFicheDTO;
import com.itu.visabackoffice.dto.HistoriqueStatutDTO;
import com.itu.visabackoffice.exceptions.ExpectedException;
import com.itu.visabackoffice.models.*;
import com.itu.visabackoffice.repositories.GenreRepository;
import com.itu.visabackoffice.repositories.SituationFamilialeRepository;
import com.itu.visabackoffice.repositories.VisaTypeRepository;
import com.itu.visabackoffice.repositories.PieceJustificativeRepository;
import com.itu.visabackoffice.repositories.DemandeurRepository;
import com.itu.visabackoffice.repositories.PasseportRepository;
import com.itu.visabackoffice.repositories.VisaTransformableRepository;
import com.itu.visabackoffice.repositories.DemandeRepository;
import com.itu.visabackoffice.repositories.DemandePieceJustificativeRepository;
import com.itu.visabackoffice.repositories.StatutDemandeRepository;
import com.itu.visabackoffice.repositories.HistoriqueStatutDemandeRepository;
import com.itu.visabackoffice.repositories.DemandeTypeRepository;
import com.itu.visabackoffice.repositories.NationaliteRepository;
import com.itu.visabackoffice.repositories.CarteResidentRepository;
import com.itu.visabackoffice.repositories.VisaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
public class DemandeVisaService {

  public static final String SANS_DONNEES_ANTERIEURS = "NO_VISA_FOUND";

  private static final long PASSPORT_DELIVRANCE_MONTHS_BEFORE = 12;
  private static final String DEMANDE_FICHE_BASE_URL = "localhost:5173/demande-fiche/";

  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private SituationFamilialeRepository situationFamilialeRepository;

  @Autowired
  private VisaTypeRepository visaTypeRepository;

  @Autowired
  private PieceJustificativeRepository pieceJustificativeRepository;

  @Autowired
  private DemandeurRepository demandeurRepository;

  @Autowired
  private PasseportRepository passeportRepository;

  @Autowired
  private VisaTransformableRepository visaTransformableRepository;

  @Autowired
  private DemandeRepository demandeRepository;

  @Autowired
  private DemandePieceJustificativeRepository demandePieceJustificativeRepository;

  @Autowired
  private StatutDemandeRepository statutDemandeRepository;

  @Autowired
  private HistoriqueStatutDemandeRepository historiqueStatutDemandeRepository;

  @Autowired
  private DemandeTypeRepository demandeTypeRepository;

  @Autowired
  private NationaliteRepository nationaliteRepository;

  @Autowired
  private CarteResidentRepository carteResidentRepository;

  @Autowired
  private VisaRepository visaRepository;

  /**
   * Récupère toutes les données nécessaires pour une demande de visa
   * 
   * @return DonneesDemandeVisaDTO contenant les genres, situations familiales,
   *         types de visa et pièces justificatives
   * @throws RuntimeException en cas d'erreur lors de la récupération
   */
  public DonneesDemandeVisaDTO getDonneesDemandeVisa() {
    log.info("[INFO] Début de la récupération des données de demande de visa");

    try {
      // Vérifier que les repositories sont injectés
      if (Objects.isNull(genreRepository)) {
        log.warn("[WARNING] GenreRepository n'est pas injecté correctement");
        throw new IllegalStateException("GenreRepository n'est pas initialisé");
      }
      if (Objects.isNull(situationFamilialeRepository)) {
        log.warn("[WARNING] SituationFamilialeRepository n'est pas injecté correctement");
        throw new IllegalStateException("SituationFamilialeRepository n'est pas initialisé");
      }
      if (Objects.isNull(visaTypeRepository)) {
        log.warn("[WARNING] VisaTypeRepository n'est pas injecté correctement");
        throw new IllegalStateException("VisaTypeRepository n'est pas initialisé");
      }
      if (Objects.isNull(pieceJustificativeRepository)) {
        log.warn("[WARNING] PieceJustificativeRepository n'est pas injecté correctement");
        throw new IllegalStateException("PieceJustificativeRepository n'est pas initialisé");
      }

      log.info("[INFO] Récupération de tous les genres");
      var genres = genreRepository.findAll();
      log.info("[INFO] {} genre(s) récupéré(s)", genres.size());

      log.info("[INFO] Récupération de toutes les situations familiales");
      var situationsFamiliales = situationFamilialeRepository.findAll();
      log.info("[INFO] {} situation(s) familiale(s) récupérée(s)", situationsFamiliales.size());

      log.info("[INFO] Récupération de tous les types de visa");
      var typesVisa = visaTypeRepository.findAll();
      log.info("[INFO] {} type(s) de visa récupéré(s)", typesVisa.size());

      log.info("[INFO] Récupération de toutes les nationalités");
      var nationalites = nationaliteRepository.findAll();
      log.info("[INFO] {} nationalité(s) récupérée(s)", nationalites.size());

      log.info("[INFO] Récupération de toutes les pièces justificatives");
      var piecesJustificatives = pieceJustificativeRepository.findAll();
      log.info("[INFO] {} pièce(s) justificative(s) récupérée(s)", piecesJustificatives.size());

      DonneesDemandeVisaDTO donnees = DonneesDemandeVisaDTO.builder()
          .genres(genres)
          .situationsFamiliales(situationsFamiliales)
          .nationalites(nationalites)
          .typesVisa(typesVisa)
          .piecesJustificatives(piecesJustificatives)
          .build();

      log.info("[INFO] Données de demande de visa construites avec succès");
      return donnees;

    } catch (IllegalStateException e) {
      log.error("[ERROR] Erreur d'injection de dépendance: {}", e.getMessage());
      log.error("[ERROR] StackTrace:", e);
      throw new RuntimeException("Erreur d'injection de dépendance: " + e.getMessage(), e);

    } catch (NullPointerException e) {
      log.error("[ERROR] Erreur de pointeur null lors de la récupération des données: {}", e.getMessage());
      log.error("[ERROR] StackTrace:", e);
      throw new RuntimeException("Erreur: une donnée requise est null", e);

    } catch (Exception e) {
      log.error("[ERROR] Erreur inattendue lors de la récupération des données de demande de visa: {}", e.getMessage());
      log.error("[ERROR] Type d'erreur: {}", e.getClass().getName());
      log.error("[ERROR] StackTrace:", e);
      throw new RuntimeException("Erreur lors de la récupération des données: " + e.getMessage(), e);
    }
  }

  private void validerChampObligatoire(String valeur, String libelle) {
    if (valeur == null || valeur.trim().isEmpty()) {
      throw new IllegalArgumentException(libelle + " est obligatoire");
    }
  }

  private void validerChampObligatoire(Integer valeur, String libelle) {
    if (valeur == null) {
      throw new IllegalArgumentException(libelle + " est obligatoire");
    }
  }

  private void validerChampObligatoire(LocalDate valeur, String libelle) {
    if (valeur == null) {
      throw new IllegalArgumentException(libelle + " est obligatoire");
    }
  }

  private void setDemandeUrl(Demande demande) {
    if (demande == null || demande.getId() == null) {
      return;
    }

    demande.setUrlDemande(DEMANDE_FICHE_BASE_URL + demande.getId());
  }

  private StatutDemande getStatut(String libelle) {
    return statutDemandeRepository.findByLibelleIgnoreCase(libelle)
        .orElseThrow(() -> new IllegalArgumentException("Statut '" + libelle + "' non trouvé"));
  }

  private void validerChampsObligatoiresCreation(DemandeVisaSaisieDTO demandeSaisie) {
    validerChampObligatoire(demandeSaisie.getNom(), "Le nom");
    validerChampObligatoire(demandeSaisie.getPrenom(), "Le prénom");
    validerChampObligatoire(demandeSaisie.getGenre(), "Le genre");
    validerChampObligatoire(demandeSaisie.getDateNaissance(), "La date de naissance");
    validerChampObligatoire(demandeSaisie.getNationalite(), "La nationalité");
    validerChampObligatoire(demandeSaisie.getNumeroPasseport(), "Le numéro du passeport");
    validerChampObligatoire(demandeSaisie.getDateDelivrance(), "La date de délivrance");
    validerChampObligatoire(demandeSaisie.getDateExpiration(), "La date d'expiration");
    validerChampObligatoire(demandeSaisie.getReferenceVisa(), "La référence du visa");
    validerChampObligatoire(demandeSaisie.getVisaType(), "Le type de visa");
  }

  private void validerChampsObligatoiresMiseAJour(DemandeVisaSaisieDTO demandeSaisie) {
    validerChampsObligatoiresCreation(demandeSaisie);
  }

  /**
   * Enregistre une demande de duplicata (2 demandes: original completed +
   * duplicata pending)
   * 
   * @param demandeur            le demandeur
   * @param visaType             le type de visa
   * @param demandeType          le type de demande normal (id=1)
   * @param demandeTypeDuplicata le type de demande duplicata (id=3)
   * @param demandeSaisie        les données saisies
   * @return Demande duplicata créée
   */
  private Demande enregistrerDemandeDuplicata(Demandeur demandeur, Passeport passeport, VisaType visaType,
      VisaTransformable visaTransformable, DemandeType demandeType,
      DemandeType demandeTypeDuplicata, DemandeVisaSaisieDTO demandeSaisie) {
    log.info("Création de 2 demandes (original completed + duplicata pending)");

    // ========== PREMIÈRE DEMANDE (ORIGINAL - COMPLÉTÉE) ==========
    log.info("Sauvegarde de la première demande (original completed)");
    Demande demandeOriginal = new Demande();
    demandeOriginal.setDemandeur(demandeur);
    demandeOriginal.setPasseport(passeport);
    demandeOriginal.setVisaTransformable(visaTransformable);
    demandeOriginal.setTypeVisa(visaType);
    demandeOriginal.setTypeDemande(demandeType); // demande_type = 1
    demandeOriginal = demandeRepository.saveAndFlush(demandeOriginal);
    setDemandeUrl(demandeOriginal);
    demandeOriginal = demandeRepository.saveAndFlush(demandeOriginal);
    log.info("Demande originale sauvegardée avec l'ID: {}", demandeOriginal.getId());

    // Créer l'historique avec statut TERMINE
    StatutDemande statutCompleted = getStatut("TERMINE");
    HistoriqueStatutDemande historiqueOriginal = new HistoriqueStatutDemande();
    historiqueOriginal.setDemande(demandeOriginal);
    historiqueOriginal.setStatutDemande(statutCompleted);
    historiqueOriginal.setCommentaire("Visa original - déjà traité");
    historiqueStatutDemandeRepository.save(historiqueOriginal);
    log.info("Historique statut COMPLETED créé pour demande originale");

    {
      java.time.LocalDate dateDebut = java.time.LocalDate.now();
      java.time.LocalDate dateFin = dateDebut.plusYears(1);
      String referenceAuto = "AUTO-" + demandeOriginal.getId();

      Visa visa = new Visa();
      visa.setDemande(demandeOriginal);
      visa.setDateDebut(dateDebut);
      visa.setDateFin(dateFin);
      visa.setReference(referenceAuto);
      visa.setLieuEntree(demandeSaisie.getLieuEntree());
      visaRepository.save(visa);
      log.info("Visa créé pour demande originale avec la référence: {}", referenceAuto);

      CarteResident carteResident = new CarteResident();
      carteResident.setDemande(demandeOriginal);
      carteResident.setDateDebut(dateDebut);
      carteResident.setDateFin(dateFin);
      carteResident.setReference(referenceAuto);
      carteResident.setLieuEntree(demandeSaisie.getLieuEntree());
      carteResidentRepository.save(carteResident);
      log.info("Carte résident créée pour demande originale avec la référence: {}", referenceAuto);
    }

    // Ajouter les pièces justificatives à la demande originale
    if (demandeSaisie.getPieces() != null && !demandeSaisie.getPieces().isEmpty()) {
      log.info("Sauvegarde des {} pièces justificatives pour demande originale", demandeSaisie.getPieces().size());
      for (Integer pieceId : demandeSaisie.getPieces()) {
        PieceJustificative pieceJustificative = pieceJustificativeRepository.findById(pieceId)
            .orElseThrow(
                () -> new IllegalArgumentException("Pièce justificative non trouvée avec l'ID: " + pieceId));

        if (!pieceJustificative.getTypeVisa().getId().equals(visaType.getId())) {
          log.warn("Pièce justificative {} n'est pas associée au type de visa {}", pieceId, visaType.getId());
          throw new IllegalArgumentException(
              "La pièce justificative (id: " + pieceId + ") n'est pas valide pour le type de visa sélectionné");
        }

        DemandePieceJustificative demandePiece = new DemandePieceJustificative();
        demandePiece.setDemande(demandeOriginal);
        demandePiece.setPieceJustificative(pieceJustificative);
        demandePieceJustificativeRepository.save(demandePiece);
        log.info("Pièce justificative {} associée à la demande originale", pieceId);
      }
    }

    // ========== DEUXIÈME DEMANDE (DUPLICATA - EN ATTENTE) ==========
    log.info("Sauvegarde de la deuxième demande (duplicata pending)");
    Demande demandeDuplicata = new Demande();
    demandeDuplicata.setDemandeur(demandeur);
    demandeDuplicata.setPasseport(passeport);
    demandeDuplicata.setVisaTransformable(visaTransformable);
    demandeDuplicata.setTypeVisa(visaType);
    demandeDuplicata.setTypeDemande(demandeTypeDuplicata); // demande_type = 3 (DUPLICATA)
    demandeDuplicata = demandeRepository.saveAndFlush(demandeDuplicata);
    setDemandeUrl(demandeDuplicata);
    demandeDuplicata = demandeRepository.saveAndFlush(demandeDuplicata);
    log.info("Demande duplicata sauvegardée avec l'ID: {}", demandeDuplicata.getId());

    // Créer l'historique avec statut CREE
    StatutDemande statutInitial = getStatut("CREE");
    HistoriqueStatutDemande historiqueDuplicata = new HistoriqueStatutDemande();
    historiqueDuplicata.setDemande(demandeDuplicata);
    historiqueDuplicata.setStatutDemande(statutInitial);
    historiqueDuplicata.setCommentaire("Demande de duplicata créée");
    historiqueStatutDemandeRepository.save(historiqueDuplicata);
    log.info("Historique statut PENDING créé pour demande duplicata");

    // Ajouter les pièces justificatives à la demande duplicata
    if (demandeSaisie.getPieces() != null && !demandeSaisie.getPieces().isEmpty()) {
      log.info("Sauvegarde des {} pièces justificatives pour demande duplicata", demandeSaisie.getPieces().size());
      for (Integer pieceId : demandeSaisie.getPieces()) {
        PieceJustificative pieceJustificative = pieceJustificativeRepository.findById(pieceId)
            .orElseThrow(
                () -> new IllegalArgumentException("Pièce justificative non trouvée avec l'ID: " + pieceId));

        if (!pieceJustificative.getTypeVisa().getId().equals(visaType.getId())) {
          log.warn("Pièce justificative {} n'est pas associée au type de visa {}", pieceId, visaType.getId());
          throw new IllegalArgumentException(
              "La pièce justificative (id: " + pieceId + ") n'est pas valide pour le type de visa sélectionné");
        }

        DemandePieceJustificative demandePiece = new DemandePieceJustificative();
        demandePiece.setDemande(demandeDuplicata);
        demandePiece.setPieceJustificative(pieceJustificative);
        demandePieceJustificativeRepository.save(demandePiece);
        log.info("Pièce justificative {} associée à la demande duplicata", pieceId);
      }
    }

    log.info("Persistence réussie - Duplicata enregistré avec succès (2 demandes créées)");
    return demandeDuplicata; // Retourner la demande duplicata (la plus récente)
  }

  /**
   * Enregistre une demande de transfert de visa (2 demandes: transfer completed +
   * transfer pending)
   * 
   * @param demandeur     le demandeur
   * @param visaType      le type de visa
   * @param demandeType   le type de demande normal (id=1)
   * @param demandeSaisie les données saisies
   * @return Demande transfert pending créée
   */
  private Demande enregistrerDemandeTransfertVisa(Demandeur demandeur, Passeport nouveauPasseport,
      VisaType visaType,
      VisaTransformable visaTransformable,
      DemandeType demandeType,
      DemandeVisaSaisieDTO demandeSaisie) {
    log.info("Création de 2 demandes (transfert visa completed + transfert visa pending)");

    // ========== PREMIÈRE DEMANDE (TRANSFERT COMPLETED) ==========
    log.info("Sauvegarde de la première demande (transfert completed)");
    Demande demandeTransfertCompleted = new Demande();
    demandeTransfertCompleted.setDemandeur(demandeur);
    demandeTransfertCompleted.setTypeVisa(visaType);
    demandeTransfertCompleted.setVisaTransformable(visaTransformable);
    demandeTransfertCompleted.setTypeDemande(demandeType); // demande_type = 1

    // Create old fake passport for transfer completed
    Passeport passeport = new Passeport();
    passeport.setDemandeur(demandeur);
    // On n'a pas encore l'id de la demande, mais on peut générer temporairement
    passeport.setNumero("OLD-PASSPORT-TEMP");
    passeport.setDateDelivrance(demandeSaisie.getDateDelivrance().minusMonths(PASSPORT_DELIVRANCE_MONTHS_BEFORE));
    // Passeport date debut - 1j
    passeport.setDateExpiration(demandeSaisie.getDateDelivrance().minusDays(1));
    passeport.setPaysDelivrance(demandeSaisie.getPaysDelivrance());
    passeport = passeportRepository.save(passeport);
    log.info("Ancien passeport créé pour demande transfert completed avec le numéro: {}", passeport.getNumero());

    demandeTransfertCompleted.setPasseport(passeport);

    demandeTransfertCompleted = demandeRepository.saveAndFlush(demandeTransfertCompleted);
    setDemandeUrl(demandeTransfertCompleted);
    demandeTransfertCompleted = demandeRepository.saveAndFlush(demandeTransfertCompleted);
    // Update passport number after obtaining demande ID
    passeport.setNumero("OLD-PASSPORT-" + demandeTransfertCompleted.getId());
    passeportRepository.save(passeport);

    log.info("Demande transfert completed sauvegardée avec l'ID: {}", demandeTransfertCompleted.getId());

    // Add both carte resident and visa
    {
      java.time.LocalDate dateDebut = java.time.LocalDate.now();
      java.time.LocalDate dateFin = dateDebut.plusYears(1);
      String referenceAuto = "AUTO-" + demandeTransfertCompleted.getId();

      Visa visa = new Visa();
      visa.setDemande(demandeTransfertCompleted);
      visa.setDateDebut(dateDebut);
      visa.setDateFin(dateFin);
      visa.setReference(referenceAuto);
      visa.setLieuEntree(demandeSaisie.getLieuEntree());
      visaRepository.save(visa);
      log.info("Visa créé pour demande transfert completed avec la référence: {}", referenceAuto);

      CarteResident carteResident = new CarteResident();
      carteResident.setDemande(demandeTransfertCompleted);
      carteResident.setDateDebut(dateDebut);
      carteResident.setDateFin(dateFin);
      carteResident.setReference(referenceAuto);
      carteResident.setLieuEntree(demandeSaisie.getLieuEntree());
      carteResidentRepository.save(carteResident);
      log.info("Carte résident créée pour demande transfert completed avec la référence: {}", referenceAuto);
    }

    // Créer l'historique avec statut COMPLETED (id=3)
    StatutDemande statutCompleted = statutDemandeRepository.findById(3)
        .orElseThrow(() -> new IllegalArgumentException("Statut COMPLETED (id=3) non trouvé"));
    HistoriqueStatutDemande historiqueTransfertCompleted = new HistoriqueStatutDemande();
    historiqueTransfertCompleted.setDemande(demandeTransfertCompleted);
    historiqueTransfertCompleted.setStatutDemande(statutCompleted);
    historiqueTransfertCompleted.setCommentaire("Ancien passeport - transfert traité");
    historiqueStatutDemandeRepository.save(historiqueTransfertCompleted);
    log.info("Historique statut COMPLETED créé pour demande transfert completed");

    // Ajouter les pièces justificatives à la demande transfert completed
    if (demandeSaisie.getPieces() != null && !demandeSaisie.getPieces().isEmpty()) {
      log.info("Sauvegarde des {} pièces justificatives pour demande transfert completed",
          demandeSaisie.getPieces().size());
      for (Integer pieceId : demandeSaisie.getPieces()) {
        PieceJustificative pieceJustificative = pieceJustificativeRepository.findById(pieceId)
            .orElseThrow(
                () -> new IllegalArgumentException("Pièce justificative non trouvée avec l'ID: " + pieceId));

        if (!pieceJustificative.getTypeVisa().getId().equals(visaType.getId())) {
          log.warn("Pièce justificative {} n'est pas associée au type de visa {}", pieceId, visaType.getId());
          throw new IllegalArgumentException(
              "La pièce justificative (id: " + pieceId + ") n'est pas valide pour le type de visa sélectionné");
        }

        DemandePieceJustificative demandePiece = new DemandePieceJustificative();
        demandePiece.setDemande(demandeTransfertCompleted);
        demandePiece.setPieceJustificative(pieceJustificative);
        demandePieceJustificativeRepository.save(demandePiece);
        log.info("Pièce justificative {} associée à la demande transfert completed", pieceId);
      }
    }

    // ========== DEUXIÈME DEMANDE (TRANSFERT PENDING) ==========
    log.info("Sauvegarde de la deuxième demande (transfert pending)");
    Demande demandeTransfertPending = new Demande();
    demandeTransfertPending.setDemandeur(demandeur);
    demandeTransfertPending.setPasseport(nouveauPasseport);
    demandeTransfertPending.setVisaTransformable(visaTransformable);
    demandeTransfertPending.setTypeVisa(visaType);
    demandeTransfertPending.setTypeDemande(demandeType); // demande_type = 1
    demandeTransfertPending = demandeRepository.saveAndFlush(demandeTransfertPending);
    setDemandeUrl(demandeTransfertPending);
    demandeTransfertPending = demandeRepository.saveAndFlush(demandeTransfertPending);
    log.info("Demande transfert pending sauvegardée avec l'ID: {}", demandeTransfertPending.getId());

    // Créer l'historique avec statut PENDING (id=1)
    StatutDemande statutInitial = statutDemandeRepository.findById(1)
        .orElseThrow(() -> new IllegalArgumentException("Statut initial (id=1) non trouvé"));
    HistoriqueStatutDemande historiqueTransfertPending = new HistoriqueStatutDemande();
    historiqueTransfertPending.setDemande(demandeTransfertPending);
    historiqueTransfertPending.setStatutDemande(statutInitial);
    historiqueTransfertPending.setCommentaire("Demande de transfert visa créée avec nouveau passeport");
    historiqueStatutDemandeRepository.save(historiqueTransfertPending);
    log.info("Historique statut PENDING créé pour demande transfert pending");

    // Ajouter les pièces justificatives à la demande transfert pending
    if (demandeSaisie.getPieces() != null && !demandeSaisie.getPieces().isEmpty()) {
      log.info("Sauvegarde des {} pièces justificatives pour demande transfert pending",
          demandeSaisie.getPieces().size());
      for (Integer pieceId : demandeSaisie.getPieces()) {
        PieceJustificative pieceJustificative = pieceJustificativeRepository.findById(pieceId)
            .orElseThrow(
                () -> new IllegalArgumentException("Pièce justificative non trouvée avec l'ID: " + pieceId));

        if (!pieceJustificative.getTypeVisa().getId().equals(visaType.getId())) {
          log.warn("Pièce justificative {} n'est pas associée au type de visa {}", pieceId, visaType.getId());
          throw new IllegalArgumentException(
              "La pièce justificative (id: " + pieceId + ") n'est pas valide pour le type de visa sélectionné");
        }

        DemandePieceJustificative demandePiece = new DemandePieceJustificative();
        demandePiece.setDemande(demandeTransfertPending);
        demandePiece.setPieceJustificative(pieceJustificative);
        demandePieceJustificativeRepository.save(demandePiece);
        log.info("Pièce justificative {} associée à la demande transfert pending", pieceId);
      }
    }

    log.info("Persistence réussie - Transfert visa enregistré avec succès (2 demandes créées)");
    return demandeTransfertPending; // Retourner la demande transfert pending (la plus récente)
  }

  /**
   * Enregistre une nouvelle demande de visa avec tous les objets associés
   * 
   * @param demandeSaisie les données saisies du formulaire
   * @return Demande créée
   * @throws RuntimeException en cas d'erreur
   */
  @Transactional
  public Demande enregistrerDemandeVisa(DemandeVisaSaisieDTO demandeSaisie) {
    log.info("Début enregistrement demande visa - Demandeur: {} {}", demandeSaisie.getNom(), demandeSaisie.getPrenom());

    try {
      // ========== VALIDATION DES DONNÉES ==========
      log.info("Validation des données de la demande");

      validerChampsObligatoiresCreation(demandeSaisie);

      // Validation des dates du passeport
      if (demandeSaisie.getDateExpiration() != null && demandeSaisie.getDateDelivrance() != null) {
        if (demandeSaisie.getDateExpiration().isBefore(demandeSaisie.getDateDelivrance())) {
          log.error("La date d'expiration du passeport est antérieure à la date de délivrance");
          throw new IllegalArgumentException("La date d'expiration doit être postérieure à la date de délivrance");
        }
      }

      log.info("Validation réussie");

      // ========== CRÉATION DES OBJETS SANS PERSISTENCE ==========
      log.info("Création des objets entité");

      // 1. Créer le Demandeur
      log.info("Création objet Demandeur: {} {}", demandeSaisie.getNom(), demandeSaisie.getPrenom());

      // Récupérer Genre et Nationalite depuis la DB
      Genre genre = genreRepository.findById(demandeSaisie.getGenre())
          .orElseThrow(() -> new IllegalArgumentException("Genre non trouvé avec l'ID: " + demandeSaisie.getGenre()));
      log.info("Genre trouvé: {}", genre.getId());

      // Récupérer Nationalite
      if (demandeSaisie.getNationalite() == null) {
        log.error("La nationalité est manquante");
        throw new IllegalArgumentException("La nationalité est obligatoire");
      }
      Nationalite nationalite = nationaliteRepository.findById(demandeSaisie.getNationalite())
          .orElseThrow(() -> new IllegalArgumentException(
              "Nationalité non trouvée avec l'ID: " + demandeSaisie.getNationalite()));
      log.info("Nationalité trouvée: {}", nationalite.getId());

      // Récupérer SituationFamiliale (optionnelle)
      SituationFamiliale situationFamiliale = situationFamilialeRepository
          .findById(demandeSaisie.getSituationFamiliale())
          .orElse(null);
      if (situationFamiliale != null) {
        log.info("Situation familiale trouvée: {}", situationFamiliale.getId());
      } else {
        log.warn("Aucune situation familiale sélectionnée");
      }

      Demandeur demandeur = new Demandeur();
      demandeur.setNom(demandeSaisie.getNom());
      demandeur.setPrenom(demandeSaisie.getPrenom());
      demandeur.setGenre(genre);
      demandeur.setNationalite(nationalite);
      demandeur.setDateNaissance(demandeSaisie.getDateNaissance());
      demandeur.setLieuNaissance(demandeSaisie.getLieuNaissance());
      demandeur.setTelephone(demandeSaisie.getTelephone());
      demandeur.setEmail(demandeSaisie.getEmail());
      demandeur.setAdresse(demandeSaisie.getAdresse());
      if (situationFamiliale != null) {
        demandeur.setSituationFamiliale(situationFamiliale);
      }

      // 2. Créer le Passeport
      log.info("Création objet Passeport");
      Passeport passeport = new Passeport();
      passeport.setNumero(demandeSaisie.getNumeroPasseport());
      passeport.setDateDelivrance(demandeSaisie.getDateDelivrance());
      passeport.setDateExpiration(demandeSaisie.getDateExpiration());
      passeport.setPaysDelivrance(demandeSaisie.getPaysDelivrance());

      // 3. Créer le Visa Transformable
      log.info("Création objet VisaTransformable");
      VisaTransformable visaTransformable = new VisaTransformable();
      visaTransformable.setReference(demandeSaisie.getReferenceVisa());
      visaTransformable.setDateEntree(demandeSaisie.getDateEntree());
      visaTransformable.setDateFin(demandeSaisie.getDateFin());
      visaTransformable.setLieuEntree(demandeSaisie.getLieuEntree());

      // 4. Récupérer DemandeType (par défaut : id = 1 pour normal)
      log.info("Récupération type de demande");
      DemandeType demandeType = demandeTypeRepository.findById(1)
          .orElseThrow(() -> new IllegalArgumentException("Type de demande par défaut non trouvé"));
      log.info("Type de demande trouvé: {}", demandeType.getId());

      // Récupérer demandeType pour duplicata si nécessaire (id = 3)
      DemandeType demandeTypeDuplicata = null;
      if (demandeSaisie.getIsDuplicata() != null && demandeSaisie.getIsDuplicata()) {
        demandeTypeDuplicata = demandeTypeRepository.findById(3)
            .orElseThrow(() -> new IllegalArgumentException("Type de demande DUPLICATA (id=3) non trouvé"));
        log.info("Type de demande DUPLICATA trouvé: {}", demandeTypeDuplicata.getId());
      }

      // 5. Créer la Demande
      log.info("Création objet Demande");
      Demande demande = new Demande();

      log.info("Objets créés avec succès (sans persistence)");

      // ========== PERSISTENCE DES OBJETS ==========
      log.info("Début de la persistence des objets");

      // Sauvegarder Demandeur
      log.info("Sauvegarde Demandeur");
      demandeur = demandeurRepository.save(demandeur);
      log.info("Demandeur sauvegardé avec l'ID: {}", demandeur.getId());

      // Sauvegarder Passeport et setter la FK
      log.info("Sauvegarde Passeport");
      passeport.setDemandeur(demandeur);
      passeport = passeportRepository.save(passeport);
      log.info("Passeport sauvegardé avec l'ID: {}", passeport.getId());

      // Sauvegarder VisaTransformable et setter la FK
      log.info("Sauvegarde VisaTransformable");
      visaTransformable.setDemandeur(demandeur);
      visaTransformable = visaTransformableRepository.save(visaTransformable);
      log.info("VisaTransformable sauvegardé avec l'ID: {}", visaTransformable.getId());

      // Récupérer VisaType depuis la DB
      VisaType visaType = visaTypeRepository.findById(demandeSaisie.getVisaType())
          .orElseThrow(
              () -> new IllegalArgumentException("Type de visa non trouvé avec l'ID: " + demandeSaisie.getVisaType()));
      log.info("Type de visa trouvé: {}", visaType.getId());

      // ========== HANDLING DUPLICATA & TRANSFER VISA ==========
      boolean isDuplicata = demandeSaisie.getIsDuplicata() != null && demandeSaisie.getIsDuplicata();
      boolean isTransferVisa = demandeSaisie.getIsTransferVisa() != null && demandeSaisie.getIsTransferVisa();

      if (isDuplicata) {
        demande = enregistrerDemandeDuplicata(demandeur, passeport, visaType, visaTransformable, demandeType,
            demandeTypeDuplicata,
            demandeSaisie);
      } else if (isTransferVisa) {
        demande = enregistrerDemandeTransfertVisa(demandeur, passeport, visaType, visaTransformable, demandeType,
            demandeSaisie);
      } else {
        // ========== CAS NORMAL (SANS DUPLICATA, SANS TRANSFER VISA) ==========
        log.info("Sauvegarde Demande (cas normal)");
        demande.setDemandeur(demandeur);
        demande.setPasseport(passeport);
        demande.setVisaTransformable(visaTransformable);
        demande.setTypeVisa(visaType);
        demande.setTypeDemande(demandeType);
        demande = demandeRepository.saveAndFlush(demande);
        log.info("Demande sauvegardée avec l'ID: {}", demande.getId());

        // ========== CRÉATION DE L'HISTORIQUE STATUT ==========
        log.info("Création de l'historique statut initial");
        StatutDemande statutInitial = statutDemandeRepository.findById(1)
            .orElseThrow(() -> new IllegalArgumentException("Statut initial (id=1) non trouvé"));

        HistoriqueStatutDemande historique = new HistoriqueStatutDemande();
        historique.setDemande(demande);
        historique.setStatutDemande(statutInitial);
        historique.setCommentaire("Demande créée");
        historiqueStatutDemandeRepository.save(historique);
        log.info("Historique statut créé avec succès");

        // Sauvegarder les DemandePieceJustificative si des pièces ont été choisies
        if (demandeSaisie.getPieces() != null && !demandeSaisie.getPieces().isEmpty()) {
          log.info("Sauvegarde des {} pièces justificatives", demandeSaisie.getPieces().size());
          for (Integer pieceId : demandeSaisie.getPieces()) {
            PieceJustificative pieceJustificative = pieceJustificativeRepository.findById(pieceId)
                .orElseThrow(
                    () -> new IllegalArgumentException("Pièce justificative non trouvée avec l'ID: " + pieceId));

            // Vérifier que la pièce est bien associée au type de visa demandé
            if (!pieceJustificative.getTypeVisa().getId().equals(visaType.getId())) {
              log.warn("Pièce justificative {} n'est pas associée au type de visa {}", pieceId, visaType.getId());
              throw new IllegalArgumentException(
                  "La pièce justificative (id: " + pieceId + ") n'est pas valide pour le type de visa sélectionné");
            }

            DemandePieceJustificative demandePiece = new DemandePieceJustificative();
            demandePiece.setDemande(demande);
            demandePiece.setPieceJustificative(pieceJustificative);
            demandePieceJustificativeRepository.save(demandePiece);
            log.info("Pièce justificative {} associée à la demande", pieceId);
          }
        } else {
          log.warn("Aucune pièce justificative sélectionnée");
        }

        log.info("Persistence réussie - Demande enregistrée avec succès");
      }

      return demande;

    } catch (IllegalArgumentException e) {
      log.error("Erreur validation: {}", e.getMessage());
      throw new RuntimeException("Erreur validation: " + e.getMessage(), e);

    } catch (Exception e) {
      log.error("Erreur enregistrement demande: {}", e.getMessage(), e);
      throw new RuntimeException("Erreur lors de l'enregistrement: " + e.getMessage(), e);
    }
  }

  private void sauvegarderFichiersPieces(Demande demande, Map<String, MultipartFile> files) {
    if (demande == null || files == null || files.isEmpty()) {
      return;
    }

    try {
      String uploadDir = "src/main/resources/uploads/" + demande.getId();
      Path uploadPath = Paths.get(uploadDir);
      Files.createDirectories(uploadPath);
      log.info("Répertoire de stockage créé: {}", uploadPath);

      for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
        String key = entry.getKey();
        MultipartFile file = entry.getValue();

        if (file == null || file.isEmpty()) {
          log.warn("Fichier vide pour la clé: {}", key);
          continue;
        }

        if (!key.startsWith("piece_")) {
          log.warn("Paramètre non reconnu: {}", key);
          continue;
        }

        final Integer pieceId;
        try {
          pieceId = Integer.parseInt(key.substring(6));
        } catch (NumberFormatException e) {
          log.warn("Impossible d'extraire l'ID de la pièce du paramètre: {}", key);
          continue;
        }

        PieceJustificative pieceJustificative = pieceJustificativeRepository.findById(pieceId)
            .orElseThrow(() -> new IllegalArgumentException("Pièce justificative non trouvée avec l'ID: " + pieceId));

        if (demande.getTypeVisa() != null && pieceJustificative.getTypeVisa() != null
            && !pieceJustificative.getTypeVisa().getId().equals(demande.getTypeVisa().getId())) {
          throw new IllegalArgumentException(
              "La pièce justificative (id: " + pieceId + ") n'est pas valide pour le type de visa sélectionné");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".")
            ? originalFilename.substring(originalFilename.lastIndexOf("."))
            : ".pdf";
        String filename = "piece_" + pieceId + "_" + System.currentTimeMillis() + fileExtension;

        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());
        log.info("Fichier sauvegardé: {}", filePath);

        DemandePieceJustificative demandePiece = demandePieceJustificativeRepository
            .findByDemandeIdAndPieceJustificativeId(demande.getId(), pieceId)
            .orElseGet(DemandePieceJustificative::new);

        demandePiece.setDemande(demande);
        demandePiece.setPieceJustificative(pieceJustificative);
        demandePiece.setPath(filePath.toString());
        demandePiece.setNomFichier(originalFilename != null && !originalFilename.isBlank() ? originalFilename : filename);
        demandePiece.setDateAjout(LocalDateTime.now());
        demandePieceJustificativeRepository.save(demandePiece);
        log.info("DemandePieceJustificative enregistrée/mise à jour pour la pièce: {}", pieceId);
      }
    } catch (Exception e) {
      throw new RuntimeException("Erreur lors de l'enregistrement des fichiers: " + e.getMessage(), e);
    }
  }

  private void validerPiecesObligatoires(Demande demande, Map<String, MultipartFile> files) {
    if (demande == null || demande.getTypeVisa() == null) {
      return;
    }

    Integer demandeId = demande.getId();
    Integer visaTypeId = demande.getTypeVisa().getId();

    List<Integer> piecesObligatoires = pieceJustificativeRepository.findAll().stream()
        .filter(piece -> piece.getTypeVisa() != null
            && piece.getTypeVisa().getId().equals(visaTypeId)
            && Boolean.TRUE.equals(piece.getEstObligatoire()))
        .map(PieceJustificative::getId)
        .collect(Collectors.toList());

    for (Integer pieceId : piecesObligatoires) {
      boolean fichierFourni = files != null && files.containsKey("piece_" + pieceId)
          && files.get("piece_" + pieceId) != null
          && !files.get("piece_" + pieceId).isEmpty();

      boolean fichierExistant = false;
      if (demandeId != null) {
        fichierExistant = demandePieceJustificativeRepository
            .findByDemandeIdAndPieceJustificativeId(demandeId, pieceId)
            .map(dpj -> dpj.getPath() != null && !dpj.getPath().isBlank())
            .orElse(false);
      }

      if (!fichierFourni && !fichierExistant) {
        PieceJustificative piece = pieceJustificativeRepository.findById(pieceId).orElse(null);
        String libelle = piece != null ? piece.getLibelle() : String.valueOf(pieceId);
        throw new IllegalArgumentException("La pièce justificative obligatoire '" + libelle + "' doit être ajoutée");
      }
    }
  }

  /**
   * Récupère la liste de toutes les demandes au format complet pour affichage
   * 
   * @return Liste de DemandeVisaCplDTO contenant toutes les demandes
   * @throws RuntimeException en cas d'erreur lors de la récupération
   */
  public List<DemandeVisaCplDTO> getListeDemandesComprises() {
    log.info("[INFO] Début de la récupération de la liste des demandes");

    try {
      // Récupérer toutes les demandes
      var demandes = demandeRepository.findAll();
      log.info("[INFO] {} demande(s) récupérée(s)", demandes.size());

      if (demandes.isEmpty()) {
        log.warn("[WARNING] Aucune demande trouvée");
        return List.of();
      }

      // Convertir chaque Demande en DemandeVisaCplDTO
      var dtoList = demandes.stream()
          .map(this::convertDemandeToDTO)
          .collect(Collectors.toList());

      log.info("[INFO] Conversion réussie de {} demande(s)", dtoList.size());
      return dtoList;

    } catch (Exception e) {
      log.error("[ERROR] Erreur lors de la récupération des demandes: {}", e.getMessage());
      log.error("[ERROR] StackTrace:", e);
      throw new RuntimeException("Erreur lors de la récupération des demandes: " + e.getMessage(), e);
    }
  }

    @Transactional(readOnly = true)
    public DemandeFicheDTO getDemandeFiche(Integer demandeId) {
    if (demandeId == null) {
      throw new IllegalArgumentException("Demande ID obligatoire");
    }

    Demande demande = demandeRepository.findById(demandeId)
      .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée avec l'ID: " + demandeId));

    DemandeVisaCplDTO demandeDto = convertDemandeToDTO(demande);

    List<HistoriqueStatutDTO> historiques = historiqueStatutDemandeRepository.findByDemandeId(demandeId)
      .stream()
      .sorted(Comparator.comparing(HistoriqueStatutDemande::getDateChangement,
        Comparator.nullsLast(Comparator.naturalOrder())))
      .map(h -> HistoriqueStatutDTO.builder()
        .id(h.getId())
        .statut(h.getStatutDemande() != null ? h.getStatutDemande().getLibelle() : null)
        .statutId(h.getStatutDemande() != null ? h.getStatutDemande().getId() : null)
        .commentaire(h.getCommentaire())
        .dateChangement(h.getDateChangement())
        .build())
      .collect(Collectors.toList());

    return DemandeFicheDTO.builder()
      .demande(demandeDto)
      .historiques(historiques)
      .build();
    }

  private VisaTransformable resolveVisaTransformable(Demande demande) {
    if (demande == null) {
      return null;
    }

    VisaTransformable visaTransformable = demande.getVisaTransformable();
    if (visaTransformable != null) {
      return visaTransformable;
    }

    Demandeur demandeur = demande.getDemandeur();
    if (demandeur != null && demandeur.getVisasTransformables() != null
        && !demandeur.getVisasTransformables().isEmpty()) {
      return demandeur.getVisasTransformables().get(demandeur.getVisasTransformables().size() - 1);
    }

    return null;
  }

  private Passeport resolvePasseport(Demande demande) {
    if (demande == null) {
      return null;
    }

    Passeport passeport = demande.getPasseport();
    if (passeport != null) {
      return passeport;
    }

    Demandeur demandeur = demande.getDemandeur();
    if (demandeur != null && demandeur.getPasseports() != null && !demandeur.getPasseports().isEmpty()) {
      return demandeur.getPasseports().get(demandeur.getPasseports().size() - 1);
    }

    return null;
  }

  /**
   * Convertit une entité Demande en DemandeVisaCplDTO
   * 
   * @param demande l'entité Demande à convertir
   * @return DemandeVisaCplDTO avec les données converties
   */
  private DemandeVisaCplDTO convertDemandeToDTO(Demande demande) {
    log.debug("[DEBUG] Conversion Demande {} en DemandeVisaCplDTO", demande.getId());

    try {
      // Récupérer le demandeur
      Demandeur demandeur = demande.getDemandeur();
      if (demandeur == null) {
        log.warn("[WARNING] Demandeur non trouvé pour la demande {}", demande.getId());
        throw new IllegalStateException("Demandeur null pour la demande " + demande.getId());
      }

      // Récupérer le passeport lié à la demande
      Passeport passeport = resolvePasseport(demande);

      // Récupérer le visa transformable lié à la demande
      VisaTransformable visaTransformable = resolveVisaTransformable(demande);

        // Récupérer les pièces justificatives depuis la table d'association
        List<DemandePieceJustificative> piecesLiees = demandePieceJustificativeRepository
          .findByDemandeId(demande.getId());

        List<String> pieces = piecesLiees.stream()
            .map(dpj -> dpj.getNomFichier() != null && !dpj.getNomFichier().isBlank()
              ? dpj.getNomFichier()
              : (dpj.getPieceJustificative() != null ? dpj.getPieceJustificative().getLibelle() : null))
          .filter(Objects::nonNull)
          .collect(Collectors.toList());

        // Récupérer les IDs des lignes pour le téléchargement
        List<Integer> piecesIds = piecesLiees.stream()
          .map(DemandePieceJustificative::getId)
          .collect(Collectors.toList());

        // Récupérer les IDs des pièces justificatives liées au type de visa
        List<Integer> piecesJustificativeIds = piecesLiees.stream()
          .map(dpj -> dpj.getPieceJustificative() != null ? dpj.getPieceJustificative().getId() : null)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());

      // Récupérer le dernier statut de la demande directement depuis l'historique
      String statut = null;
      Integer statutId = null;
      List<HistoriqueStatutDemande> historiques = historiqueStatutDemandeRepository.findByDemandeId(demande.getId());
      if (historiques != null && !historiques.isEmpty()) {
        HistoriqueStatutDemande dernierHistorique = historiques.stream()
            .max((h1, h2) -> h1.getDateChangement().compareTo(h2.getDateChangement()))
            .orElse(null);
        if (dernierHistorique != null && dernierHistorique.getStatutDemande() != null) {
          statut = dernierHistorique.getStatutDemande().getLibelle();
          statutId = dernierHistorique.getStatutDemande().getId();
        }
      }

      return DemandeVisaCplDTO.builder()
          // ID de la demande
          .id(demande.getId())
          // État civil
          .nom(demandeur.getNom())
          .prenom(demandeur.getPrenom())
          .genre(demandeur.getGenre() != null ? demandeur.getGenre().getLibelle() : null)
          .dateNaissance(demandeur.getDateNaissance())
          .lieuNaissance(demandeur.getLieuNaissance())
          .telephone(demandeur.getTelephone())
          .email(demandeur.getEmail())
          .adresse(demandeur.getAdresse())
          .nationalite(demandeur.getNationalite() != null ? demandeur.getNationalite().getLibelle() : null)
          .situationFamiliale(
              demandeur.getSituationFamiliale() != null ? demandeur.getSituationFamiliale().getLibelle() : null)
          // Passeport
          .numeroPasseport(passeport != null ? passeport.getNumero() : null)
          .dateDelivrance(passeport != null ? passeport.getDateDelivrance() : null)
          .dateExpiration(passeport != null ? passeport.getDateExpiration() : null)
          .paysDelivrance(passeport != null ? passeport.getPaysDelivrance() : null)
          // Visa transformable
          .referenceVisa(visaTransformable != null ? visaTransformable.getReference() : null)
          .dateEntree(visaTransformable != null ? visaTransformable.getDateEntree() : null)
          .dateFin(visaTransformable != null ? visaTransformable.getDateFin() : null)
          .lieuEntree(visaTransformable != null ? visaTransformable.getLieuEntree() : null)
          // Type de visa
          .visaType(demande.getTypeVisa() != null ? demande.getTypeVisa().getLibelle() : null)
          .visaTypeId(demande.getTypeVisa() != null ? demande.getTypeVisa().getId() : null)
          // Statut de la demande
          .statut(statut)
          .statutId(statutId)
          // Pièces justificatives
          .pieces(pieces)
          .piecesIds(piecesIds)
          .piecesJustificativeIds(piecesJustificativeIds)
          .build();

    } catch (Exception e) {
      log.error("[ERROR] Erreur lors de la conversion de la Demande {}: {}",
          demande != null ? demande.getId() : "UNKNOWN", e.getMessage());
      throw new RuntimeException("Erreur conversion: " + e.getMessage(), e);
    }
  }

  @Transactional(readOnly = true)
  public DemandeurDemandesDTO getDemandeurAvecDemandesParNumero(String numero) {
    if (numero == null || numero.trim().isEmpty()) {
      throw new IllegalArgumentException("Le numero est obligatoire");
    }

    String trimmed = numero.trim();
    boolean isPassport = trimmed.regionMatches(true, 0, "PASS", 0, 4);

    Demandeur demandeur = null;

    if (isPassport) {
      String passeportNumero = trimmed.substring(4).trim();
      if (!passeportNumero.isEmpty()) {
        char firstChar = passeportNumero.charAt(0);
        if (firstChar == '-' || firstChar == '_' || firstChar == ':') {
          passeportNumero = passeportNumero.substring(1).trim();
        }
      }

      if (passeportNumero.isEmpty()) {
        throw new IllegalArgumentException("Numero de passeport invalide");
      }

      Passeport passeport = passeportRepository.findByNumero(passeportNumero).orElse(null);
      if (passeport != null) {
        demandeur = passeport.getDemandeur();
      }
    } else {
      Integer demandeId;
      try {
        demandeId = Integer.parseInt(trimmed);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Numero de demande invalide");
      }

      Demande demande = demandeRepository.findById(demandeId).orElse(null);
      if (demande != null) {
        demandeur = demande.getDemandeur();
      }
    }

    if (demandeur == null) {
      return null;
    }

    List<DemandeVisaCplDTO> demandes = demandeRepository.findByDemandeurId(demandeur.getId()).stream()
        .map(this::convertDemandeToDTO)
        .collect(Collectors.toList());

    DemandeurInfoDTO demandeurInfo = DemandeurInfoDTO.builder()
        .id(demandeur.getId())
        .nom(demandeur.getNom())
        .prenom(demandeur.getPrenom())
        .genre(demandeur.getGenre() != null ? demandeur.getGenre().getLibelle() : null)
        .dateNaissance(demandeur.getDateNaissance())
        .lieuNaissance(demandeur.getLieuNaissance())
        .telephone(demandeur.getTelephone())
        .email(demandeur.getEmail())
        .adresse(demandeur.getAdresse())
        .nationalite(demandeur.getNationalite() != null ? demandeur.getNationalite().getLibelle() : null)
        .situationFamiliale(
            demandeur.getSituationFamiliale() != null ? demandeur.getSituationFamiliale().getLibelle() : null)
        .build();

    return DemandeurDemandesDTO.builder()
        .demandeur(demandeurInfo)
        .demandes(demandes)
        .build();
  }

  /**
   * Met à jour une demande si son statut est "En attente" (id=1)
   * 
   * @param demandeId  l'ID de la demande à mettre à jour
   * @param demandeDTO les nouvelles données
   * @return DemandeVisaCplDTO la demande mise à jour au format DTO
   * @throws RuntimeException si le statut n'est pas "En attente" ou si erreur
   */
  @Transactional
  public DemandeVisaCplDTO updateDemandeVisa(Integer demandeId, DemandeVisaSaisieDTO demandeDTO) {
    log.info("[INFO] Début de la mise à jour de la demande {}", demandeId);

    try {
      // Vérifier que la demande existe
      Demande demande = demandeRepository.findById(demandeId)
          .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée avec l'ID: " + demandeId));
      log.info("[INFO] Demande trouvée: {}", demandeId);

        // Vérifier que les champs requis sont complets avant de permettre le passage au statut 2
        validerChampsObligatoiresMiseAJour(demandeDTO);

      // Vérifier que le statut est "CREE"
      if (demande.getHistoriques() != null && !demande.getHistoriques().isEmpty()) {
        HistoriqueStatutDemande dernierHistorique = demande.getHistoriques()
            .stream()
            .max((h1, h2) -> h1.getDateChangement().compareTo(h2.getDateChangement()))
            .orElse(null);

        if (dernierHistorique != null && dernierHistorique.getStatutDemande() != null) {
          String statutActuel = dernierHistorique.getStatutDemande().getLibelle();
          if (!"CREE".equalsIgnoreCase(statutActuel)) {
            log.warn("[WARNING] Tentative de modification avec statut != CREE. Statut actuel: {}", statutActuel);
            throw new IllegalStateException("Seules les demandes avec le statut 'CREE' peuvent être modifiées");
          }
          log.info("[INFO] Statut valide pour modification (CREE)");
        }
      }

      // Récupérer et mettre à jour le Demandeur
      Demandeur demandeur = demande.getDemandeur();
      if (demandeur == null) {
        throw new IllegalStateException("Demandeur non trouvé pour la demande " + demandeId);
      }

      log.info("[INFO] Mise à jour des données du demandeur");
      demandeur.setNom(demandeDTO.getNom());
      demandeur.setPrenom(demandeDTO.getPrenom());
      demandeur.setLieuNaissance(demandeDTO.getLieuNaissance());
      demandeur.setTelephone(demandeDTO.getTelephone());
      demandeur.setEmail(demandeDTO.getEmail());
      demandeur.setAdresse(demandeDTO.getAdresse());
      demandeur.setDateNaissance(demandeDTO.getDateNaissance());

      // Mettre à jour Genre si fourni
      if (demandeDTO.getGenre() != null) {
        Genre genre = genreRepository.findById(demandeDTO.getGenre())
            .orElseThrow(() -> new IllegalArgumentException("Genre non trouvé"));
        demandeur.setGenre(genre);
      }

      // Mettre à jour Nationalité si fourni
      if (demandeDTO.getNationalite() != null) {
        Nationalite nationalite = nationaliteRepository.findById(demandeDTO.getNationalite())
            .orElseThrow(() -> new IllegalArgumentException("Nationalité non trouvée"));
        demandeur.setNationalite(nationalite);
      }

      // Mettre à jour Situation Familiale si fourni
      if (demandeDTO.getSituationFamiliale() != null) {
        SituationFamiliale situationFamiliale = situationFamilialeRepository
            .findById(demandeDTO.getSituationFamiliale())
            .orElse(null);
        demandeur.setSituationFamiliale(situationFamiliale);
      }

      demandeur = demandeurRepository.save(demandeur);
      log.info("[INFO] Demandeur mis à jour avec l'ID: {}", demandeur.getId());

      // Mettre à jour le Passeport
      Passeport passeport = resolvePasseport(demande);
      if (passeport != null) {
        log.info("[INFO] Mise à jour du passeport");
        passeport.setNumero(demandeDTO.getNumeroPasseport());
        passeport.setDateDelivrance(demandeDTO.getDateDelivrance());
        passeport.setDateExpiration(demandeDTO.getDateExpiration());
        passeport.setPaysDelivrance(demandeDTO.getPaysDelivrance());
        passeportRepository.save(passeport);
        demande.setPasseport(passeport);
        log.info("[INFO] Passeport mis à jour");
      }

      // Mettre à jour le Visa Transformable
      VisaTransformable visaTransformable = resolveVisaTransformable(demande);
      if (visaTransformable != null) {
        log.info("[INFO] Mise à jour du visa transformable");
        visaTransformable.setReference(demandeDTO.getReferenceVisa());
        visaTransformable.setDateEntree(demandeDTO.getDateEntree());
        visaTransformable.setDateFin(demandeDTO.getDateFin());
        visaTransformable.setLieuEntree(demandeDTO.getLieuEntree());
        visaTransformableRepository.save(visaTransformable);
        demande.setVisaTransformable(visaTransformable);
        log.info("[INFO] Visa transformable mis à jour");
      }

      // Mettre à jour le type de visa si fourni
      if (demandeDTO.getVisaType() != null) {
        VisaType visaType = visaTypeRepository.findById(demandeDTO.getVisaType())
            .orElseThrow(() -> new IllegalArgumentException("Type de visa non trouvé"));
        demande.setTypeVisa(visaType);
        log.info("[INFO] Type de visa mis à jour");
      }

      // Mettre à jour les pièces justificatives si fourni
      if (demandeDTO.getPieces() != null && !demandeDTO.getPieces().isEmpty()) {
        log.info("[INFO] Mise à jour des pièces justificatives");

        // Supprimer les anciennes associations
        demandePieceJustificativeRepository.deleteAll(demande.getPieceJustificatives());

        // Ajouter les nouvelles
        for (Integer pieceId : demandeDTO.getPieces()) {
          PieceJustificative pieceJustificative = pieceJustificativeRepository.findById(pieceId)
              .orElseThrow(() -> new IllegalArgumentException("Pièce justificative non trouvée"));

          if (!pieceJustificative.getTypeVisa().getId().equals(demande.getTypeVisa().getId())) {
            throw new IllegalArgumentException("Pièce justificative non valide pour le type de visa");
          }

          DemandePieceJustificative demandePiece = new DemandePieceJustificative();
          demandePiece.setDemande(demande);
          demandePiece.setPieceJustificative(pieceJustificative);
          demandePieceJustificativeRepository.save(demandePiece);
        }
        log.info("[INFO] {} pièce(s) justificative(s) mises à jour", demandeDTO.getPieces().size());
      }

      // Sauvegarder la demande
      demande = demandeRepository.saveAndFlush(demande);
      log.info("[INFO] Demande {} mise à jour avec succès", demandeId);

          StatutDemande statutModifie = getStatut("SCANNE");
        HistoriqueStatutDemande historiqueModifie = new HistoriqueStatutDemande();
        historiqueModifie.setDemande(demande);
        historiqueModifie.setStatutDemande(statutModifie);
        historiqueModifie.setCommentaire("Demande modifiée");
        historiqueStatutDemandeRepository.save(historiqueModifie);
        log.info("[INFO] Statut de la demande {} passé à 2 après modification", demandeId);

      // Convertir en DTO pour éviter les références circulaires
      return convertDemandeToDTO(demande);

    } catch (IllegalArgumentException e) {
      log.error("[ERROR] Erreur validation: {}", e.getMessage());
      throw new RuntimeException("Erreur validation: " + e.getMessage(), e);
    } catch (IllegalStateException e) {
      log.error("[ERROR] Erreur état: {}", e.getMessage());
      throw new RuntimeException("Erreur: " + e.getMessage(), e);
    } catch (Exception e) {
      log.error("[ERROR] Erreur mise à jour demande: {}", e.getMessage(), e);
      throw new RuntimeException("Erreur lors de la mise à jour: " + e.getMessage(), e);
    }
  }

  /**
   * Recherche une carte résident par sa référence
   * 
   * @param reference la référence de la carte résident
   * @return Un objet contenant les informations de la carte résident et du
   *         demandeur si trouvé, null sinon
   * @throws RuntimeException en cas d'erreur
   */
  public Object searchCarteResidentByReference(String reference) {
    log.info("Recherche d'une carte résident avec la référence: {}", reference);

    try {
      if (reference == null || reference.trim().isEmpty()) {
        log.warn("Référence vide dans la recherche");
        return null;
      }

      var carteResidentOpt = carteResidentRepository.findByReference(reference.trim());

      if (carteResidentOpt.isEmpty()) {
        log.info("Aucune carte résident trouvée avec la référence: {}", reference);
        return null;
      }

      CarteResident carteResident = carteResidentOpt.get();
      log.info("Carte résident trouvée avec ID: {}", carteResident.getId());

      Demande demande = carteResident.getDemande();
      Demandeur demandeur = demande != null ? demande.getDemandeur() : null;
      if (demandeur == null) {
        log.warn("Demandeur non trouvé pour la carte résident: {}", carteResident.getId());
        return null;
      }

      return java.util.Map.ofEntries(
          java.util.Map.entry("reference", carteResident.getReference()),
          java.util.Map.entry("nom", demandeur.getNom()),
          java.util.Map.entry("prenom", demandeur.getPrenom()),
          java.util.Map.entry("dateNaissance", demandeur.getDateNaissance()),
          java.util.Map.entry("lieuNaissance", demandeur.getLieuNaissance()),
          java.util.Map.entry("telephone", demandeur.getTelephone()),
          java.util.Map.entry("email", demandeur.getEmail()),
          java.util.Map.entry("adresse", demandeur.getAdresse()),
          java.util.Map.entry("nationalite",
              demandeur.getNationalite() != null ? demandeur.getNationalite().getLibelle() : null),
          java.util.Map.entry("genre", demandeur.getGenre() != null ? demandeur.getGenre().getLibelle() : null),
          java.util.Map.entry("situationFamiliale",
              demandeur.getSituationFamiliale() != null ? demandeur.getSituationFamiliale().getLibelle() : null),
          java.util.Map.entry("dateDebut", carteResident.getDateDebut()),
          java.util.Map.entry("dateFin", carteResident.getDateFin()),
          java.util.Map.entry("lieuEntree", carteResident.getLieuEntree()));

    } catch (Exception e) {
      log.error("Erreur lors de la recherche de la carte résident: {}", e.getMessage(), e);
      throw new RuntimeException("Erreur lors de la recherche de la carte résident: " + e.getMessage(), e);
    }
  }

  /**
   * Recherche un VISA (Visa table) par sa référence - utilisé pour transfert visa
   * 
   * @param reference la référence du visa
   * @return Un objet contenant les informations du visa et du demandeur si
   *         trouvé, null sinon
   * @throws RuntimeException en cas d'erreur
   */
  public Object searchVisaByReference(String reference) {
    log.info("Recherche d'un VISA (table visas) avec la référence: {}", reference);

    try {
      if (reference == null || reference.trim().isEmpty()) {
        log.warn("Référence vide dans la recherche");
        return null;
      }

      var visas = visaRepository.findAll()
          .stream()
          .filter(visa -> visa.getReference().equalsIgnoreCase(reference.trim()))
          .toList();

      if (visas.isEmpty()) {
        log.info("Aucun VISA trouvé avec la référence: {}", reference);
        return null;
      }

      Visa visa = visas.get(0);
      log.info("VISA trouvé avec ID: {}", visa.getId());

      Demande demande = visa.getDemande();
      Demandeur demandeur = demande != null ? demande.getDemandeur() : null;
      if (demandeur == null) {
        log.warn("Demandeur non trouvé pour le VISA: {}", visa.getId());
        return null;
      }

      return java.util.Map.ofEntries(
          java.util.Map.entry("reference", visa.getReference()),
          java.util.Map.entry("nom", demandeur.getNom()),
          java.util.Map.entry("prenom", demandeur.getPrenom()),
          java.util.Map.entry("dateNaissance", demandeur.getDateNaissance()),
          java.util.Map.entry("lieuNaissance", demandeur.getLieuNaissance()),
          java.util.Map.entry("telephone", demandeur.getTelephone()),
          java.util.Map.entry("email", demandeur.getEmail()),
          java.util.Map.entry("adresse", demandeur.getAdresse()),
          java.util.Map.entry("nationalite",
              demandeur.getNationalite() != null ? demandeur.getNationalite().getLibelle() : null),
          java.util.Map.entry("genre", demandeur.getGenre() != null ? demandeur.getGenre().getLibelle() : null),
          java.util.Map.entry("situationFamiliale",
              demandeur.getSituationFamiliale() != null ? demandeur.getSituationFamiliale().getLibelle() : null),
          java.util.Map.entry("dateDebut", visa.getDateDebut()),
          java.util.Map.entry("dateFin", visa.getDateFin()),
          java.util.Map.entry("lieuEntree", visa.getLieuEntree()));

    } catch (Exception e) {
      log.error("Erreur lors de la recherche du VISA: {}", e.getMessage(), e);
      throw new RuntimeException("Erreur lors de la recherche du VISA: " + e.getMessage(), e);
    }
  }

  /**
   * Crée une demande de transfert de visa avec un nouveau passeport
   * 
   * @param transfertSaisie les données du transfert (visa reference + new
   *                        passport data)
   * @return Demande créée
   * @throws RuntimeException en cas d'erreur
   */
  @Transactional
  public Demande enregistrerTransfertVisa(TransfertVisaSaisieDTO transfertSaisie) {
    log.info("Début enregistrement transfert visa - Référence VISA: {}", transfertSaisie.getVisaReference());

    try {
      // ========== VALIDATION DES DONNÉES ==========
      if (transfertSaisie.getVisaReference() == null || transfertSaisie.getVisaReference().trim().isEmpty()) {
        log.error("La référence du visa est manquante");
        throw new IllegalArgumentException("La référence du visa est obligatoire");
      }

      if (transfertSaisie.getNumeroPasseport() == null || transfertSaisie.getNumeroPasseport().isEmpty()) {
        log.error("Le numéro du passeport est manquant");
        throw new IllegalArgumentException("Le numéro du passeport est obligatoire");
      }

      if (transfertSaisie.getDateExpiration() != null && transfertSaisie.getDateDelivrance() != null) {
        if (transfertSaisie.getDateExpiration().isBefore(transfertSaisie.getDateDelivrance())) {
          log.error("La date d'expiration du passeport est antérieure à la date de délivrance");
          throw new IllegalArgumentException("La date d'expiration doit être postérieure à la date de délivrance");
        }
      }

      log.info("Validation réussie");

      // ========== RECHERCHE DU VISA EXISTANT ==========
      log.info("Recherche du visa avec la référence: {}", transfertSaisie.getVisaReference());
      var visas = visaRepository.findAll()
          .stream()
          .filter(visa -> visa.getReference().equalsIgnoreCase(transfertSaisie.getVisaReference().trim()))
          .toList();

      if (visas.isEmpty()) {
        log.warn("Aucun visa trouvé avec la référence: {}", transfertSaisie.getVisaReference());

        throw new ExpectedException(SANS_DONNEES_ANTERIEURS);
      }

      Visa visaExistant = visas.get(0);
      log.info("Visa trouvé avec ID: {}", visaExistant.getId());

      Demande demandeExistante = visaExistant.getDemande();
      if (demandeExistante == null) {
        log.error("Demande non trouvée pour le visa");
        throw new IllegalArgumentException("Demande associée au visa non trouvée");
      }

      Demandeur demandeurOriginal = demandeExistante.getDemandeur();
      if (demandeurOriginal == null) {
        log.error("Demandeur non trouvé pour la demande");
        throw new IllegalArgumentException("Demandeur non trouvé");
      }

      log.info("Demandeur trouvé: {} {}", demandeurOriginal.getNom(), demandeurOriginal.getPrenom());

      // ========== CRÉATION DES OBJETS POUR LE TRANSFERT ==========
      log.info("Création des objets pour le transfert visa");

      // 1. Créer un nouveau Passeport
      log.info("Création du nouveau passeport");
      Passeport nouveauPasseport = new Passeport();
      nouveauPasseport.setNumero(transfertSaisie.getNumeroPasseport());
      nouveauPasseport.setDateDelivrance(transfertSaisie.getDateDelivrance());
      nouveauPasseport.setDateExpiration(transfertSaisie.getDateExpiration());
      nouveauPasseport.setPaysDelivrance(transfertSaisie.getPaysDelivrance());
      nouveauPasseport.setDemandeur(demandeurOriginal);

      // 2. Récupérer DemandeType pour transfert (id = 2, ou id = 1 si 2 n'existe pas)
      log.info("Récupération type de demande pour transfert");
      DemandeType demandeTypeTransfert = demandeTypeRepository.findById(2)
          .orElseThrow(() -> new IllegalArgumentException("Type de demande non trouvé"));

      log.info("Type de demande trouvé: {}", demandeTypeTransfert.getId());

      // 3. Récupérer le VisaType de la demande originale
      VisaType visaType = demandeExistante.getTypeVisa();
      if (visaType == null) {
        log.error("Type de visa non trouvé dans la demande originale");
        throw new IllegalArgumentException("Type de visa non trouvé");
      }

      log.info("Objets créés avec succès (sans persistence)");

      // ========== PERSISTENCE DES OBJETS ==========
      log.info("Début de la persistence des objets");

      // Sauvegarder le nouveau Passeport
      log.info("Sauvegarde du nouveau passeport");
      nouveauPasseport = passeportRepository.save(nouveauPasseport);
      log.info("Nouveau passeport sauvegardé avec l'ID: {}", nouveauPasseport.getId());

      // Créer une nouvelle Demande de transfert
      log.info("Sauvegarde de la demande de transfert");
      Demande demandeTransfert = new Demande();
      demandeTransfert.setDemandeur(demandeurOriginal);
      demandeTransfert.setPasseport(nouveauPasseport);
      demandeTransfert.setTypeVisa(visaType);
      demandeTransfert.setTypeDemande(demandeTypeTransfert);
      demandeTransfert = demandeRepository.saveAndFlush(demandeTransfert);
      log.info("Demande de transfert sauvegardée avec l'ID: {}", demandeTransfert.getId());

      // Créer l'historique avec statut PENDING (id=1)
      log.info("Création de l'historique statut initial");
      StatutDemande statutInitial = statutDemandeRepository.findById(1)
          .orElseThrow(() -> new IllegalArgumentException("Statut initial (id=1) non trouvé"));

      HistoriqueStatutDemande historique = new HistoriqueStatutDemande();
      historique.setDemande(demandeTransfert);
      historique.setStatutDemande(statutInitial);
      historique.setCommentaire("Demande de transfert de visa créée avec nouveau passeport");
      historiqueStatutDemandeRepository.save(historique);
      log.info("Historique statut créé avec succès");

      // Copier les pièces justificatives de la demande originale
      if (demandeExistante.getPieceJustificatives() != null && !demandeExistante.getPieceJustificatives().isEmpty()) {
        log.info("Copie des {} pièces justificatives", demandeExistante.getPieceJustificatives().size());
        for (DemandePieceJustificative dpjOriginal : demandeExistante.getPieceJustificatives()) {
          DemandePieceJustificative dpjNew = new DemandePieceJustificative();
          dpjNew.setDemande(demandeTransfert);
          dpjNew.setPieceJustificative(dpjOriginal.getPieceJustificative());
          demandePieceJustificativeRepository.save(dpjNew);
          log.info("Pièce justificative {} copiée", dpjOriginal.getPieceJustificative().getId());
        }
      }

      log.info("Persistence réussie - Transfert de visa enregistré avec succès");
      return demandeTransfert;

    } catch (IllegalArgumentException e) {
      log.error("Erreur validation: {}", e.getMessage());
      throw new RuntimeException("Erreur validation: " + e.getMessage(), e);

    } catch (Exception e) {
      log.error("Erreur enregistrement transfert visa: {}", e.getMessage(), e);
      throw new RuntimeException("Erreur lors de l'enregistrement du transfert: " + e.getMessage(), e);
    }
  }

  /**
   * Enregistre une nouvelle demande de visa avec upload de fichiers
   * 
   * @param demandeSaisie les données saisies du formulaire
   * @param files les fichiers uploadés pour les pièces justificatives
   * @return Demande créée
   * @throws RuntimeException en cas d'erreur
   */
  @Transactional
  public Demande enregistrerDemandeVisaAvecFichiers(DemandeVisaSaisieDTO demandeSaisie, Map<String, MultipartFile> files) {
    log.info("Début enregistrement demande visa avec fichiers - Demandeur: {} {}", 
        demandeSaisie.getNom(), demandeSaisie.getPrenom());

    try {
      // 1. Enregistrer la demande sans fichiers d'abord
      Demande demande = enregistrerDemandeVisa(demandeSaisie);
      log.info("Demande enregistrée avec l'ID: {}", demande.getId());

      // Vérifier que toutes les pièces obligatoires du type de visa ont bien un fichier
      validerPiecesObligatoires(demande, files);

      // 2. Sauvegarder les fichiers et créer/mettre à jour les DemandePieceJustificative
      sauvegarderFichiersPieces(demande, files);

      log.info("Enregistrement demande visa avec fichiers terminé avec succès");
      return demande;

    } catch (Exception e) {
      log.error("Erreur lors de l'enregistrement de la demande avec fichiers: {}", e.getMessage(), e);
      throw new RuntimeException("Erreur lors de l'enregistrement: " + e.getMessage(), e);
    }
  }

  @Transactional
  public DemandeVisaCplDTO updateDemandeVisaAvecFichiers(Integer demandeId, DemandeVisaSaisieDTO demandeDTO,
      Map<String, MultipartFile> files) {
    updateDemandeVisa(demandeId, demandeDTO);
    Demande demande = demandeRepository.findById(demandeId)
        .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée avec l'ID: " + demandeId));

    // Si une pièce obligatoire manque, l'exception déclenche un rollback de la mise à jour
    validerPiecesObligatoires(demande, files);
    sauvegarderFichiersPieces(demande, files);
    return convertDemandeToDTO(demande);
  }

  @Transactional
  public DemandeVisaCplDTO passerDemandeEnScanne(Integer demandeId) {
    Demande demande = demandeRepository.findById(demandeId)
        .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée avec l'ID: " + demandeId));

    List<HistoriqueStatutDemande> historiques = historiqueStatutDemandeRepository.findByDemandeId(demandeId);
    HistoriqueStatutDemande dernierHistorique = historiques.stream()
        .max((h1, h2) -> h1.getDateChangement().compareTo(h2.getDateChangement()))
        .orElse(null);

    if (dernierHistorique == null || dernierHistorique.getStatutDemande() == null
        || !"CREE".equalsIgnoreCase(dernierHistorique.getStatutDemande().getLibelle())) {
      throw new IllegalStateException("Seules les demandes au statut 'CREE' peuvent passer à 'SCANNE'");
    }

    StatutDemande statutScanne = getStatut("SCANNE");
    HistoriqueStatutDemande historiqueScanne = new HistoriqueStatutDemande();
    historiqueScanne.setDemande(demande);
    historiqueScanne.setStatutDemande(statutScanne);
    historiqueScanne.setCommentaire("Demande passée en SCANNE");
    historiqueStatutDemandeRepository.save(historiqueScanne);

    log.info("[INFO] Demande {} passée au statut SCANNE", demandeId);
    return convertDemandeToDTO(demande);
  }

  /**
   * Récupère les informations d'un fichier de pièce justificative
   * @param pieceId l'ID de la pièce justificative
   * @return les informations du fichier (chemin, nom)
   */
  public DemandePieceJustificative getPieceJustificativeInfo(Integer pieceId) {
    return demandePieceJustificativeRepository.findById(pieceId)
        .orElse(null);
  }
}
