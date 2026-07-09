package it.uniroma3.siw.manga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.repository.UtenteRepository;

@Service
public class UserService {

    @Autowired
    private UtenteRepository utenteRepository;

    // Restituisce true se esiste già un utente registrato con questa email.
    // Usato in fase di registrazione: l'email è unique nel DB, quindi senza questo
    // controllo un'email duplicata causerebbe una DataIntegrityViolationException (errore 500).
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return this.utenteRepository.existsByEmail(email);
    }
}
