package it.uniroma3.siw.manga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.manga.model.Credentials;
import it.uniroma3.siw.manga.model.User;
import it.uniroma3.siw.manga.service.CredentialsService;
import it.uniroma3.siw.manga.service.UserService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

	private final CredentialsService credentialsService;
	private final UserService userService;

    public AuthenticationController(CredentialsService credentialsService, UserService userService) {
        this.credentialsService = credentialsService;
        this.userService = userService;
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

        if (credentialsService.existsByUsername(credentials.getUsername())) {
            credentialsBindingResult.rejectValue("username", "error.credentials", "Username già preso");
        }

        // L'email è unique nel DB: senza questo controllo un duplicato causerebbe un errore 500
        if (userService.existsByEmail(user.getEmail())) {
            userBindingResult.rejectValue("email", "error.user", "Email già registrata");
        }

        if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            credentials.setUtente(user);
            credentialsService.saveCredentials(credentials);
            return "redirect:/";
        }
        return "authentication/registerUser";
    }

}