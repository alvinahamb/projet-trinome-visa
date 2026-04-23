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
('ALGERIEN'),
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
('CREE'),
('SCANNE'),
('TERMINE'),
('ANNULE');

-- =========================
-- PIECES_JUSTIFICATIVES
-- =========================
INSERT INTO pieces_justificatives (libelle, visa_types_id) VALUES
('STATUT DE SOCIETE', 1),
('EXTRAIT INSCRIPTION REGISTRE COMMERCE', 1),
('CARTE FISCALE', 1),
('AUTORISATION EMPLOI', 2),
('ATTESTATION EMPLOI', 2);