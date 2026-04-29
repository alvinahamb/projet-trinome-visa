-- ============================================================
-- V3__add_duplicata_demande_type.sql - Add DUPLICATA demande type
-- ============================================================

INSERT INTO demande_types (id_demande_type, code, description) 
VALUES (3, 'DUPLICATA', 'Demande de duplicata d''un visa existant');
