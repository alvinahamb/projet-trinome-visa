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
-- TYPES_VISAS visa_types
-- =========================
INSERT INTO visa_types (libelle) VALUES
('INVESTISSEUR'),
('TRAVAILLEUR');

-- =========================
-- TYPES_DEMANDES demande_types
-- =========================
INSERT INTO demande_types (code, description) VALUES
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
INSERT INTO pieces_justificatives (libelle, visa_types_id, est_obligatoire) VALUES
('STATUT DE SOCIETE', 1, FALSE),
('EXTRAIT INSCRIPTION REGISTRE COMMERCE', 1, FALSE),
('CARTE FISCALE', 1, FALSE),
('AUTORISATION EMPLOI', 2, FALSE),
('ATTESTATION EMPLOI', 2, FALSE);