package com.itu.visabackoffice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "types_visas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeVisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_visa")
    private Integer id;

    @Column(name = "libelle", nullable = false, length = 100)
    private String libelle;
}
