package com.itu.visabackoffice.dto;

import com.itu.visabackoffice.models.Genre;
import com.itu.visabackoffice.models.SituationFamiliale;
import com.itu.visabackoffice.models.VisaType;
import com.itu.visabackoffice.models.PieceJustificative;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonneesDemandeVisaDTO {
    
    private List<Genre> genres;
    
    private List<SituationFamiliale> situationsFamiliales;
    
    private List<VisaType> typesVisa;
    
    private List<PieceJustificative> piecesJustificatives;
}
