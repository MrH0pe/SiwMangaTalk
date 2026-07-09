package it.uniroma3.siw.manga.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Credentials;
import it.uniroma3.siw.manga.repository.CredentialsRepository;

@Service
public class CredentialsService {

    // Encoder BCrypt iniettato da SecurityConfiguration: cifra la password prima di salvarla
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Repository per l'accesso alla tabella "credentials" nel database
    @Autowired
    private CredentialsRepository credentialsRepository;

    // Restituisce le credenziali associate all'id specificato, null se non trovate
    @Transactional(readOnly = true)
    public Credentials getCredentials(Long id) {
        Credentials result = this.credentialsRepository.findById(id).orElse(null);
        return result;
    }

    // Restituisce le credenziali associate all'username specificato, null se non trovate
    @Transactional(readOnly = true)
    public Credentials getCredentials(String username) {
        return this.credentialsRepository.findByUsername(username);
    }

    // Salva le credenziali del nuovo utente: assegna il ruolo DEFAULT e cifra la password con BCrypt
    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return this.credentialsRepository.existsByUsername(username);
    }

    // Restituisce tutte le credenziali degli utenti non-admin che hanno un utente associato.
    // Usato da AdminHomeController per costruire la dashboard.
    @Transactional(readOnly = true)
    public List<Credentials> getAllNonAdmin() {
        List<Credentials> result = new ArrayList<>();
        for (Credentials creds : this.credentialsRepository.findAll()) {
            if (!Credentials.ADMIN_ROLE.equals(creds.getRole()) && creds.getUtente() != null) {
                result.add(creds);
            }
        }
        return result;
    }
}
