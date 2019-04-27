package local.mrjnk.messenger.controller;

import local.mrjnk.messenger.domain.Role;
import local.mrjnk.messenger.domain.User;
import local.mrjnk.messenger.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.HashSet;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User userFromDB = userRepo.findByUsername(user.getUsername());

        if (userFromDB != null) {
            model.addAttribute("message", "User exists");
            return "login";
        }
        user.setActive(true);
        user.setRoles(new HashSet<>(Arrays.asList(Role.ADMIN)));
        userRepo.save(user);

        return "redirect:/login";
    }
}
