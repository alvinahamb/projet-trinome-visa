package com.itu.visabackoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransfertVisaSaisieDTO {
    
    // Visa reference to search
    private String visaReference;
    
    // New passport data
    private String numeroPasseport;
    private LocalDate dateDelivrance;
    private LocalDate dateExpiration;
    private String paysDelivrance;
}
