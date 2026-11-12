# 🐾 Pawradise

> Une plateforme unique pour la **protection, le sauvetage, l'adoption, la santé et le bien-être des animaux**.
> Elle met en relation citoyens, refuges, bénévoles, vétérinaires et donateurs.

Projet réalisé en architecture **microservices** par le groupe **4DAS16 – G6**, sous la supervision de **Ghassen Hamouda**.

---

## 📌 Problématique

> Comment coordonner citoyens, sauveteurs, vétérinaires et associations pour sauver, soigner et placer plus vite les animaux en détresse ?

Aujourd'hui, les acteurs de la protection animale travaillent chacun de leur côté :

- **Acteurs dispersés** : citoyens, sauveteurs, vétérinaires et refuges ne sont pas connectés.
- **Information perdue** : un animal trouvé blessé n'est pas toujours signalé à une équipe de sauvetage.
- **Réaction trop lente** : un animal blessé attend une intervention.

Pawradise réunit tous ces acteurs dans un même écosystème numérique.

---

## ✨ Fonctionnalités principales

| Microservice | Fonctionnalités |
|---|---|
| **Lost & Found Animals** | Signalements perdus / trouvés, recherche par proximité, observations, rapprochements, réclamations de propriété |
| **Animal Rescue** | Demandes classées par urgence, missions, équipes et bénévoles, suivi et rapport de mission |
| **Adoption** | Annonces et candidatures, évaluation des adoptants, rencontres, contrats et suivi post-adoption |
| **Pet Marketplace & E-commerce** | Catalogue et panier, commandes et paiement (simulé), livraison et retours |
| **VetCare** | Cliniques et rendez-vous, dossiers médicaux, vaccins et traitements |
| **Donation & Resource Management** | Campagnes et dons, parrainages, stock de ressources, attribution aux refuges |

---

## 🏗️ Architecture

Les clients (web et mobile) passent par une **API Gateway** qui assure le routage et le contrôle d'accès (JWT).
Chaque microservice est une application **Spring Boot** indépendante avec **sa propre base PostgreSQL**.
Les services communiquent en **REST (OpenFeign)** pour les réponses immédiates et via **RabbitMQ** pour les événements asynchrones.

```
Client (Web / Mobile)
        │  Bearer JWT
        ▼
   API Gateway :8080  ◄── Keycloak :8180 (OAuth2 / JWT)
        │
        ├── MS1 Lost & Found       :8081 ── lostandfound_db
        ├── MS2 Animal Rescue      :8082 ── animalrescue_db
        ├── MS3 Adoption           :8083 ── adoption_db
        ├── MS4 Pet Marketplace    :8084 ── marketplace_db
        ├── MS5 VetCare            :8085 ── vetcare_db
        └── MS6 Donation & Resource:8086 ── donation_db
                    │
              RabbitMQ :5672 (UI :15672)

Eureka Server :8761  ·  Config Server :8888 (config-repo sur GitHub)
Le tout orchestré par Docker Compose
```

> Les schémas détaillés se trouvent dans [`Documentation/Diagrams`](./Documentation/Diagrams).

### Principes de conception

- **Un service = un domaine métier** (règles, modèle de données et API REST propres).
- **Une base de données par service** : aucun accès direct à la base d'un autre service.
- **Références par identifiants stables** (`animalId`, `userId`, `clinicId`, `shelterId`) plutôt que par copie de données.
- **REST** pour les échanges synchrones, **RabbitMQ** pour les événements asynchrones.
- **Démarrer simple** : REST, bases PostgreSQL séparées, Docker Compose et quelques événements, puis enrichir.

### Échanges entre les services

**Synchrone (REST / OpenFeign)**

| # | Source → Cible | Objet |
|---|---|---|
| 1 | Animal Rescue → Donation & Resources | Demander nourriture, médicaments, équipements |
| 2 | Animal Rescue → Adoption | Transmettre un animal prêt à être adopté |
| 3 | Lost & Found → Adoption | Orienter un animal retrouvé vers l'adoption |
| 4 | Adoption → VetCare | Consulter le dossier médical et les vaccins |
| 5 | Adoption → Marketplace | Proposer un kit de bienvenue |
| 6 | Donation & Resources → Marketplace | Acheter les fournitures manquantes au stock |

**Asynchrone (RabbitMQ)**

| Événement | Émis par | Consommé par / effet |
|---|---|---|
| `RescueReferralRequested` | Lost & Found | Animal Rescue : crée une demande de sauvetage |
| `RescueMissionCompleted` | Animal Rescue | VetCare : soins vétérinaires, animal prêt à l'adoption |
| `ResourceAllocated` | Donation & Resources | Animal Rescue : fournitures confirmées pour l'opération |
| `AdoptionCompleted` | Adoption | Lost & Found, Animal Rescue, VetCare, Donation & Resources (clôture des dossiers, rattachement du dossier médical, fin du parrainage) |
| `OrderDelivered` *(proposé)* | Marketplace | Donation & Resources : mise à jour du stock |

> ⚠️ Les événements marqués « proposé » sont à valider avec l'équipe.

---

## 🛠️ Technologies

| Catégorie | Technologies |
|---|---|
| **Backend** | Spring Boot, Spring Cloud Gateway, OpenAPI / Swagger |
| **Base de données** | PostgreSQL (PostGIS pour la recherche par proximité) |
| **Communication** | API REST (OpenFeign), RabbitMQ |
| **Infrastructure** | Docker / Docker Compose, Eureka (annuaire), Config Server |
| **Sécurité** | Keycloak (OAuth2), JWT |
| **Frontend** | Angular / React (web), Flutter (mobile) |

---

## 📁 Structure du dépôt

```
pawradise-4das16-g6/
├── Backend/
│   ├── lost-found-service/
│   ├── animal-rescue-service/
│   ├── adoption-service/
│   ├── marketplace-service/
│   ├── vetcare-service/
│   └── donation-resource-service/
├── Frontend/
├── Documentation/
│   ├── Presentation/
│   ├── Diagrams/
│   └── Dossier technique
└── README.md
```

---

## 👥 Équipe et répartition

| Membre | Service individuel | Part du service collectif (Donation & Resource) |
|---|---|---|
| **Hiba Ibn Hadj Mohamed** | Lost & Found Animals | Campagnes de collecte, enregistrement des dons, parcours des donateurs |
| **Mariem Abbes** | Animal Rescue | Catalogue des ressources matérielles, catégories et stock |
| **Eya Gobji** | Adoption | Demandes de ressources, validation et workflows d'attribution |
| **Yosra Bejaoui** | Pet Marketplace & E-commerce | Parrainages, reçus et rapports de campagne |
| **Mayssem Jihene Rezgui** | VetCare | Intégration, coordination des API, tests et appui au déploiement |

Le service **Donation & Resource Management** est un seul microservice déployable avec une seule base, même si les cinq membres y contribuent.

---

## 🔀 Règles de travail Git

- `main` : version stable. `develop` : intégration des services.
- Une branche par fonctionnalité, par exemple `feature/adoption-candidatures`.
- Service collectif : une branche par membre et par partie, par exemple `feature/donation-campagnes`.
- Fusion dans `develop` par **pull request relue par un autre membre**.

**GitHub Projects**

- Colonnes : À faire, En cours, En revue, Terminé.
- Une issue par workflow ou endpoint (ex. « POST /api/adoptions/applications »).
- Un label par service pour filtrer le travail de chacun.
- Jalon : validation intermédiaire (séance 4).

---

## 🗓️ Planning prévisionnel

| Phase | Contenu |
|---|---|
| **1. Conception** | Architecture, diagrammes, dépôt Git, présentation |
| **2. Infrastructure** | Gateway, Eureka, Config Server, Keycloak, Docker Compose |
| **3. Développement** | Workflows essentiels de chaque microservice et du service collectif |
| **4. Intégration** | RabbitMQ, frontend, tests de bout en bout, démo |

### Principaux risques et réponses

| Risque | Réponse |
|---|---|
| Service collectif à cinq (conflits de code) | Une partie par membre, branches par fonctionnalité, revue des pull requests |
| Contrats d'API instables | Définir tôt les contrats d'API et les noms d'événements, utiliser des mocks |
| Infrastructure complexe | Poser le socle tôt (Keycloak, Eureka, Gateway), un membre en appui à l'intégration |
| Panne ou retard d'un service | RabbitMQ conserve les événements en file ; suivi des tâches et point hebdomadaire |

---

## 🚀 Démarrage rapide

> 🚧 *À compléter au fur et à mesure de l'avancement du projet.*

```bash
# Cloner le dépôt
git clone https://github.com/bejaouYosra123i/pawradise-4das16-g6.git
cd pawradise-4das16-g6

# Lancer l'infrastructure (PostgreSQL, RabbitMQ, Keycloak, Eureka...)
docker compose up -d
```

**Prérequis prévus** : Java, Maven, Docker et Docker Compose, Node.js (frontend).

---

## 📚 Documentation

- 📄 Dossier technique de conception : `Documentation/`
- 🎤 Présentation du projet : `Documentation/Presentation/`
- 📐 Diagrammes (cas d'utilisation, classes, architecture, carte des contextes) : `Documentation/Diagrams/`
- 📖 Documentation des API : Swagger / OpenAPI (exposée par chaque microservice)

---

## 📄 Licence

Projet académique : *à préciser*.
