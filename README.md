# Ymmo_Api

Ce dépôt contient l'API backend du projet Ymmo, implémentée avec Spring Boot (Java 26) et exposée via un conteneur Docker. Le projet peut être lancé localement via Docker Compose ou construit avec Maven.

Checklist (ce que couvre ce README)
- Présentation rapide du projet
- Services fournis par le fichier `docker-compose.yml`
- Variables d'environnement attendues (fichier `.env`)
- Commandes pour démarrer en local (Docker Compose / Maven)
- Points d'accès utiles (Swagger / pgAdmin)

1) Présentation
----------------
Ymmo_Api est une API REST Java (Spring Boot) qui gère les utilisateurs, propriétés, transactions, favoris et un stockage d'objets S3 compatible (via rustfs). Le projet utilise PostgreSQL pour la persistance et inclut un script d'initialisation SQL dans `init/schema.sql`.

2) Architecture et services (docker-compose)
-------------------------------------------
Le fichier `docker-compose.yml` définit les services suivants :

- ymmo-db (postgres:16-alpine)
  - Port exposé : 5432
  - Volume : `postgres_data` et montage local `./init` pour initialiser la base (`init/schema.sql`).
  - Variables attendues : POSTGRES_DB, POSTGRES_USER, POSTGRES_PASSWORD
  - Healthcheck configuré (pg_isready)

- ymmo-api (build local via Dockerfile)
  - Application Spring Boot packagée en jar et exécutée avec Java 26
  - Port exposé : 8080
  - Variables attendues :
	- SPRING_DATASOURCE_URL (ex: jdbc:postgresql://ymmo-db:5432/${POSTGRES_DB})
	- SPRING_DATASOURCE_USERNAME
	- SPRING_DATASOURCE_PASSWORD
	- JWT_REFRESH_SECRET_KEY, JWT_ACCESS_SECRET_KEY
	- JWT_REFRESH_EXPIRATION, JWT_ACCESS_EXPIRATION
	- S3_ENDPOINT (ex: http://ymmo-rustfs:9000)
	- S3_ACCESSKEY, S3_SECRETKEY
  - Dépend de `ymmo-db` (health) et `ymmo-rustfs` (service_started)

- ymmo-rustfs (rustfs/rustfs)
  - Fournit un endpoint S3-compatible (MinIO-like) utilisé par l'application
  - Ports exposés : 9000, 9001
  - Volume : `rustfs_data`
  - Variables attendues : RUSTFS_ROOT_USER, RUSTFS_ROOT_PASSWORD

- ymmo-pgadmin (dpage/pgadmin4) — profil `tools`
  - Outil optionnel pour administrer la base
  - Port exposé : 5050 (mappé vers 80 du conteneur)
  - Volume : `pgadmin_data`
  - Variables attendues : PGADMIN_EMAIL, PGADMIN_PASSWORD

Les volumes déclarés : `postgres_data`, `rustfs_data`, `pgadmin_data`.

3) Fichier `.env` recommandé
--------------------------------
Créez un fichier `.env` à la racine du projet avec au minimum les variables suivantes (exemple) :

```powershell
# PostgreSQL
POSTGRES_DB=ymmo_db
POSTGRES_USER=ymmo_user
POSTGRES_PASSWORD=changeme

# PGAdmin (profil tools)
PGADMIN_EMAIL=admin@example.com
PGADMIN_PASSWORD=admin

# JWT (exemples)
JWT_REFRESH_SECRET_KEY=change_this_refresh_secret
JWT_ACCESS_SECRET_KEY=change_this_access_secret
JWT_REFRESH_EXPIRATION=2592000000   # en milisecondes (ex: 30 jours)
JWT_ACCESS_EXPIRATION=3600000       # en milisecondes (ex: 1 heure)

# RustFS / S3 (utilisé par l'application)
RUSTFS_ROOT_USER=admin
RUSTFS_ROOT_PASSWORD=admin
S3_ACCESS_USER_KEY=s3accesskey
S3_SECRET_USER_KEY=s3secretkey

# Optionnel : autres variables d'environnement
```

4) Initialisation de la base
-----------------------------
Le dossier `init/` contient `schema.sql` qui sera exécuté automatiquement par l'image PostgreSQL au démarrage (monté dans `/docker-entrypoint-initdb.d`). Il crée les tables principales (users, properties, wallets, transactions, favorites, etc.).

5) Lancer le projet
---------------------
Avec Docker Compose (recommandé)

```powershell
# Démarre tous les services (sans les services en profile 'tools')
docker compose up --build -d

# Si vous voulez inclure les outils (pgAdmin), activez le profile 'tools'
docker compose --profile tools up --build -d

# Voir les logs de l'API
docker compose logs -f ymmo-api
```

**Attention !** S'il s'agit de la première fois que vous executez le projet, il est nécessaire de paramétrer le bucket RustFS.

Pour cela, rendez-vous sur http://localhost:9001, connectez-vous avec les identifiants de connexion admin puis : 

- Allez sur l'onglet compartiments, créez un compartiment avec le même nom que la variable d'environnement "BUCKET_NAME"
- Rendez-vous dans les parameterises du bucket et mettez la visibilité en "Publique", sans ça vous ne pourrez pas avoir accès aux images depuis le front-end
- Créez un groupe d'utilisateur et mettez la politique "readwrite"
- Créez un utilisateur et ajoutez-le dans le groupe
- Enfin, créez une clé d'accès pour cet utilisateur et mettez les informations dans le fichier .env ("Clé d'accès" → "S3_ACCESS_USER_KEY" et "Clé secrète → "GZanK383n11fLJI3y2JDQ8CPWuNqd83NdpjA1pZ8")
- Relancez l'API pour mettre ses variables d'environnement à jour


Construire l'image Docker localement

```powershell
docker build -t ymmo-api:local .
docker run --env-file .env -p 8080:8080 --network bridge ymmo-api:local
```

6) Points d'accès utiles
------------------------
- API: http://localhost:8080
- Swagger / OpenAPI UI: http://localhost:8080/swagger-ui/index.html (ou /swagger-ui.html)
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- PostgreSQL: hôte localhost:5432 (si conteneur exposé), nom de la DB et creds définis dans `.env`
- pgAdmin (si démarré, profile tools): http://localhost:5050
- RustFS S3 endpoint: http://localhost:9001 (login avec RUSTFS_ROOT_USER / RUSTFS_ROOT_PASSWORD)

7) Tests
--------
Lancer les tests unitaires avec Maven :

```powershell
./mvnw test
```

8) Dépannage rapide
-------------------
- Si la base ne démarre pas : vérifier que les variables POSTGRES_* sont correctes et qu'aucun autre service n'écoute sur le port 5432.
- Si l'API indique des erreurs S3 : vérifier que `ymmo-rustfs` est démarré et que `S3_ENDPOINT`, `S3_ACCESSKEY` et `S3_SECRETKEY` correspondent.
- Pour forcer la réinitialisation des volumes :

```powershell
docker compose down -v
docker compose up --build
```

9) Structure du projet (emplacement des éléments clés)
------------------------------------------------------
- `src/main/java/com/ymmo/ymmoapi` : code source Spring Boot
- `src/main/resources/application.properties` : configuration Spring
- `init/schema.sql` : script d'initialisation PostgreSQL
- `Dockerfile` : construction de l'image de l'API
- `docker-compose.yml` : orchestre Postgres, l'API, rustfs et pgAdmin

10) Contributeurs
----------------------------

- Noah CHARRIN--BOURRAT
- Léna RICARD
- Emma DE OLIVEIRA
