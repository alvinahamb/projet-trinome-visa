package com.itu.visabackoffice.controller;

import com.itu.visabackoffice.dto.ApiResponse;
import com.itu.visabackoffice.services.DemandeVisaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carte-resident")
@Slf4j
public class CarteResidentRestController {

    @Autowired
    private DemandeVisaService demandeVisaService;

    // ================== GET: LECTURES ==================

    /**
     * Endpoint pour rechercher une carte résident par sa référence - utilisé pour la demande de duplicata
     * @param reference la référence de la carte résident
     * @return ApiResponse avec les informations de la carte résident et du demandeur si trouvé
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Object>> searchCarteResidentByReference(@RequestParam String reference) {
        try {
            log.info("Recherche carte résident par référence: {}", reference);

            Object resultat = demandeVisaService.searchCarteResidentByReference(reference);

            if (resultat != null) {
                ApiResponse<Object> response = ApiResponse.success(
                        resultat,
                        "Carte résident trouvée avec succès"
                );
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Object> response = ApiResponse.success(
                        null,
                        "Aucune carte résident trouvée avec cette référence"
                );
                return ResponseEntity.ok(response);
            }

        } catch (RuntimeException e) {
            log.error("Erreur métier recherche carte résident: {}", e.getMessage());

            ApiResponse<Object> response = ApiResponse.error(
                    400,
                    e.getMessage(),
                    "Erreur lors de la recherche de la carte résident"
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            log.error("Erreur inattendue recherche carte résident: {}", e.getMessage(), e);

            ApiResponse<Object> response = ApiResponse.error(
                    500,
                    "Erreur serveur interne",
                    "Une erreur inattendue s'est produite"
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
