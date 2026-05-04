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

CREATE TABLE visa_types (
    id_visa_type             SERIAL PRIMARY KEY,
    libelle                  VARCHAR(100) NOT NULL
);

CREATE TABLE demande_types (
    id_demande_type          SERIAL PRIMARY KEY,
    code                     VARCHAR(50) NOT NULL UNIQUE,
    description              TEXT
);

CREATE TABLE statuts_demandes (
    id_statut_demande        SERIAL PRIMARY KEY,
    libelle                  VARCHAR(100) NOT NULL
);

CREATE TABLE pieces_justificatives (
    id_piece_justificative   SERIAL PRIMARY KEY,
    libelle                  VARCHAR(150) NOT NULL, 
    visa_types_id            INT NOT NULL,
    est_obligatoire          BOOLEAN NOT NULL,

    CONSTRAINT fk_pieces_justificatives_visa_types
        FOREIGN KEY (visa_types_id)
        REFERENCES visa_types(id_visa_type)
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
    situation_familiale_id   INT,

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
        REFERENCES genres(id_genre),
    
    CONSTRAINT fk_demandeurs_situation_familiale
        FOREIGN KEY (situation_familiale_id)
        REFERENCES situations_familiales(id_situation_familiale)
);

CREATE TABLE visas_transformables (
    id_visa_transformable    SERIAL PRIMARY KEY,

    date_entree              DATE,
    date_fin                 DATE,
    demandeur_id             INT,

    reference                VARCHAR(100) NOT NULL UNIQUE,
    lieu_entree              VARCHAR(150),

    CONSTRAINT fk_visas_transformables_demandeur
        FOREIGN KEY (demandeur_id)
        REFERENCES demandeurs(id_demandeur)
);

CREATE TABLE passeports (
    id_passeport             SERIAL PRIMARY KEY,

    date_delivrance          DATE NOT NULL,
    date_expiration          DATE NOT NULL,
    demandeur_id             INT NOT NULL,

    numero                   VARCHAR(100) NOT NULL UNIQUE,
    pays_delivrance          VARCHAR(100),

    CONSTRAINT fk_passeports_demandeur
        FOREIGN KEY (demandeur_id)
        REFERENCES demandeurs(id_demandeur)
);

CREATE TABLE demandes (
    id_demande               SERIAL PRIMARY KEY,

    date_demande             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    demandeur_id             INT NOT NULL,
    type_visa_id             INT NULL,
    type_demande_id          INT NOT NULL,

    observations             TEXT,

    CONSTRAINT fk_demandes_demandeur
        FOREIGN KEY (demandeur_id)
        REFERENCES demandeurs(id_demandeur),

    CONSTRAINT fk_demandes_visa_type
        FOREIGN KEY (type_visa_id)
        REFERENCES visa_types(id_visa_type),

    CONSTRAINT fk_demandes_demande_type
        FOREIGN KEY (type_demande_id)
        REFERENCES demande_types(id_demande_type)
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
    demande_id              INT NOT NULL,

    reference               VARCHAR(100) NOT NULL UNIQUE,
    lieu_entree             VARCHAR(150),

    CONSTRAINT fk_visas_demande
        FOREIGN KEY (demande_id)
        REFERENCES demandes(id_demande)
);

CREATE TABLE cartes_resident (
    id_carte_resident       SERIAL PRIMARY KEY,

    date_debut              DATE NOT NULL,
    date_fin                DATE NOT NULL,
    demande_id              INT NOT NULL,

    reference               VARCHAR(100) NOT NULL UNIQUE,
    lieu_entree             VARCHAR(150),

    CONSTRAINT fk_cartes_resident_demande
        FOREIGN KEY (demande_id)
        REFERENCES demandes(id_demande)
);