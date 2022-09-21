package com.example.securitystart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {
    //Lägg till loggning i programmet genom att lägga till en logger som en instansvariabel i klassen LoginController. Använd den här raden för att skapa en logger som en förekomstvariabel i kontrollenheten (inom klassen men utanför någon metod):
    Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    AdminRepository adminRepository;

    @GetMapping("/")
    public String start() {
        logger.error("Logging an ERROR message");
        logger.warn("Logging a WARN message");
        logger.info("Logging an INFO message");
        logger.debug("Logging a DEBUG message");
        logger.trace("Logging a TRACE message");
        return "start";
    }

    @PostMapping("/login")
    public String login(HttpSession session, @RequestParam String username, @RequestParam String password) {
        List<Admin> users = adminRepository.findByUsername(username);
        if (users.size() > 0 && users.get(0).getPassword().equals(password)) {
            session.setAttribute("user", users.get(0));

            return "redirect:/secret";
        }
        return "redirect:/";
    }

    @PostMapping("/user")
    public String user(HttpSession session, @RequestParam String username, @RequestParam String password) {
        Admin admin = new Admin(username, password, 100);
        adminRepository.save(admin);
        return "start";
    }

    @GetMapping("/secret")
    public String level1(HttpSession session) {
        Admin user = (Admin) session.getAttribute("user");
        if (user != null) {
            return "secret";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // you could also invalidate the whole session, a new session will be created the next request
        return "redirect:/";
    }

    //Uppgift 3 - Förfalskning av begäran över flera webbplatser (CSRF)
//Men försök ändå ändra metoden för formuläret till POST och ändra GetMapping-anteckningen för överföringsmetoden till PostMapping istället. Utan någon metod som hanterar en GET-begäran till den förutsägbara URL:en kunde en img-tagg inte användas för den här attacken. Efter att ha ändrat till POST, försök att köra programmet, titta på kommentarsidan.html i en annan flik och se om du förlorar pengar eller om du bara gjorde applikationen lite säkrare!
    @PostMapping("/transfer")
    public String transfer(HttpSession session, @RequestParam int amount, @RequestParam String account) {
        Admin user = (Admin) session.getAttribute("user");
        if (user != null && amount > 0) {
            user.setSavings(user.getSavings() - amount);
            adminRepository.save(user);
        }

        return "redirect:/secret";
    }
}
