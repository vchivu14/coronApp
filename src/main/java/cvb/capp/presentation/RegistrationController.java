package cvb.capp.presentation;

import cvb.capp.business.services.UserRegistrationService;
import cvb.capp.business.services.UserService;
import cvb.capp.business.services.dtos.PersonRegistrationDTO;
import cvb.capp.business.services.dtos.UserRegistrationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    UserRegistrationService userRegistrationService;

    public RegistrationController(UserRegistrationService userRegistrationService, UserService userService) {
        this.userRegistrationService = userRegistrationService;
    }

    @ModelAttribute("person")
    public PersonRegistrationDTO personRegistrationDTO() {
        return new PersonRegistrationDTO();
    }

    @ModelAttribute("user")
    public UserRegistrationDTO profileRegistrationDTO() {
        return new UserRegistrationDTO();
    }

    @GetMapping("/user")
    public String getRegistrationFormUser(@ModelAttribute UserRegistrationDTO userRegistrationDTO, Model model) {
        model.addAttribute(userRegistrationDTO);
        return "userFormUserRegistration";
    }

    @PostMapping("/user")
    public String registerUser(@ModelAttribute UserRegistrationDTO userRegistrationDTO) {
        int userId = userRegistrationService.addUser(userRegistrationDTO);
        return "redirect:/registration/person/"+userId;
    }

    @GetMapping("/person/{userId}")
    public String getRegistrationFormPerson(@PathVariable("userId") int userId,
                                            @ModelAttribute PersonRegistrationDTO personRegistrationDTO,
                                            Model model) {
        model.addAttribute(personRegistrationDTO);
        model.addAttribute(userId);
        return "userFormPersonRegistration";
    }

    @PostMapping("/person/{userId}")
    public String registerPerson(@PathVariable("userId") int userId,
                                 @ModelAttribute PersonRegistrationDTO personRegistrationDTO,
                                 Model model) {
        userRegistrationService.addPerson(personRegistrationDTO, userId);
        model.addAttribute("success", "Account successfully created");
        return "login";
    }

}
