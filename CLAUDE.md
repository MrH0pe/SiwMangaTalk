# SiwMangaTalk — Blueprint

Progetto universitario SIW — Roma Tre · Daniele Panzeri

---

## Configurazione & Stack

| Parametro | Valore |
|-----------|--------|
| Framework | Spring Boot 4.0.5 |
| Java | 17 |
| Database | PostgreSQL @ localhost:5433 |
| Database name | SiwMangaTalk |
| Server | localhost:8080 |
| ddl-auto | update |
| open-in-view | false |
| Package base | it.uniroma3.siw.manga |

### Dipendenze Maven

| Dipendenza | Scope |
|-----------|-------|
| spring-boot-starter-data-jpa | compile |
| spring-boot-starter-thymeleaf | compile |
| spring-boot-starter-webmvc | compile |
| spring-boot-starter-security | compile |
| spring-boot-starter-validation | compile |
| postgresql | runtime |
| spring-boot-starter-data-jpa-test | test |
| spring-boot-starter-thymeleaf-test | test |
| spring-boot-starter-webmvc-test | test |

---

## Architettura a strati

```
Browser (Thymeleaf SSR + componenti React)
          ↕
Controller layer  (@Controller MVC + @RestController JSON /api)
          ↕
Service layer     (@Transactional, logica di business)
          ↕
Repository layer  (Spring Data JPA — JpaRepository)
          ↕
Database          (PostgreSQL 5433 / Hibernate ORM)
```

---

## Modello dati (Entità JPA)

### Manga
- Campi: `id`, `nome`, `descrizione`, `annoPubblicazione`, `pathImmagine`, `pathSfondo`
- `@OneToOne(mappedBy="manga")` ← Autore `[EAGER]`
- `@OneToMany` → Votazione `[LAZY]`
- `@OneToMany` → Commento `[LAZY]`

### Autore
- Campi: `id`, `nome`, `cognome`, `descrizione`, `dataDiNascita`
- `@OneToOne` → Manga (lato proprietario)

### User (`@Table("utente")`)
- Campi: `id`, `name`, `email` (unique)
- `@OneToMany` ← Commento `[LAZY]`
- `@OneToMany` ← Votazione `[LAZY]`

### Credentials
- Campi: `id`, `username` (unique), `password` (BCrypt), `role`
- Costanti: `ROLE_DEFAULT`, `ROLE_ADMIN`
- `@OneToOne` → User `[CascadeType.ALL]`

### Commento
- Campi: `id`, `testo`, `tempoPubblicazione`
- `@ManyToOne` → User `[EAGER]`
- `@ManyToOne` → Manga `[EAGER]`
- `@ManyToOne` → Commento padre `[EAGER, nullable]`
- `@OneToMany` ← risposte `[LAZY, cascade=ALL, orphanRemoval=true]`

### Votazione (`unique(utente_id, manga_id)`)
- Campi: `id`, `valoreStelline` (Double, 0.5–5.0)
- `@ManyToOne` → Manga `[LAZY]`
- `@ManyToOne` → User `[LAZY]`
> LAZY (deviazione dal default EAGER di `@ManyToOne`): nessuna vista naviga da una Votazione verso manga/utente; media e conteggio usano query aggregate (AVG/COUNT).

---

## Casi d'uso per ruolo

### Anonimo (visitatore)
- Visualizza lista manga (con ordinamento alfabetico o per rating)
- Visualizza dettaglio manga (voti aggregati, commenti)
- Visualizza lista autori
- Visualizza dettaglio autore
- Login
- Registrazione (crea User + Credentials)

### ROLE_DEFAULT (utente loggato)
- Tutti i casi d'uso dell'anonimo
- Vota un manga (0.5–5.0 stelle, una volta per manga)
- Commenta un manga
- Rispondi a un commento
- Modifica il proprio commento (via PATCH /api)
- Elimina il proprio commento
- Visualizza "I miei commenti"

### ROLE_ADMIN (amministratore)
- Dashboard admin (tutti gli utenti + i loro commenti)
- Pannello di gestione manga
- Elimina qualsiasi commento
- Redirect automatico a `/admin` al login
> L'admin non può votare né commentare. Al `GET /mangas/{id}` viene reindirizzato a `/mangas/admin/{id}`.

---

## Mappa URL — Tutti gli endpoint

### MVC Controllers

| Metodo | URL | Ruolo | Descrizione |
|--------|-----|-------|-------------|
| GET | `/` · `/index` | tutti | Home page |
| GET | `/mangas` | tutti | Lista manga (`?sort=alpha-asc\|alpha-desc\|rating-asc\|rating-desc`) |
| GET | `/mangas/{id}` | tutti | Dettaglio manga (admin → redirect `/mangas/admin/{id}`) |
| POST | `/mangas/{id}/voto` | auth | Vota manga |
| GET | `/autori` | tutti | Lista autori |
| GET | `/autori/{id}` | tutti | Dettaglio autore |
| GET | `/mieiCommenti` | auth | I miei commenti |
| POST | `/manga/{id}/commenti` | auth | Nuovo commento |
| POST | `/manga/{id}/commenti/{padreId}/risposte` | auth | Risposta a un commento |
| POST | `/commenti/{id}/elimina` | auth | Elimina commento (`?fromAdmin=true`) |
| GET | `/login` | tutti | Pagina login |
| GET | `/register` | tutti | Form registrazione |
| POST | `/register` | tutti | Salva nuovo utente |
| POST | `/logout` | tutti | Termina sessione |
| GET | `/admin` | admin | Dashboard admin |
| GET | `/mangas/admin/{id}` | admin | Pannello manga |

### REST API — `@RestController /api` (usata da React)

| Metodo | URL | Ruolo | Risposta |
|--------|-----|-------|---------|
| POST | `/api/manga/{id}/commenti` | auth | JSON `{id, testo, autore, data}` |
| POST | `/api/commenti/{id}/risposte` | auth | JSON `{id, testo, autore, data}` |
| PATCH | `/api/commenti/{id}/modifica` | auth | JSON `{testo}` oppure 403/404 |

### Sicurezza (Spring Security)

- **Autenticazione**: `JdbcUserDetailsManager` + BCrypt
- **Login**: `/login` → successo: admin→`/admin`, utente→`/`
- **Logout**: `/logout` → invalida sessione + cookie `JSESSIONID`
- **CSRF**: attivo su tutti i form POST Thymeleaf

---

## Template Thymeleaf

| Template | URL associato |
|----------|--------------|
| `index.html` | `/` |
| `manga/listaManga.html` | `/mangas` |
| `manga/mostraManga.html` | `/mangas/{id}` |
| `manga/adminManga.html` | `/mangas/admin/{id}` |
| `autori/listaAutori.html` | `/autori` |
| `autori/mostraAutore.html` | `/autori/{id}` |
| `commenti/mieiCommenti.html` | `/mieiCommenti` |
| `admin/adminHome.html` | `/admin` |
| `admin/index.html` | — |
| `authentication/login.html` | `/login` |
| `authentication/registerUser.html` | `/register` |

---

## Struttura classi Java

```
it.uniroma3.siw.manga
├── SiwMangaTalkApplication.java          @SpringBootApplication
├── authentication/
│   └── SecurityConfiguration.java        @Configuration @EnableWebSecurity
├── configuration/
│   └── DataLoader.java                   @Component
├── controller/
│   ├── HomeController.java               @Controller
│   ├── AutoreController.java             @Controller
│   ├── MangaController.java              @Controller
│   ├── CommentoController.java           @Controller
│   ├── AdminController.java              @Controller @RequestMapping("/mangas/admin")
│   ├── AdminHomeController.java          @Controller
│   ├── AuthenticationController.java     @Controller
│   ├── CommentoRestController.java       @RestController @RequestMapping("/api")
│   ├── GlobalController.java             @ControllerAdvice
│   └── GlobalBindingConfig.java          @ControllerAdvice
├── model/
│   ├── Manga.java                        @Entity
│   ├── Autore.java                       @Entity
│   ├── User.java                         @Entity @Table("utente")
│   ├── Credentials.java                  @Entity
│   ├── Commento.java                     @Entity
│   └── Votazione.java                    @Entity @Table(uniqueConstraints)
├── repository/
│   ├── MangaRepository.java
│   ├── AutoreRepository.java
│   ├── UtenteRepository.java
│   ├── CredentialsRepository.java
│   ├── CommentoRepository.java
│   └── VotazioneRepository.java
└── service/
    ├── MangaService.java                 @Service @Transactional(readOnly=true)
    ├── AutoreService.java                @Service @Transactional(readOnly=true)
    ├── CommentoService.java              @Service @Transactional
    ├── CredentialsService.java           @Service @Transactional
    ├── UserService.java                  @Service @Transactional(readOnly=true)
    └── VotazioneService.java             @Service @Transactional