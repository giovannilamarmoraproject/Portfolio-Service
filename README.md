# 🚀 Portfolio-Service

[![Java 22](https://img.shields.io/badge/Java-22-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot 3](https://img.shields.io/badge/Spring_Boot-3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring WebFlux](https://img.shields.io/badge/Spring_WebFlux-Reactive-3FA037?style=for-the-badge&logo=spring&logoColor=white)](https://docs.spring.io/spring-framework/reference/web/webflux.html)
[![Redis](https://img.shields.io/badge/Redis-Reactive-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg?style=for-the-badge)](LICENSE)

Microservizio reattivo ad alte prestazioni sviluppato con **Spring Boot 3** e **Project Reactor (WebFlux)**. Funge da backend API per la WebApp personale di [Giovanni Lamarmora](https://giovannilamarmora.github.io), integrando e aggregando i contenuti headless forniti da **Strapi CMS** con supporto avanzato alla cache (in-memory o Redis reattivo).

---

## 📑 Indice

- [Caratteristiche Principali](#-caratteristiche-principali)
- [Architettura & Stack Tecnologico](#-architettura--stack-tecnologico)
- [API Endpoints](#-api-endpoints)
- [Gestione della Cache](#-gestione-della-cache)
- [Configurazione ed Environment Variables](#-configurazione-ed-environment-variables)
- [Avvio e Sviluppo Locale](#-avvio-e-sviluppo-locale)
- [Docker & Docker Compose](#-docker--docker-compose)
- [CI/CD & Deployment](#-cicd--deployment)
- [Autore & Licenza](#-autore--licenza)

---

## ✨ Caratteristiche Principali

- **Completamente Reattivo (Non-blocking I/O)**: Architettura end-to-end asincrona costruita su **Spring WebFlux** e **Project Reactor** (`Mono` / `Flux`).
- **Integrazione Strapi CMS**: Client reattivo (`WebClient`) per l'estrazione, trasformazione e mappatura dei contenuti gestiti su Strapi (configurazioni portfolio, esperienze lavorative, progetti personali, corsi e certificazioni).
- **Internazionalizzazione (i18n)**: Supporto nativo per contenuti multilingua tramite il parametro query `locale` (es. `it`, `en`).
- **Strategia di Caching Flessibile**:
  - Cache In-Memory con `ConcurrentMapCacheManager`.
  - Cache distribuita con **Redis Reattivo** (`RedisCacheManager`).
  - Schedulazione programmata con espressioni cron (supportate da `cron-utils`) per l'invalidation automatica e endpoint dedicato per l'eviction on-demand.
- **Logging & Tracing Avanzato**:
  - Tracciamento tempi di esecuzione e logging strutturato dei layer controller, service e cache via `@LogInterceptor` e `@Logged` (libreria `utils-code`).
  - Integrazione predisposta per Google Cloud Logging e Logtail (`logback-google.xml`, `logback-logtail.xml`).
  - Distributed tracing con **Micrometer Tracing** e bridge Zipkin Brave.
- **Documentazione OpenAPI / Swagger**: UI interattiva e specifiche OpenAPI 3 integrate con Springdoc OpenAPI WebFlux.

---

## 🛠️ Architettura & Stack Tecnologico

- **Linguaggio**: Java 22
- **Framework**: Spring Boot 3.4+ / Spring WebFlux
- **Programmazione Reattiva**: Project Reactor & Reactive Streams
- **Data & Caching**: Spring Data Redis Reactive, Lettuce, Spring Cache
- **Integrazioni Esterne**: Strapi Headless CMS API v4/v5 via Reactive WebClient
- **Utility & Tracing**: `io.github.giovannilamarmora.utils:utils-code`, Micrometer, Brave
- **Documentazione API**: Springdoc OpenAPI WebFlux UI (`v2.7.0+`)
- **Build Tool**: Apache Maven 3.9+
- **Container**: Docker (Multi-stage build basato su Eclipse Temurin 22)
- **Registry & CI/CD**: GitHub Actions, GitHub Container Registry (GHCR)

---

## 📡 API Endpoints

I principali endpoint esposti dal servizio sono i seguenti:

| Metodo | Path | Descrizione | Parametri |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Redirect permanente (301) verso il sito portfolio statico | Nessuno |
| `GET` | `/v1/app/portfolio/data` | Recupera l'intero aggregato di dati del portfolio (configurazione, lavori, progetti, corsi) | `locale` (query, obbligatorio, es. `it` o `en`) |
| `DELETE` | `/v1/app/cache/evict` | Svuota e invalida la cache attiva (In-Memory o Redis) | Nessuno |
| `GET` | `/swagger-ui.html` | Interfaccia grafica Swagger UI per testare le API | Nessuno |
| `GET` | `/api-docs` | Specifiche OpenAPI in formato JSON | Nessuno |

### Esempio Richiesta Dati Portfolio

```bash
curl -X GET "http://localhost:8080/v1/app/portfolio/data?locale=it" \
  -H "Accept: application/json"
```

### Esempio Invalidation Cache

```bash
curl -X DELETE "http://localhost:8080/v1/app/cache/evict" \
  -H "Accept: application/json"
```

---

## 💾 Gestione della Cache

Il servizio include due strategie di caching intercambiabili controllate dai profili o dalle variabili di ambiente:

1. **In-Memory Cache (Default / Locale)**:
   - Abilitata impostando `spring.data.cache.active=true` e `spring.data.redis.enabled=false`.
   - Utilizza `ConcurrentMapCacheManager` registrato sulla cache `Portfolio_Cache`.
2. **Redis Distributed Cache (Produzione / Cluster)**:
   - Abilitata impostando `spring.data.cache.active=true` e `spring.data.redis.enabled=true`.
   - Si connette tramite Lettuce reactive driver a un'istanza Redis remota.
3. **No-Cache (Passthrough)**:
   - Impostando `spring.data.cache.active=false`, ogni richiesta inoltra la chiamata direttamente a Strapi CMS senza caching.

---

## ⚙️ Configurazione ed Environment Variables

La configurazione principale risiede in `src/main/resources/application.yml` e nei relativi profili `application-local.yml` e `application-deploy.yml`.

| Variabile | Default | Descrizione |
| :--- | :--- | :--- |
| `PORT` | `8006:8080` | Mappatura porta host:container Docker |
| `SPRING_PROFILES_ACTIVE` | `local` / `deploy` | Profilo Spring attivo (`local` per dev, `deploy` per produzione) |
| `STRAPI_BASE_URL` | `https://strapi.giovannilamarmora.com` | URL base dell'istanza Strapi CMS |
| `STRAPI_AUTH_TOKEN` | - | Bearer Token API per autenticare le richieste a Strapi |
| `CACHE_ENABLE` | `false` | Attiva o disattiva il layer di caching dei dati |
| `REDIS_CACHE_ENABLE` | `false` | Se `true`, attiva Redis al posto della cache in-memory |
| `REDIS_CACHE_HOST` | `localhost` | Host del server Redis |
| `REDIS_CACHE_PORT` | `6379` | Porta del server Redis |
| `REDIS_CACHE_USERNAME` | - | Username per l'autenticazione a Redis |
| `REDIS_CACHE_PASSWORD` | - | Password per l'autenticazione a Redis |
| `LOGGING_LEVEL` | `INFO` | Livello di log dell'applicazione (`DEBUG`, `INFO`, `WARN`, `ERROR`) |
| `LOGBACK_FILE` | `classpath:logback-spring.xml` | File di configurazione Logback da caricare (`logback-google.xml` per GCP) |
| `SHOW_UTILS_EXCEPTION_STACKTRACE` | `false` | Include lo stacktrace completo nelle risposte d'errore REST |
| `APP_ENV` | `Local` / `Production` | Etichetta dell'ambiente usata nei log |

---

## 💻 Avvio e Sviluppo Locale

### Prerequisiti
- **JDK 22** installato e configurato (es. Amazon Corretto o Eclipse Temurin)
- **Apache Maven 3.9+** (oppure utilizzare il wrapper `mvnw`)
- Istanza Strapi raggiungibile con token API valido

### Compilazione e Test

```bash
# Compila il progetto
mvn clean compile

# Esegui la suite di test
mvn test

# Genera il pacchetto JAR
mvn clean package
```

### Esecuzione con Profilo Locale

```bash
# Tramite Maven Spring Boot Plugin
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Oppure eseguendo il jar compilato
java -jar target/portfolio-service.jar --spring.profiles.active=local
```

Una volta avviato:
- L'API sarà disponibile su: `http://localhost:8080`
- Swagger UI sarà consultabile su: `http://localhost:8080/swagger-ui.html`
- Specifiche OpenAPI su: `http://localhost:8080/api-docs`

---

## 🐳 Docker & Docker Compose

### Build Manuale dell'Immagine Docker

```bash
docker build -t portfolio-service:latest .
```

### Avvio con Docker Compose

Il file `docker-compose.yml` è predisposto per l'integrazione con reti esterne, gestione dei secrets e aggiornamenti automatici tramite **WUD (What's Up Docker)**.

```bash
# Avvio del servizio in background
docker compose up -d

# Visualizzazione dei log del container
docker compose logs -f portfolio-service
```

---

## 🚀 CI/CD & Deployment

Il repository utilizza **GitHub Actions** (`.github/workflows/deploy_prod_ghcr.yml`):
1. **Build & Test**: Ad ogni push sul branch `master`, esegue compilazione e test con JDK 22.
2. **Build Multi-Arch & Push su GHCR**: Costruisce l'immagine Docker per architetture `linux/amd64` e `linux/arm64` e la pubblica sul GitHub Container Registry (`ghcr.io`).
3. **GitHub Release**: Genera automaticamente note di rilascio, tag e changelog.
4. **Auto-Deploy**: Tramite label WUD configurate in `docker-compose.yml`, l'ambiente di produzione aggiorna automaticamente il container quando un nuovo digest viene rilevato su GHCR.

---

## 👤 Autore & Licenza

- **Autore**: [Giovanni Lamarmora](https://github.com/giovannilamarmora)
- **Portfolio Web**: [giovannilamarmora.github.io](https://giovannilamarmora.github.io)
- **Licenza**: Distribuito sotto licenza [Apache 2.0](LICENSE).
