package com.itu.visabackoffice.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class QrCodeGeneratorService {

  @Value("${qr.code.path:src/main/resources/qr-codes}")
  private String qrCodePath;

  /**
   * Génère un code QR pour une URL donnée et le sauvegarde dans le dossier ressources
   * @param url l'URL à encoder dans le QR code
   * @param demandeId l'ID de la demande (utilisé pour le nom du fichier)
   * @return Map contenant:
   *   - "qrLien": l'URL relative du QR code pour accès web
   *   - "qrChemin": le chemin absolu du fichier QR généré
   * @throws Exception en cas d'erreur lors de la génération
   */
  public Map<String, String> genererQrCode(String url, Integer demandeId) throws Exception {
    try {
      log.info("Génération du QR code pour URL: {}", url);

      // Créer le dossier s'il n'existe pas
      Path dirPath = Paths.get(qrCodePath);
      if (!Files.exists(dirPath)) {
        Files.createDirectories(dirPath);
        log.info("Dossier QR code créé: {}", qrCodePath);
      }

      // Nom du fichier
      String filename = "qr_demande_" + demandeId + ".png";
      Path filePath = dirPath.resolve(filename);

      // Paramètres du code QR
      int width = 300;
      int height = 300;

      // Générer la matrice du code QR
      MultiFormatWriter writer = new MultiFormatWriter();
      BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height);

      // Écrire le fichier PNG
      MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);
      log.info("QR code généré et sauvegardé: {}", filePath);

      // Préparer les résultats
      Map<String, String> result = new HashMap<>();
      result.put("qrLien", "/qr-codes/" + filename);
      result.put("qrChemin", filePath.toString());

      log.info("QR code link: {}", result.get("qrLien"));
      log.info("QR code path: {}", result.get("qrChemin"));

      return result;

    } catch (WriterException e) {
      log.error("Erreur lors de l'écriture du QR code: {}", e.getMessage());
      throw new RuntimeException("Erreur lors de la génération du QR code: " + e.getMessage(), e);
    } catch (IOException e) {
      log.error("Erreur IO lors de la génération du QR code: {}", e.getMessage());
      throw new RuntimeException("Erreur IO lors de la génération du QR code: " + e.getMessage(), e);
    }
  }
}
