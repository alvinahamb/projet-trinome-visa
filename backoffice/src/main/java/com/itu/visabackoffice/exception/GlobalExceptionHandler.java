package com.itu.visabackoffice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions pour les endpoints REST
 * Inclut la gestion des erreurs CORS et des erreurs courantes
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * Gère les requêtes vers des endpoints inexistants
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(NoHandlerFoundException ex) {
    log.warn("Endpoint non trouvé: {} {}", ex.getHttpMethod(), ex.getRequestURL());
    
    Map<String, Object> response = new HashMap<>();
    response.put("status", "error");
    response.put("code", HttpStatus.NOT_FOUND.value());
    response.put("message", "Endpoint non trouvé: " + ex.getRequestURL());
    response.put("timestamp", System.currentTimeMillis());
    
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  /**
   * Gère les exceptions générales
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
    log.error("Erreur serveur", ex);
    
    Map<String, Object> response = new HashMap<>();
    response.put("status", "error");
    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.put("message", ex.getMessage() != null ? ex.getMessage() : "Erreur serveur interne");
    response.put("timestamp", System.currentTimeMillis());
    
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  /**
   * Gère les erreurs de validation et arguments invalides
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
    log.warn("Argument invalide: {}", ex.getMessage());
    
    Map<String, Object> response = new HashMap<>();
    response.put("status", "error");
    response.put("code", HttpStatus.BAD_REQUEST.value());
    response.put("message", ex.getMessage());
    response.put("timestamp", System.currentTimeMillis());
    
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}
