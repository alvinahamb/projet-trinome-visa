-- ============================================================
-- V9__improve_demande_table.sql
-- Améliore la structure de la table demandes avec des contraintes et des indices
-- ============================================================

-- Ajouter des indices pour améliorer les performances de recherche
CREATE INDEX idx_demandes_demandeur_id ON demandes(demandeur_id);
CREATE INDEX idx_demandes_type_visa_id ON demandes(type_visa_id);
CREATE INDEX idx_demandes_type_demande_id ON demandes(type_demande_id);
CREATE INDEX idx_demandes_passeport_id ON demandes(passeport_id);
CREATE INDEX idx_demandes_visa_transformable_id ON demandes(visa_transformable_id);
CREATE INDEX idx_demandes_date_demande ON demandes(date_demande);

-- Ajouter des contraintes NOT NULL pour les colonnes importantes
ALTER TABLE demandes ALTER COLUMN passeport_id SET NOT NULL;

-- Ajouter des commentaires aux colonnes pour la documentation
COMMENT ON COLUMN demandes.id_demande IS 'Identifiant unique de la demande';
COMMENT ON COLUMN demandes.date_demande IS 'Date de création de la demande';
COMMENT ON COLUMN demandes.url_demande IS 'URL de la fiche demande (localhost:5173/demande-fiche/{id})';
COMMENT ON COLUMN demandes.qr_lien IS 'Chemin relatif du code QR généré';
COMMENT ON COLUMN demandes.qr_chemin IS 'Chemin absolu du code QR généré';
COMMENT ON COLUMN demandes.observations IS 'Observations additionnelles sur la demande';
