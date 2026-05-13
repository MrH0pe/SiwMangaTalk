package it.uniroma3.siw.manga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SiwMangaTalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiwMangaTalkApplication.class, args);
	}

	
	/*
	 * Casi d’uso: Abbiamo implementato almeno 6 casi d’uso, come richiesto negli obbiettivi del progetto assegnato.
1)	(DONE) Operazione di inserimento dati di una entità: L’utente registrato può scrivere zero o molti commenti .
2)	(DONE) Operazione di aggiornamento dati di una entità: Aggiornamento di uno o molti commenti, scritti dagli utenti.
3)	(HALF DONE)Operazione di cancellazione di una entità: L’utente può cancellare soltanto i propri commenti scritti nei vari Thread. Mentre l’admin può cancellare qualsiasi commento di qualsiasi utente.
4)	(DONE) Operazione di lettura dati di una entità o più entità:  Una pagina web che contiene tutti i commenti scritti da un singolo utente in molti Thread.
5)	(DONE) Operazione di lettura dati di una entità o più entità: Nella pagina iniziale (/index) l’utente può visualizzare OrderBy voto dato dall’utente ai vari manga. Così facendo la pagina ordina graficamente tutti i manga in ordine ascendente di voto.
6)	Operazione di lettura dati di una entità o più entità: Solo l’admin può vedere per un manga quali utenti hanno commentato, mostrandone gli username e il numero di commenti effettuati.


	 */
}
