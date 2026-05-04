ALTER TABLE demandes ADD COLUMN passeport_id INT;
ALTER TABLE demandes ADD CONSTRAINT fk_demande_passeport FOREIGN KEY (passeport_id) REFERENCES passeports (id_passeport);
