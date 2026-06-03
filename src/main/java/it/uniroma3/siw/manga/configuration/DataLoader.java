package it.uniroma3.siw.manga.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Credentials;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.repository.CredentialsRepository;
import it.uniroma3.siw.manga.repository.UtenteRepository;

/**
 * Inizializzazione dati all'avvio dell'applicazione.
 *
 * Crea l'utente admin con ruolo ADMIN se non è già presente nel database.
 * Viene eseguito una sola volta in modo idempotente: se l'utente "Admin" esiste
 * già (ad esempio da un avvio precedente), non viene fatto nulla.
 *
 * Credenziali dell'admin: username="Admin", password="Admin".
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final CredentialsRepository credentialsRepository;
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    /** Costruttore con iniezione delle dipendenze tramite Spring. */
    public DataLoader(CredentialsRepository credentialsRepository,
                      UtenteRepository utenteRepository,
                      PasswordEncoder passwordEncoder) {
        this.credentialsRepository = credentialsRepository;
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Eseguito all'avvio: crea l'admin se non esiste ancora nel DB.
     * La password viene cifrata con BCrypt prima del salvataggio.
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (credentialsRepository.findByUsername("Admin") == null) {

            // Riusa il profilo utente se l'email esiste già (avvio precedente parzialmente fallito),
            // altrimenti ne crea uno nuovo.
            User adminUser = utenteRepository.findByEmail("admin@mangatalk.it")
                    .orElseGet(() -> {
                        User u = new User();
                        u.setName("Admin");
                        u.setEmail("admin@mangatalk.it");
                        return utenteRepository.save(u);
                    });

            // Crea le credenziali con ruolo ADMIN e password cifrata
            Credentials adminCredentials = new Credentials();
            adminCredentials.setUsername("Admin");
            adminCredentials.setPassword(passwordEncoder.encode("Admin"));
            adminCredentials.setRole(Credentials.ADMIN_ROLE);
            adminCredentials.setUtente(adminUser);
            credentialsRepository.save(adminCredentials);
        }
    }
}
