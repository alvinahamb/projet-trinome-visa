-- =============================================================
-- V1__init.sql - Schéma ordonné (logique + colonnes)
-- =============================================================

-- =========================
-- TABLES DE RÉFÉRENCE (général)
-- =========================

CREATE TABLE situations_familiales (
    id_situation_familiale   SERIAL PRIMARY KEY,
    libelle                  VARCHAR(100) NOT NULL
);

CREATE TABLE nationalites (
    id_nationalite           SERIAL PRIMARY KEY,
    libelle                  VARCHAR(100) NOT NULL
);

CREATE TABLE genres (
    id_genre                 SERIAL PRIMARY KEY,
    libelle                  VARCHAR(50) NOT NULL
);

CREATE TABLE types_visas (
    id_type_visa             SERIAL PRIMARY KEY,
    libelle                  VARCHAR(100) NOT NULL
);

CREATE TABLE types_demandes (
    id_type_demande          SERIAL PRIMARY KEY,
    code                     VARCHAR(50) NOT NULL UNIQUE,
    description              TEXT
);

CREATE TABLE statuts_demandes (
    id_statut_demande        SERIAL PRIMARY KEY,
    libelle                  VARCHAR(100) NOT NULL
);

CREATE TABLE pieces_justificatives (
    id_piece_justificative   SERIAL PRIMARY KEY,
    libelle                  VARCHAR(150) NOT NULL
);

-- =========================
-- ENTITÉS PRINCIPALES
-- =========================

CREATE TABLE demandeurs (
    id_demandeur             SERIAL PRIMARY KEY,

    date_naissance           DATE NOT NULL,
    date_creation            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    nationalite_id           INT NOT NULL,
    genre_id                 INT NOT NULL,

    nom                      VARCHAR(100) NOT NULL,
    prenom                   VARCHAR(100) NOT NULL,
    lieu_naissance           VARCHAR(150),
    telephone                VARCHAR(20),
    email                    VARCHAR(150),
    adresse                  TEXT,

    CONSTRAINT fk_demandeurs_nationalite
        FOREIGN KEY (nationalite_id)
        REFERENCES nationalites(id_nationalite),

    CONSTRAINT fk_demandeurs_genre
        FOREIGN KEY (genre_id)
        REFERENCES genres(id_genre)
);

CREATE TABLE visas_transformables (
    id_visa_transformable    SERIAL PRIMARY KEY,

    date_entree              DATE,
    date_fin                 DATE,

    lieu_entree              VARCHAR(150)
);

CREATE TABLE passeports (
    id_passeport             SERIAL PRIMARY KEY,

    date_delivrance          DATE NOT NULL,
    date_expiration          DATE NOT NULL,

    demandeur_id             INT NOT NULL,
    visa_transformable_id    INT,

    numero                   VARCHAR(100) NOT NULL UNIQUE,
    pays_delivrance          VARCHAR(100),

    CONSTRAINT fk_passeports_demandeur
        FOREIGN KEY (demandeur_id)
        REFERENCES demandeurs(id_demandeur),

    CONSTRAINT fk_passeports_visa_transformable
        FOREIGN KEY (visa_transformable_id)
        REFERENCES visas_transformables(id_visa_transformable)
);

CREATE TABLE demandes (
    id_demande               SERIAL PRIMARY KEY,

    date_demande             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    demandeur_id             INT NOT NULL,
    type_visa_id             INT NOT NULL,
    type_demande_id          INT NOT NULL,

    observations             TEXT,

    CONSTRAINT fk_demandes_demandeur
        FOREIGN KEY (demandeur_id)
        REFERENCES demandeurs(id_demandeur),

    CONSTRAINT fk_demandes_type_visa
        FOREIGN KEY (type_visa_id)
        REFERENCES types_visas(id_type_visa),

    CONSTRAINT fk_demandes_type_demande
        FOREIGN KEY (type_demande_id)
        REFERENCES types_demandes(id_type_demande)
);

-- =========================
-- TABLES ASSOCIATIVES / HISTORIQUES
-- =========================

CREATE TABLE demandes_pieces_justificatives (
    id_demande_piece_justificative SERIAL PRIMARY KEY,

    demande_id                     INT NOT NULL,
    piece_justificative_id         INT NOT NULL,

    CONSTRAINT fk_dpj_demande
        FOREIGN KEY (demande_id)
        REFERENCES demandes(id_demande),

    CONSTRAINT fk_dpj_piece
        FOREIGN KEY (piece_justificative_id)
        REFERENCES pieces_justificatives(id_piece_justificative)
);

CREATE TABLE historiques_statuts_demandes (
    id_historique_statut    SERIAL PRIMARY KEY,

    date_changement         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    demande_id              INT NOT NULL,
    statut_demande_id       INT NOT NULL,

    commentaire             TEXT,

    CONSTRAINT fk_historique_demande
        FOREIGN KEY (demande_id)
        REFERENCES demandes(id_demande),

    CONSTRAINT fk_historique_statut
        FOREIGN KEY (statut_demande_id)
        REFERENCES statuts_demandes(id_statut_demande)
);

-- =========================
-- RÉSULTATS (les plus spécifiques)
-- =========================

CREATE TABLE visas (
    id_visa                 SERIAL PRIMARY KEY,

    date_debut              DATE NOT NULL,
    date_fin                DATE NOT NULL,

    reference               VARCHAR(100) NOT NULL UNIQUE,
    lieu_entree             VARCHAR(150)
);

CREATE TABLE cartes_resident (
    id_carte_resident       SERIAL PRIMARY KEY,

    date_debut              DATE NOT NULL,
    date_fin                DATE NOT NULL,

    reference               VARCHAR(100) NOT NULL UNIQUE,
    lieu_entree             VARCHAR(150)
);