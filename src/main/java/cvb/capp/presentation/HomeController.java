package cvb.capp.presentation;

import cvb.capp.business.services.AdminService;
import cvb.capp.business.services.UserService;
import cvb.capp.business.services.dtos.AddressDTO;
import cvb.capp.business.services.dtos.AppointmentDTO;
import cvb.capp.business.services.dtos.PersonDTO;
import cvb.capp.business.services.dtos.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {
    final
    AdminService adminService;
    final
    UserService userService;

    public HomeController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/{username}")
    public String showUserProfile(@PathVariable("username") String username, Model model) {
        UserDTO userDTO = adminService.findUserByUsername(username);
        switch (username) {
            case "admin":
            case "secretary": {
                model.addAttribute(userDTO);
                break;
            }
            default:
                model.addAttribute(userDTO);
                PersonDTO personDTO = userService.findPersonByUserId(userDTO.getUserId());
                AddressDTO addressDTO = userService.findAddressById(personDTO.getAddresses_Id());
                List<AppointmentDTO> appointmentDTOS = userService.getAllAppointments(personDTO.getPersonId());
                model.addAttribute(appointmentDTOS);
                model.addAttribute(personDTO);
                model.addAttribute(addressDTO);
                break;
        }
        return "/userView";
    }
}
