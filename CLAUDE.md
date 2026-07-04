# CLAUDE.md — Guardrail per progetto SIW Tornei di Calcio Amatoriale

## Contesto
Progetto universitario per il corso SIW (Sistemi Informativi sul Web), Roma Tre.
Studente: Daniele. Ruolo di Claude: **tutor**, non soluzionatore.
Approccio: guidare Daniele a ragionare e implementare autonomamente. Scrivere codice direttamente solo se Daniele è esplicitamente bloccato e lo chiede.

---

## Stack tecnologico — NON deviare
- **Java 21**, Spring Boot 3.5.16, Maven
- **PostgreSQL** porta 5433, database `torneo`
- **JPA/Hibernate** con Spring Data
- **Thymeleaf** per le viste MVC
- **Spring Security** con `JdbcUserDetailsManager` e BCrypt
- **React** per almeno una sezione (classifica o calendario)
- IDE: Spring Tool Suite (Eclipse)
- Package base: `it.uniroma3.siw.torneo`

---

## Struttura del progetto — rispettare sempre

```
it.uniroma3.siw.torneo
├── model/          (Entity JPA)
├── repository/     (interfacce Spring Data)
├── service/        (logica di business, @Transactional)
├── controller/     (Spring MVC + REST)
└── security/       (SecurityConfiguration)

src/main/resources/
├── templates/      (Thymeleaf)
│   ├── tornei/
│   ├── squadre/
│   ├── partite/
│   └── admin/
└── import.sql      (dati di test, caricati con ddl-auto=create)
```

---

## Entità del dominio — NON aggiungere entità non previste

| Entità | Note chiave |
|--------|-------------|
| `Torneo` | unique su nome+anno, @ManyToMany squadre (lato proprietario) |
| `Squadra` | unique su nome+citta |
| `Giocatore` | unique su nome+cognome+dataDiNascita, @ManyToOne squadra |
| `Arbitro` | unique su codiceArbitrale |
| `Partita` | unique su dataOra+luogo, enum StatoPartita {SCHEDULED, PLAYED} |
| `Commento` | unique su partita+credentials+dataCreazione |
| `User` | @Table(name="users"), unique su email |
| `Credentials` | lato proprietario @OneToOne(cascade=ALL) su User |

---

## Regole implementative OBBLIGATORIE

### JPA e Hibernate
- `@ManyToOne` è EAGER di default — non cambiare senza motivazione
- `@OneToMany` e `@ManyToMany` sono LAZY di default
- Usare `JOIN FETCH` nel Repository quando si accede a collezioni fuori da una transazione
- MAI due `JOIN FETCH` su due collezioni diverse nella stessa query (MultipleBagFetchException) — usare due query separate
- `spring.jpa.open-in-view=false` — obbligatorio

### Service layer
- Tutti i metodi di lettura: `@Transactional(readOnly=true)`
- Tutti i metodi di scrittura: `@Transactional`
- La logica di business sta nel Service, NON nel Controller
- Dependency injection via costruttore (non @Autowired su campo)

### Spring Security
- Ruoli nel DB **sempre con prefisso** `ROLE_` → `ROLE_DEFAULT`, `ROLE_ADMIN`
- Costanti in `Credentials.java`: `DEFAULT_ROLE="ROLE_DEFAULT"`, `ADMIN_ROLE="ROLE_ADMIN"`
- Tutti i form POST **devono** avere il CSRF token Thymeleaf:
  ```html
  <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
  ```

### Controller
- I Controller non contengono logica di business
- `GlobalController` con `@ControllerAdvice` inietta `userDetails` nel Model globalmente
- Per leggere l'utente loggato: `SecurityContextHolder.getContext().getAuthentication().getName()`

### application.properties — configurazione consolidata
```properties
spring.jpa.hibernate.ddl-auto=create
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.hbm2ddl.import_files_sql_extractor=org.hibernate.tool.schema.internal.script.MultiLineSqlScriptExtractor
```

---

## Funzionalità completate ✅

- Elenco e dettaglio Torneo (con squadre partecipanti e calendario partite)
- Elenco e dettaglio Squadra (con lista giocatori)
- Dettaglio Partita (con risultato condizionale su stato PLAYED)
- Visualizzazione commenti (pubblica)
- Inserimento commento (`POST /partite/{id}/commenti`) — solo utenti loggati
- Registrazione (`POST /register`) — crea User + Credentials, redirect a login
- Login / Logout funzionanti
- Navbar con stato login (Ciao {username} / Login+Registrati)

---

## Funzionalità da completare ❌

### Sezione 4.2 — USER
- [ ] Modifica commento: `GET /commenti/{id}/edit` → form precompilato
- [ ] Modifica commento: `POST /commenti/{id}/edit` → salva, redirect a partita
- [ ] Controllo ownership: solo il proprietario può modificare il proprio commento

### Sezione 4.3 — ADMIN (URL sotto `/admin/**`)
- [ ] Torneo: creazione, modifica
- [ ] Squadra: creazione, modifica, eliminazione
- [ ] Giocatore: creazione, modifica
- [ ] Partita: creazione, registrazione risultato (cambio stato → PLAYED)
- [ ] Arbitro: creazione
- [ ] Associazione squadre a torneo

### Altro
- [ ] Classifica torneo (punti per squadra: 3 vittoria, 1 pareggio, 0 sconfitta)
- [ ] Sezione React (classifica o calendario) con `@RestController` e CORS
- [ ] Analisi sperimentale LAZY vs EAGER — Sezione 8.2 obbligatoria
- [ ] Gestione errore username duplicato in registrazione

---

## Analisi LAZY/EAGER — da svolgere obbligatoriamente

Confrontare su **calendario partite di un torneo**:
- **Scenario A**: EAGER default su `@ManyToOne` di Partita → query separate per arbitro, squadraCasa, squadraOspite, torneo per ogni partita
- **Scenario B**: `JOIN FETCH` esplicito → una sola query con tutti i JOIN
- Misurare: numero di query generate, tempo di risposta
- Documentare i risultati con i log Hibernate (`spring.jpa.show-sql=true`)

---

## Errori classici da evitare

| Errore | Soluzione |
|--------|-----------|
| `LazyInitializationException` | Aggiungere `JOIN FETCH` nel Repository |
| `MultipleBagFetchException` | Usare due query separate invece di un doppio JOIN FETCH |
| Login fallisce silenziosamente | Verificare prefisso `ROLE_` nei ruoli |
| Form POST ignorato | Aggiungere CSRF token Thymeleaf |
| SQL multi-riga in import.sql | `MultiLineSqlScriptExtractor` in application.properties |
| `model.addAttribute(lista)` senza nome | Usare sempre `model.addAttribute("nomeAttributo", valore)` |
| Null check dopo uso dell'oggetto | Controllare null PRIMA di usare l'oggetto |

---

## Note per l'orale

- Sapere spiegare perché `open-in-view=false` è una buona pratica
- Sapere spiegare la differenza tra LAZY e EAGER e quando usare JOIN FETCH
- Sapere spiegare cosa fa `@Transactional(readOnly=true)` e perché usarlo
- Sapere spiegare il ruolo del `CascadeType.ALL` tra Credentials e User
- Sapere spiegare perché i ruoli Spring Security richiedono il prefisso `ROLE_`
- Sapere spiegare la MultipleBagFetchException e come si risolve
