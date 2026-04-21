package com.itu.visabackoffice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "types_demandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_demande")
    private Integer id;

    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
