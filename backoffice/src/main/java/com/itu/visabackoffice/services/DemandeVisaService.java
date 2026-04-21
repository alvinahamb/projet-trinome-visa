import com.itu.visabackoffice.dto.DonneesDemandeVisaDTO;
import com.itu.visabackoffice.repositories.GenreRepository;
import com.itu.visabackoffice.repositories.SituationFamilialeRepository;
import com.itu.visabackoffice.repositories.VisaTypeRepository;
import com.itu.visabackoffice.repositories.PieceJustificativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;

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
            
            log.info("[INFO] Récupération de toutes les pièces justificatives");
            var piecesJustificatives = pieceJustificativeRepository.findAll();
            log.info("[INFO] {} pièce(s) justificative(s) récupérée(s)", piecesJustificatives.size());
            
            DonneesDemandeVisaDTO donnees = DonneesDemandeVisaDTO.builder()
                    .genres(genres)
                    .situationsFamiliales(situationsFamiliales)
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
}