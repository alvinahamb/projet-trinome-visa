package com.itu.visabackoffice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nationalites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nationalite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nationalite")
    private Integer id;

    @Column(name = "libelle", nullable = false, length = 100)
    private String libelle;
}
