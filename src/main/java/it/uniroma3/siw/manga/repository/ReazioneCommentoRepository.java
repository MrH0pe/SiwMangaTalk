package it.uniroma3.siw.manga.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.manga.model.ReazioneCommento;

/**
 * Repository JPA per l'entità ReazioneCommento.
 *
 * Spring Data JPA genera automaticamente le implementazioni SQL
 * sulla base dei nomi dichiarati nell'interfaccia (query by method name).
 *
 * È collegato a: ReazioneCommentoService (che usa questo repository per tutte le operazioni DB)
 */
public interface ReazioneCommentoRepository extends CrudRepository<ReazioneCommento, Long> {

    /**
     * Cerca la reazione di un utente su uno specifico commento.
     * Restituisce Optional vuoto se l'utente non ha ancora reagito a quel commento.
     * Usato da ReazioneCommentoService per la logica di toggle (aggiungi/cambia/rimuovi).
     */
    Optional<ReazioneCommento> findByUtenteIdAndCommentoId(Long utenteId, Long commentoId);

    /**
     * Restituisce tutte le reazioni sui commenti di un manga.
     * Usato da ReazioneCommentoService per calcolare i contatori like/dislike
     * di tutti i commenti di una pagina manga in una singola query.
     */
    List<ReazioneCommento> findByCommentoMangaId(Long mangaId);

    /**
     * Restituisce le reazioni di un utente su tutti i commenti di un manga.
     * Usato per sapere cosa ha già votato l'utente nella pagina del manga
     * (evidenzia il pulsante like o dislike già cliccato).
     */
    List<ReazioneCommento> findByUtenteIdAndCommentoMangaId(Long utenteId, Long mangaId);

    /**
     * Conta le reazioni di un tipo specifico ("LIKE" o "DISLIKE") su un singolo commento.
     * Usato dagli endpoint REST per restituire i contatori aggiornati senza ricaricare la pagina.
     */
    long countByCommentoIdAndTipo(Long commentoId, String tipo);
}
