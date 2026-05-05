alter table demandes_pieces_justificatives add column path varchar(255);
alter table demandes_pieces_justificatives add column nom_fichier varchar(255);
alter table demandes_pieces_justificatives add column date_ajout timestamp default current_timestamp;