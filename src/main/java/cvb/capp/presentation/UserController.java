package cvb.capp.presentation;

import cvb.capp.business.services.UserService;
import cvb.capp.business.services.dtos.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    final
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public String getProfile(@PathVariable("username") String username,
                             Model model) {
        UserDTO userDTO = userService.findUserByUsername(username);
        int userId = userDTO.getUserId();
        PersonDTO personDTO = userService.findPersonByUserId(userId);
        int personId = personDTO.getPersonId();
        int addressId = personDTO.getAddresses_Id();
        AddressDTO addressDTO = userService.findAddressById(addressId);
        model.addAttribute("personId",personId);
        model.addAttribute(addressDTO);
        model.addAttribute(personDTO);
        model.addAttribute(userDTO);
        return "userProfile";
    }

    @GetMapping("/appointments/{personId}/")
    public String showCurrentAppointments(@PathVariable("personId") int personId,
                                          @ModelAttribute AppointmentDTO appointmentDTO,
                                          Model model) {
        model.addAttribute(userService.getAllAppointments(personId));
        return "userAppointments";
    }

    @GetMapping("/appointments/{personId}/delete/{appId}")
    public String deleteAppointment(@PathVariable("personId") int personId,
                                    @PathVariable("appId") int appointmentId) {
        boolean delete = userService.deleteAppointment(personId, appointmentId);
        if (delete) {
            return "redirect:/user/appointments/{personId}/";
        } else {
            return "errorUpdate";
        }
    }

    @GetMapping("/{username}/updatePerson/{personId}")
    public String updatePersonForm(@PathVariable("username") String username,
                                   @PathVariable("personId") int personId,
                                   @ModelAttribute PersonDTO personDTO,
                                   Model model) {
        model.addAttribute(username);
        model.addAttribute(personDTO);
        model.addAttribute(personId);
        return "userUpdatePersonForm";
    }

    @PostMapping("/{username}/updatePerson/{personId}")
    public String updatePerson(@PathVariable("username") String username,
                               @PathVariable("personId") int personId,
                               @ModelAttribute PersonDTO personDTO) {
        boolean update = userService.updatePersonsDetails(personDTO, personId);
        if (update) {
            return "redirect:/"+username;
        } else {
            return "errorUpdate";
        }
    }

    @GetMapping("/{username}/updateAddress/{personId}")
    public String updatePersonsAddressForm(@PathVariable("username") String username,
                                           @PathVariable("personId") int personId,
                                           @ModelAttribute AddressDTO addressDTO,
                                           Model model) {
        model.addAttribute(username);
        model.addAttribute(addressDTO);
        model.addAttribute(personId);
        return "userUpdateAddressForm";
    }

    @PostMapping("/{username}/updateAddress/{personId}")
    public String updatesPersonsAddress(@PathVariable("username") String username,
                                        @PathVariable("personId") int personId,
                                        @ModelAttribute AddressDTO addressDTO) {
        boolean update = userService.updateAddressDetails(addressDTO, personId);
        if (update) {
            return "redirect:/"+username;
        } else {
            return "/errorUpdate";
        }
    }



}
