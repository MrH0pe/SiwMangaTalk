package it.uniroma3.siw.manga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication abilita la configurazione automatica di Spring Boot,
// la scansione dei componenti e la configurazione dei bean.
// È il punto di ingresso dell'intera applicazione.
@SpringBootApplication
public class SiwMangaTalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiwMangaTalkApplication.class, args);
	}


	/*
	 * Casi d'uso implementati nel progetto (almeno 6, come richiesto):
	 *
	 * 1) (DONE) INSERIMENTO: L'utente registrato può scrivere zero o molti commenti su un manga.
	 *
	 * 2) (DONE) AGGIORNAMENTO: L'utente può modificare i propri commenti già pubblicati.
	 *
	 * 3) (DONE) CANCELLAZIONE: L'utente può cancellare soltanto i propri commenti.
	 *          L'admin può cancellare qualsiasi commento di qualsiasi utente.
	 *
	 * 4) (DONE) LETTURA SINGOLA ENTITÀ: Una pagina web che mostra tutti i commenti
	 *          scritti da un singolo utente su vari manga (/mieiCommenti).
	 *
	 * 5) (DONE) LETTURA CON ORDINAMENTO: Nella pagina iniziale (/index) l'utente può
	 *          visualizzare i manga ordinati per voto (ascendente o discendente).
	 *
	 * 6) (DONE) LETTURA ADMIN: Solo l'admin può vedere, per un manga, quali utenti
	 *          hanno commentato, con username e numero di commenti effettuati.
	 */
}
