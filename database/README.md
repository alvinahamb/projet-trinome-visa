# Base de données Vehicule

## Description
Setup PostgreSQL avec Flyway pour migrations multi-environnements (dev, staging).
Chaque environnement a ses propres migrations completes (schema + donnees) dans son dossier dedie.
Chaque environnement utilise un schema PostgreSQL separe.

## Demarrage

Dev: `docker compose --profile dev up`
Staging: `docker compose --profile staging up`

## Structure

- `dev/` - Migrations completes pour le developpement (schema + donnees)
- `staging/` - Migrations completes pour la pre-production (schema + donnees)

Chaque dossier contient:
- `V1__init.sql` - Schema complet de la base (tables, contraintes, etc.)
- `V2__data.sql` - Donnees initiales de l'environnement
- `V3__...`, `V4__...` - Migrations futures (alterations de schema, nouvelles donnees, etc.)

## Regles de developpement

### Nommage Flyway
Versioned: `V<version>__<description>.sql` (ex: V1__init.sql)

### Workflow

**Nouvelles tables/structures ou donnees**: Creer dans CHAQUE dossier d'environnement (dev/ et staging/)
- Les migrations doivent etre dedoublees dans chaque environnement
- Nommer: V<numero>__description.sql
- Exemple: V3__add_table_clients.sql dans dev/ ET staging/

**Important**: Puisque chaque environnement gere ses propres migrations,
toute modification de schema doit etre ajoutee dans les deux dossiers.

### Connection applicative
Specifier le schema dans la connection:
- Dev: `?currentSchema=dev`
- Staging: `?currentSchema=staging`

### Run migrations
docker compose run --rm flyway_dev
docker compose run --rm flyway_staging
