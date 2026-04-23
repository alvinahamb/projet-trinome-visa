package com.itu.visabackoffice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "situations_familiales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SituationFamiliale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_situation_familiale")
    private Integer id;

    @Column(name = "libelle", nullable = false, length = 100)
    private String libelle;
}
