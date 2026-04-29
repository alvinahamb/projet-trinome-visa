package com.itu.visabackoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

  @GetMapping("/")
  public String home() {
    return "home";
  }

  @GetMapping("/visa-saisie")
  public String demandeVisaSaisie() {
    return "demande-visa-saisie";
  }

  @GetMapping("/demande-visa-saisie")
  public String demandeVisaSaisieAlias() {
    return "demande-visa-saisie";
  }

  @GetMapping("/visa-list")
  public String demandeVisaList() {
    return "demande-visa-list";
  }

  @GetMapping("/duplicata")
  public String duplicata() {
    return "duplicata";
  }

  @GetMapping("/duplicata-saisie")
  public String duplicataSaisie() {
    return "duplicata-saisie";
  }
}
