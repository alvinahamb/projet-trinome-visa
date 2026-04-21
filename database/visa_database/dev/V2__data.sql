-- =========================
-- SITUATIONS_FAMILIALES
-- =========================
INSERT INTO situations_familiales (libelle) VALUES
('CELIBATAIRE'),
('EN COUPLE'),
('MARIE'),
('DIVORCE'),
('VEUF');

-- =========================
-- NATIONALITES
-- =========================
INSERT INTO nationalites (libelle) VALUES
('MALGACHE'),
('FRANCAIS'),
('AMERICAIN'),
('ITALIEN'),
('ESPAGNOL'),
('CHINOIS');

-- =========================
-- GENRES
-- =========================
INSERT INTO genres (libelle) VALUES
('HOMME'),
('FEMME');

-- =========================
-- TYPES_VISAS
-- =========================
INSERT INTO types_visas (libelle) VALUES
('INVESTISSEUR'),
('TRAVAILLEUR');

-- =========================
-- TYPES_DEMANDES
-- =========================
INSERT INTO types_demandes (code, description) VALUES
('TRANSFORMATION', 'TRANSFORMATION DE VISA'),
('TRANSFERT', 'TRANSFERT DE VISA');

-- =========================
-- STATUTS_DEMANDES
-- =========================
INSERT INTO statuts_demandes (libelle) VALUES
('EN COURS DE TRAITEMENT'),
('APPROUVE'),
('ANNULE');

-- =========================
-- PIECES_JUSTIFICATIVES
-- =========================
INSERT INTO pieces_justificatives (libelle) VALUES
('STATUT DE SOCIETE'),
('EXTRAIT INSCRIPTION REGISTRE COMMERCE'),
('CARTE FISCALE'),
('AUTORISATION EMPLOI'),
('ATTESTATION EMPLOI');