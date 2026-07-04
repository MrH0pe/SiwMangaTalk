package it.uniroma3.siw.manga.controller;

import it.uniroma3.siw.manga.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.manga.model.Credentials;
import it.uniroma3.siw.manga.service.CredentialsService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
	
	private final CredentialsService credentialsService;

    public AuthenticationController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }


	// Mostra il form di registrazione di un nuovo utente
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "authentication/registerUser";
	}

	// Mostra il form di login; Spring Security gestisce l'autenticazione in automatico
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		return "authentication/login";
	}

	// Mostra la pagina di amministrazione; l'accesso è protetto da Spring Security (solo ADMIN)
	@GetMapping("/admin/index")
	public String index() {
		return "admin/index";
	}

	// Processa il form di registrazione: salva l'utente e le credenziali se non ci sono errori di validazione
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user,     //BindingResult è una classe di SpringBoot che gestisce tutti gli errori
			BindingResult userBindingResult, @Valid
			@ModelAttribute("credentials") Credentials credentials,
			BindingResult credentialsBindingResult) {

		if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {   //Se non ci sono errori, salvi utente e credenziali nel DB
			credentials.setUtente(user);
			credentialsService.saveCredentials(credentials);
			return "redirect:/";     // serve per evitare il problema del doppio invio del form.
		}
		return "authentication/registerUser";
	}
}