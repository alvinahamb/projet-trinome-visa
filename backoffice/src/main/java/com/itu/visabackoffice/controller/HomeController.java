package com.itu.visabackoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

  // ================== HOME ==================

  @GetMapping("/")
  public String home() {
    return "home";
  }

  // ================== DEMANDE VISA SAISIE ==================

  @GetMapping("/visa-saisie")
  public String demandeVisaSaisie() {
    return "demande-visa-saisie";
  }

  @GetMapping("/demande-visa-saisie")
  public String demandeVisaSaisieAlias() {
    return "demande-visa-saisie";
  }

  // ================== DEMANDE VISA LIST ==================

  @GetMapping("/visa-list")
  public String demandeVisaList() {
    return "demande-visa-list";
  }

  // ================== DUPLICATA SAISIE ==================

  @GetMapping("/duplicata")
  public String duplicata() {
    return "duplicata";
  }

  @GetMapping("/duplicata-saisie")
  public String duplicataSaisie() {
    return "duplicata-saisie";
  }

  // ================== TRANSFERT VISA SAISIE ==================

  @GetMapping("/transfert-visa-saisie")
  public String transfertVisaSaisie() {
    return "transfer-visa-saisie";
  }
}
