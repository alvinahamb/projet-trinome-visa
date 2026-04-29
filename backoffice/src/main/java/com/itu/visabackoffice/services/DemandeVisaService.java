package com.itu.visabackoffice.services;

import com.itu.visabackoffice.dto.DonneesDemandeVisaDTO;
import com.itu.visabackoffice.dto.DemandeVisaSaisieDTO;
import com.itu.visabackoffice.dto.DemandeVisaCplDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DemandeVisaService {
    
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
    
    /**
     * Récupère toutes les données nécessaires pour une demande de visa
     * @return DonneesDemandeVisaDTO contenant les genres, situations familiales, types de visa et pièces justificatives
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

    /**
     * Enregistre une nouvelle demande de visa avec tous les objets associés
     * @param demandeSaisie les données saisies du formulaire
     * @return Demande créée
     * @throws RuntimeException en cas d'erreur
     */
    public Demande enregistrerDemandeVisa(DemandeVisaSaisieDTO demandeSaisie) {
        log.info("Début enregistrement demande visa - Demandeur: {} {}", demandeSaisie.getNom(), demandeSaisie.getPrenom());
        
        try {
            // ========== VALIDATION DES DONNÉES ==========
            log.info("Validation des données de la demande");
            
            if (demandeSaisie.getNom() == null || demandeSaisie.getNom().isEmpty()) {
                log.error("Le nom est manquant");
                throw new IllegalArgumentException("Le nom est obligatoire");
            }
            
            if (demandeSaisie.getPrenom() == null || demandeSaisie.getPrenom().isEmpty()) {
                log.error("Le prénom est manquant");
                throw new IllegalArgumentException("Le prénom est obligatoire");
            }
            
            if (demandeSaisie.getGenre() == null) {
                log.error("Le genre est manquant");
                throw new IllegalArgumentException("Le genre est obligatoire");
            }
            
            if (demandeSaisie.getDateNaissance() == null) {
                log.error("La date de naissance est manquante");
                throw new IllegalArgumentException("La date de naissance est obligatoire");
            }
            
            if (demandeSaisie.getVisaType() == null) {
                log.error("Le type de visa est manquant");
                throw new IllegalArgumentException("Le type de visa est obligatoire");
            }
            
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
                    .orElseThrow(() -> new IllegalArgumentException("Nationalité non trouvée avec l'ID: " + demandeSaisie.getNationalite()));
            log.info("Nationalité trouvée: {}", nationalite.getId());
            
            // Récupérer SituationFamiliale (optionnelle)
            SituationFamiliale situationFamiliale = situationFamilialeRepository.findById(demandeSaisie.getSituationFamiliale())
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
                    .orElseThrow(() -> new IllegalArgumentException("Type de visa non trouvé avec l'ID: " + demandeSaisie.getVisaType()));
            log.info("Type de visa trouvé: {}", visaType.getId());
            
            // ========== HANDLING DUPLICATA ==========
            boolean isDuplicata = demandeSaisie.getIsDuplicata() != null && demandeSaisie.getIsDuplicata();
            
            if (isDuplicata) {
                log.info("Création de 2 demandes (original completed + duplicata pending)");
                
                // ========== PREMIÈRE DEMANDE (ORIGINAL - COMPLÉTÉE) ==========
                log.info("Sauvegarde de la première demande (original completed)");
                Demande demandeOriginal = new Demande();
                demandeOriginal.setDemandeur(demandeur);
                demandeOriginal.setTypeVisa(visaType);
                demandeOriginal.setTypeDemande(demandeType); // demande_type = 1
                demandeOriginal = demandeRepository.save(demandeOriginal);
                log.info("Demande originale sauvegardée avec l'ID: {}", demandeOriginal.getId());
                
                // Créer l'historique avec statut COMPLETED (id=3)
                StatutDemande statutCompleted = statutDemandeRepository.findById(3)
                        .orElseThrow(() -> new IllegalArgumentException("Statut COMPLETED (id=3) non trouvé"));
                HistoriqueStatutDemande historiqueOriginal = new HistoriqueStatutDemande();
                historiqueOriginal.setDemande(demandeOriginal);
                historiqueOriginal.setStatutDemande(statutCompleted);
                historiqueOriginal.setCommentaire("Visa original - déjà traité");
                historiqueStatutDemandeRepository.save(historiqueOriginal);
                log.info("Historique statut COMPLETED créé pour demande originale");
                
                // Ajouter les pièces justificatives à la demande originale
                if (demandeSaisie.getPieces() != null && !demandeSaisie.getPieces().isEmpty()) {
                    log.info("Sauvegarde des {} pièces justificatives pour demande originale", demandeSaisie.getPieces().size());
                    for (Integer pieceId : demandeSaisie.getPieces()) {
                        PieceJustificative pieceJustificative = pieceJustificativeRepository.findById(pieceId)
                                .orElseThrow(() -> new IllegalArgumentException("Pièce justificative non trouvée avec l'ID: " + pieceId));
                        
                        if (!pieceJustificative.getTypeVisa().getId().equals(visaType.getId())) {
                            log.warn("Pièce justificative {} n'est pas associée au type de visa {}", pieceId, visaType.getId());
                            throw new IllegalArgumentException("La pièce justificative (id: " + pieceId + ") n'est pas valide pour le type de visa sélectionné");
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
                demandeDuplicata.setTypeVisa(visaType);
                demandeDuplicata.setTypeDemande(demandeTypeDuplicata); // demande_type = 3 (DUPLICATA)
                demande = demandeRepository.save(demandeDuplicata);
                log.info("Demande duplicata sauvegardée avec l'ID: {}", demande.getId());
                
                // Créer l'historique avec statut PENDING (id=1)
                StatutDemande statutInitial = statutDemandeRepository.findById(1)
                        .orElseThrow(() -> new IllegalArgumentException("Statut initial (id=1) non trouvé"));
                HistoriqueStatutDemande historiqueDuplicata = new HistoriqueStatutDemande();
                historiqueDuplicata.setDemande(demande);
                historiqueDuplicata.setStatutDemande(statutInitial);
                historiqueDuplicata.setCommentaire("Demande de duplicata créée");
                historiqueStatutDemandeRepository.save(historiqueDuplicata);
                log.info("Historique statut PENDING créé pour demande duplicata");
                
                // Ajouter les pièces justificatives à la demande duplicata
                if (demandeSaisie.getPieces() != null && !demandeSaisie.getPieces().isEmpty()) {
                    log.info("Sauvegarde des {} pièces justificatives pour demande duplicata", demandeSaisie.getPieces().size());
                    for (Integer pieceId : demandeSaisie.getPieces()) {
                        PieceJustificative pieceJustificative = pieceJustificativeRepository.findById(pieceId)
                                .orElseThrow(() -> new IllegalArgumentException("Pièce justificative non trouvée avec l'ID: " + pieceId));
                        
                        if (!pieceJustificative.getTypeVisa().getId().equals(visaType.getId())) {
                            log.warn("Pièce justificative {} n'est pas associée au type de visa {}", pieceId, visaType.getId());
                            throw new IllegalArgumentException("La pièce justificative (id: " + pieceId + ") n'est pas valide pour le type de visa sélectionné");
                        }
                        
                        DemandePieceJustificative demandePiece = new DemandePieceJustificative();
                        demandePiece.setDemande(demande);
                        demandePiece.setPieceJustificative(pieceJustificative);
                        demandePieceJustificativeRepository.save(demandePiece);
                        log.info("Pièce justificative {} associée à la demande duplicata", pieceId);
                    }
                }
                
                log.info("Persistence réussie - Duplicata enregistré avec succès (2 demandes créées)");
                return demande; // Retourner la demande duplicata (la plus récente)
                
            } else {
                // ========== CAS NORMAL (SANS DUPLICATA) ==========
                log.info("Sauvegarde Demande (cas normal)");
                demande.setDemandeur(demandeur);
                demande.setTypeVisa(visaType);
                demande.setTypeDemande(demandeType);
                demande = demandeRepository.save(demande);
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
                
                // Sauvegarder les DemandePieceJustificative
                if (demandeSaisie.getPieces() != null && !demandeSaisie.getPieces().isEmpty()) {
                    log.info("Sauvegarde des {} pièces justificatives", demandeSaisie.getPieces().size());
                    for (Integer pieceId : demandeSaisie.getPieces()) {
                        PieceJustificative pieceJustificative = pieceJustificativeRepository.findById(pieceId)
                                .orElseThrow(() -> new IllegalArgumentException("Pièce justificative non trouvée avec l'ID: " + pieceId));
                        
                        // Vérifier que la pièce est bien associée au type de visa demandé
                        if (!pieceJustificative.getTypeVisa().getId().equals(visaType.getId())) {
                            log.warn("Pièce justificative {} n'est pas associée au type de visa {}", pieceId, visaType.getId());
                            throw new IllegalArgumentException("La pièce justificative (id: " + pieceId + ") n'est pas valide pour le type de visa sélectionné");
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
                return demande;
            }
            
        } catch (IllegalArgumentException e) {
            log.error("Erreur validation: {}", e.getMessage());
            throw new RuntimeException("Erreur validation: " + e.getMessage(), e);
            
        } catch (Exception e) {
            log.error("Erreur enregistrement demande: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de l'enregistrement: " + e.getMessage(), e);
        }
    }

    /**
     * Récupère la liste de toutes les demandes au format complet pour affichage
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

    /**
     * Convertit une entité Demande en DemandeVisaCplDTO
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
            
            // Récupérer le passeport (dernier)
            Passeport passeport = null;
            if (demandeur.getPasseports() != null && !demandeur.getPasseports().isEmpty()) {
                passeport = demandeur.getPasseports().get(demandeur.getPasseports().size() - 1);
            }
            
            // Récupérer le visa transformable (dernier)
            VisaTransformable visaTransformable = null;
            if (demandeur.getVisasTransformables() != null && !demandeur.getVisasTransformables().isEmpty()) {
                visaTransformable = demandeur.getVisasTransformables().get(demandeur.getVisasTransformables().size() - 1);
            }
            
            // Récupérer les pièces justificatives
            List<String> pieces = demande.getPieceJustificatives() != null
                    ? demande.getPieceJustificatives().stream()
                    .map(dpj -> dpj.getPieceJustificative().getLibelle())
                    .collect(Collectors.toList())
                    : List.of();
            
            // Récupérer le dernier statut de la demande
            String statut = null;
            if (demande.getHistoriques() != null && !demande.getHistoriques().isEmpty()) {
                // Récupérer le dernier historique (le plus récent)
                HistoriqueStatutDemande dernierHistorique = demande.getHistoriques()
                        .stream()
                        .max((h1, h2) -> h1.getDateChangement().compareTo(h2.getDateChangement()))
                        .orElse(null);
                if (dernierHistorique != null && dernierHistorique.getStatutDemande() != null) {
                    statut = dernierHistorique.getStatutDemande().getLibelle();
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
                    .situationFamiliale(demandeur.getSituationFamiliale() != null ? demandeur.getSituationFamiliale().getLibelle() : null)
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
                    // Statut de la demande
                    .statut(statut)
                    // Pièces justificatives
                    .pieces(pieces)
                    .build();
                    
        } catch (Exception e) {
            log.error("[ERROR] Erreur lors de la conversion de la Demande {}: {}", 
                    demande != null ? demande.getId() : "UNKNOWN", e.getMessage());
            throw new RuntimeException("Erreur conversion: " + e.getMessage(), e);
        }
    }

    /**
     * Met à jour une demande si son statut est "En attente" (id=1)
     * @param demandeId l'ID de la demande à mettre à jour
     * @param demandeDTO les nouvelles données
     * @return DemandeVisaCplDTO la demande mise à jour au format DTO
     * @throws RuntimeException si le statut n'est pas "En attente" ou si erreur
     */
    public DemandeVisaCplDTO updateDemandeVisa(Integer demandeId, DemandeVisaSaisieDTO demandeDTO) {
        log.info("[INFO] Début de la mise à jour de la demande {}", demandeId);
        
        try {
            // Vérifier que la demande existe
            Demande demande = demandeRepository.findById(demandeId)
                    .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée avec l'ID: " + demandeId));
            log.info("[INFO] Demande trouvée: {}", demandeId);
            
            // Vérifier que le statut est "En attente" (id = 1)
            if (demande.getHistoriques() != null && !demande.getHistoriques().isEmpty()) {
                HistoriqueStatutDemande dernierHistorique = demande.getHistoriques()
                        .stream()
                        .max((h1, h2) -> h1.getDateChangement().compareTo(h2.getDateChangement()))
                        .orElse(null);
                
                if (dernierHistorique != null && dernierHistorique.getStatutDemande() != null) {
                    Integer statutId = dernierHistorique.getStatutDemande().getId();
                    if (!statutId.equals(1)) {
                        log.warn("[WARNING] Tentative de modification avec statut != 1. Statut actuel: {}", statutId);
                        throw new IllegalStateException("Seules les demandes avec le statut 'En attente' peuvent être modifiées");
                    }
                    log.info("[INFO] Statut valide pour modification (id=1)");
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
                SituationFamiliale situationFamiliale = situationFamilialeRepository.findById(demandeDTO.getSituationFamiliale())
                        .orElse(null);
                demandeur.setSituationFamiliale(situationFamiliale);
            }
            
            demandeur = demandeurRepository.save(demandeur);
            log.info("[INFO] Demandeur mis à jour avec l'ID: {}", demandeur.getId());
            
            // Mettre à jour le Passeport
            if (!demandeur.getPasseports().isEmpty()) {
                Passeport passeport = demandeur.getPasseports().get(demandeur.getPasseports().size() - 1);
                log.info("[INFO] Mise à jour du passeport");
                passeport.setNumero(demandeDTO.getNumeroPasseport());
                passeport.setDateDelivrance(demandeDTO.getDateDelivrance());
                passeport.setDateExpiration(demandeDTO.getDateExpiration());
                passeport.setPaysDelivrance(demandeDTO.getPaysDelivrance());
                passeportRepository.save(passeport);
                log.info("[INFO] Passeport mis à jour");
            }
            
            // Mettre à jour le Visa Transformable
            if (!demandeur.getVisasTransformables().isEmpty()) {
                VisaTransformable visaTransformable = demandeur.getVisasTransformables().get(demandeur.getVisasTransformables().size() - 1);
                log.info("[INFO] Mise à jour du visa transformable");
                visaTransformable.setReference(demandeDTO.getReferenceVisa());
                visaTransformable.setDateEntree(demandeDTO.getDateEntree());
                visaTransformable.setDateFin(demandeDTO.getDateFin());
                visaTransformable.setLieuEntree(demandeDTO.getLieuEntree());
                visaTransformableRepository.save(visaTransformable);
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
            demande = demandeRepository.save(demande);
            log.info("[INFO] Demande {} mise à jour avec succès", demandeId);
            
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
     * Recherche un VISA transformable par sa référence
     * @param reference la référence du visa
     * @return Un objet contenant les informations du visa et du demandeur si trouvé, null sinon
     * @throws RuntimeException en cas d'erreur
     */
    public Object searchVisaByReference(String reference) {
        log.info("Recherche d'un VISA avec la référence: {}", reference);
        
        try {
            if (reference == null || reference.trim().isEmpty()) {
                log.warn("Référence vide dans la recherche");
                return null;
            }
            
            // Rechercher le visa transformable
            var visasTransformables = visaTransformableRepository.findAll()
                    .stream()
                    .filter(visa -> visa.getReference().equalsIgnoreCase(reference.trim()))
                    .toList();
            
            if (visasTransformables.isEmpty()) {
                log.info("Aucun VISA trouvé avec la référence: {}", reference);
                return null;
            }
            
            VisaTransformable visa = visasTransformables.get(0);
            log.info("VISA trouvé avec ID: {}", visa.getId());
            
            // Récupérer le demandeur associé
            Demandeur demandeur = visa.getDemandeur();
            if (demandeur == null) {
                log.warn("Demandeur non trouvé pour le VISA: {}", visa.getId());
                return null;
            }
            
            // Construire une réponse avec les informations du visa et du demandeur
            return java.util.Map.ofEntries(
                    java.util.Map.entry("referenceVisa", visa.getReference()),
                    java.util.Map.entry("nom", demandeur.getNom()),
                    java.util.Map.entry("prenom", demandeur.getPrenom()),
                    java.util.Map.entry("dateNaissance", demandeur.getDateNaissance()),
                    java.util.Map.entry("lieuNaissance", demandeur.getLieuNaissance()),
                    java.util.Map.entry("telephone", demandeur.getTelephone()),
                    java.util.Map.entry("email", demandeur.getEmail()),
                    java.util.Map.entry("adresse", demandeur.getAdresse()),
                    java.util.Map.entry("nationalite", demandeur.getNationalite() != null ? demandeur.getNationalite().getLibelle() : null),
                    java.util.Map.entry("genre", demandeur.getGenre() != null ? demandeur.getGenre().getLibelle() : null),
                    java.util.Map.entry("situationFamiliale", demandeur.getSituationFamiliale() != null ? demandeur.getSituationFamiliale().getLibelle() : null),
                    java.util.Map.entry("visaType", visa.getId()),
                    java.util.Map.entry("dateEntree", visa.getDateEntree()),
                    java.util.Map.entry("dateFin", visa.getDateFin()),
                    java.util.Map.entry("lieuEntree", visa.getLieuEntree())
            );
            
        } catch (Exception e) {
            log.error("Erreur lors de la recherche du VISA: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la recherche du VISA: " + e.getMessage(), e);
        }
    }

    /**
     * Recherche une carte résident par sa référence
     * @param reference la référence de la carte résident
     * @return Un objet contenant les informations de la carte résident et du demandeur si trouvé, null sinon
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
                    java.util.Map.entry("nationalite", demandeur.getNationalite() != null ? demandeur.getNationalite().getLibelle() : null),
                    java.util.Map.entry("genre", demandeur.getGenre() != null ? demandeur.getGenre().getLibelle() : null),
                    java.util.Map.entry("situationFamiliale", demandeur.getSituationFamiliale() != null ? demandeur.getSituationFamiliale().getLibelle() : null),
                    java.util.Map.entry("dateDebut", carteResident.getDateDebut()),
                    java.util.Map.entry("dateFin", carteResident.getDateFin()),
                    java.util.Map.entry("lieuEntree", carteResident.getLieuEntree())
            );

        } catch (Exception e) {
            log.error("Erreur lors de la recherche de la carte résident: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la recherche de la carte résident: " + e.getMessage(), e);
        }
    }
}