package it.uniroma3.siw.manga.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Credentials;
import it.uniroma3.siw.manga.repository.CredentialsRepository;


@Service
public class CredentialsService {

    //Questo attributo serve per criptare la password
    private PasswordEncoder passwordEncoder;

    //Questo attributo serve per accedere alla tabella Credentials nella base di dati
    private CredentialsRepository credentialsRepository;    

    public CredentialsService(PasswordEncoder passwordEncoder, CredentialsRepository credentialsRepository) {
        this.passwordEncoder = passwordEncoder;
        this.credentialsRepository = credentialsRepository;
    }

    // Restituisce le credenziali associate all'id specificato, null se non trovate
    @Transactional(readOnly = true)
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    // Restituisce le credenziali associate all'username specificato, null se non trovate
    @Transactional(readOnly = true)
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = Optional.ofNullable(this.credentialsRepository.findByUsername(username));
        return result.orElse(null);
    }

    // Salva le credenziali del nuovo utente: assegna il ruolo DEFAULT e cifra la password con BCrypt
    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }
}
