package local.mrjnk.messenger.controller;

import local.mrjnk.messenger.domain.User;
import local.mrjnk.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Value("${application.use-email-registration}")
    boolean useEmail;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {

        if (!userService.addUser(user)) {
            model.addAttribute("message", "User exists");
            return "login";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model) {
        if (useEmail) {
            boolean isActivated = userService.activateUser(code);
            if (isActivated) {
                model.addAttribute("message", "User successfully activated");
            } else {
                model.addAttribute("message", "Activation code is fault");
            }
        }
        return "login";
    }
}
