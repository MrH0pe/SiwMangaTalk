package it.uniroma3.siw.manga.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.manga.model.Commento;
import it.uniroma3.siw.manga.model.Credentials;
import it.uniroma3.siw.manga.repository.CommentoRepository;


@Service
public class CommentoService {

    @Autowired
    private CommentoRepository commentoRepository;

    /**
     * Restituisce un commento dato il suo id, oppure null se non esiste.
     * Usato nei controller per recuperare il commento prima di operazioni su di esso.
     */
    @Transactional(readOnly = true)
    public Commento findById(Long id) {
        return this.commentoRepository.findById(id).orElse(null);
    }

    /**
     * Restituisce tutti i commenti scritti da un utente, identificato dal suo id.
     * Usato nella pagina "I miei commenti" (/mieiCommenti).
     */
    @Transactional(readOnly = true)
    public List<Commento> findByUtenteId(Long id) {
        return this.commentoRepository.findByUtenteId(id);
    }

    /**
     * Restituisce solo i commenti principali (senza padre) di un manga,
     * con le loro risposte già caricate tramite JOIN FETCH.
     * Il JOIN FETCH è necessario perché risposte è LAZY e open-in-view=false:
     * senza di esso Thymeleaf otterrebbe una LazyInitializationException
     * quando tenta di iterare c.risposte fuori dalla transazione.
     */
    @Transactional(readOnly = true)
    public List<Commento> findTopLevelByMangaId(Long mangaId) {
        return this.commentoRepository.findTopLevelByMangaIdWithRisposte(mangaId);
    }

    /**
     * Salva un commento (nuovo o aggiornato) nel database.
     * Usato sia per nuovi commenti che per modifiche al testo.
     */
    @Transactional
    public void save(Commento commento) {
        this.commentoRepository.save(commento);
    }

    /**
     * Elimina un commento scegliendo automaticamente il metodo in base al ruolo.
     * Se l'utente è ADMIN elimina qualsiasi commento (deleteAsAdmin);
     * altrimenti elimina solo se l'utente è il proprietario (deleteIfOwner).
     * Questa logica di business sta nel service, non nel controller.
     */
    @Transactional
    public void delete(Long commentoId, Credentials creds) {
        if (Credentials.ADMIN_ROLE.equals(creds.getRole())) {
            deleteAsAdmin(commentoId);
        } else {
            deleteIfOwner(commentoId, creds.getUtente().getId());
        }
    }

    /**
     * Elimina un commento senza verificare la proprietà (riservato all'admin).
     *
     * - Se è una risposta: viene rimossa dalla collection del padre;
     *   Hibernate la cancella automaticamente grazie a orphanRemoval=true.
     * - Se è un commento principale: viene cancellato direttamente;
     *   CascadeType.ALL propaga la cancellazione alle risposte.
     */
    @Transactional
    public void deleteAsAdmin(Long commentoId) {
        Commento commento = this.commentoRepository.findById(commentoId).orElse(null);
        if (commento == null) return;

        Commento padre = commento.getCommentoPadre();
        if (padre != null) {
            List<Commento> risposte = padre.getRisposte();
            for (int i = 0; i < risposte.size(); i++) {
                if (risposte.get(i).getId().equals(commentoId)) {
                    risposte.remove(i);
                    break;
                }
            }
            this.commentoRepository.save(padre);
        } else {
            this.commentoRepository.delete(commento);
        }
    }

    /**
     * Elimina un commento solo se l'utente corrente ne è il proprietario.
     * Se il commento non esiste o appartiene a un altro utente, non viene fatto nulla.
     */
    @Transactional
    public void deleteIfOwner(Long commentoId, Long currentUserId) {
        Commento commento = this.commentoRepository.findById(commentoId).orElse(null);
        if (commento == null || commento.getUtente() == null
                || !commento.getUtente().getId().equals(currentUserId)) {
            return;
        }

        Commento padre = commento.getCommentoPadre();
        if (padre != null) {
            List<Commento> risposte = padre.getRisposte();
            for (int i = 0; i < risposte.size(); i++) {
                if (risposte.get(i).getId().equals(commentoId)) {
                    risposte.remove(i);
                    break;
                }
            }
            this.commentoRepository.save(padre);
        } else {
            this.commentoRepository.delete(commento);
        }
    }

    /**
     * Aggiorna il testo di un commento solo se l'utente corrente ne è il proprietario.
     * Restituisce false se: il commento non esiste, l'utente non è il proprietario,
     * o il nuovo testo è vuoto (trim().isEmpty() per compatibilità Java 7+).
     */
    @Transactional
    public boolean updateIfOwner(Long commentoId, Long utenteId, String nuovoTesto) {
        if (nuovoTesto == null || nuovoTesto.trim().isEmpty()) return false;
        Commento commento = this.commentoRepository.findById(commentoId).orElse(null);
        if (commento == null || commento.getUtente() == null
                || !commento.getUtente().getId().equals(utenteId)) {
            return false;
        }
        commento.setTesto(nuovoTesto.trim());
        this.commentoRepository.save(commento);
        return true;
    }
}
