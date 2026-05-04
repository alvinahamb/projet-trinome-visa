-- ============================================================
-- V5__add_visa_transformable_to_demande.sql
-- Add visa_transformable reference to demandes
-- ============================================================

ALTER TABLE demandes
    ADD COLUMN visa_transformable_id INT;

ALTER TABLE demandes
    ADD CONSTRAINT fk_demande_visa_transformable
        FOREIGN KEY (visa_transformable_id)
        REFERENCES visas_transformables (id_visa_transformable);
