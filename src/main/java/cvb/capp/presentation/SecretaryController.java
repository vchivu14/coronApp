package cvb.capp.presentation;

import cvb.capp.business.models.TestCenter;
import cvb.capp.business.services.SecretaryService;
import cvb.capp.business.services.dtos.AddressDTO;
import cvb.capp.business.services.dtos.AppointmentDTO;
import cvb.capp.business.services.dtos.PersonDTO;
import cvb.capp.business.services.dtos.TestCenterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/secretary")
public class SecretaryController {
    @Autowired
    SecretaryService secretaryService;

    @ModelAttribute AppointmentDTO appointmentDTO() {
        return new AppointmentDTO();
    }

    @GetMapping("/{username}")
    public String getProfile(@PathVariable("username") String username,
                             Model model) {
        model.addAttribute(username);
        return "secretaryPage";
    }

    @GetMapping("/persons")
    public String showPersons(@ModelAttribute PersonDTO personDTO,
                              Model model) {
        model.addAttribute(personDTO);
        model.addAttribute(secretaryService.getAllPersons());
        return "secretaryPersons";
    }

    @GetMapping("/appointments")
    public String getAppointments(@ModelAttribute AppointmentDTO appointmentDTO,
                                  Model model) {
        model.addAttribute(appointmentDTO);
        model.addAttribute(secretaryService.fetchAllPresentAppointments());
        return "secretaryAppointments";
    }

    @GetMapping("/appointments/{appId}")
    public String removeAppointment(@PathVariable("appId") int appId) {
        boolean delete = secretaryService.deleteAppointment(appId);
        if (delete) {
            return "redirect:/secretary/appointments";
        } else {
            return "errorUpdate";
        }
    }

    @GetMapping("/persons/{personId}")
    public String showPerson(@PathVariable("personId") int personId,
                                @ModelAttribute PersonDTO personDTO,
                                Model model) {
        model.addAttribute(personDTO);
        model.addAttribute(personId);
        model.addAttribute(secretaryService.fetchAppointmentsForPerson(personId));
        model.addAttribute(secretaryService.findPersonByPersonId(personId));
        model.addAttribute("address", secretaryService.getAddressForPerson(personId));
        return "secretaryPersonView";
    }

    @GetMapping("/persons/delete/{personId}")
    public String removePerson(@PathVariable("personId") int personId) {
        boolean delete = secretaryService.removePerson(personId);
        if (delete) {
            return "redirect:/secretary/persons";
        } else {
            return "errorUpdate";
        }
    }

    @GetMapping("/persons/{personId}/appointment/delete/{appId}")
    public String deleteAppointment(@PathVariable("personId") int personId,
                                    @PathVariable("appId") int appId,
                                    Model model) {
        boolean delete = secretaryService.deleteAppointment(appId);
        if (delete) {
            return "redirect:/secretary/persons/"+personId;
        } else {
            return "errorUpdate";
        }
    }

    @GetMapping("/persons/updatePerson/{personId}")
    public String updatePersonForm(@PathVariable("personId") int personId,
                                   @ModelAttribute PersonDTO personDTO,
                                   Model model) {
        model.addAttribute(personDTO);
        model.addAttribute(personId);
        return "secretaryPersonUpdateForm";
    }

    @PostMapping("/persons/updatePerson/{personId}")
    public String updatePerson(@PathVariable("personId") int personId,
                               @ModelAttribute PersonDTO personDTO) {
        boolean update = secretaryService.updatePersonsDetails(personDTO, personId);
        if (update) {
            return "redirect:/secretary/persons/"+personId;
        } else {
            return "errorUpdate";
        }
    }

    @GetMapping("/persons/updateAddress/{personId}")
    public String updatePersonsAddressForm(@PathVariable("personId") int personId,
                                   @ModelAttribute AddressDTO addressDTO,
                                   Model model) {
        model.addAttribute(addressDTO);
        model.addAttribute(personId);
        return "secretaryAddressUpdateForm";
    }

    @PostMapping("/persons/updateAddress/{personId}")
    public String updatesPersonsAddress(@PathVariable("personId") int personId,
                                        @ModelAttribute AddressDTO addressDTO) {
        boolean update = secretaryService.updateAddressDetails(addressDTO, personId);
        if (update) {
            return "redirect:/secretary/persons/"+personId;
        } else {
            return "/errorUpdate";
        }
    }

    @GetMapping("/appointmentsCheckDate")
    public String getAppointmentsFormDate(@ModelAttribute AppointmentDTO appointmentDTO,
                                          Model model) {
        model.addAttribute(secretaryService.fetchAllTestCenters());
        model.addAttribute(appointmentDTO);
        return "secretaryAppointmentCheckDate";
    }

    @GetMapping("/appointmentsCheckSlots")
    public String getAppointmentsFormHour(@ModelAttribute AppointmentDTO appointmentDTO,
                                          Model model) {
        model.addAttribute(secretaryService.fetchAllTestCenters());
        model.addAttribute(appointmentDTO);
        return "secretaryAppointmentCheckSlots";
    }

    @PostMapping("/appointmentsCheckDate")
    public String showAppointmentsByDate(@ModelAttribute AppointmentDTO appointmentDTO,
                                         Model model) {
        Date date = appointmentDTO.getDate();
        int testCenterId = appointmentDTO.getTestCenters_id();
        TestCenter testCenter = secretaryService.findTestCenterById(testCenterId);
        model.addAttribute(date);
        model.addAttribute(testCenter);
        List<AppointmentDTO> appointmentDTOList = secretaryService.getAppointmentsByDateAndTestCenter(date,testCenterId);
        model.addAttribute(appointmentDTOList);
        model.addAttribute(appointmentDTO);
        return "secretaryAppointmentsByDate";
    }

    @PostMapping("/appointmentsCheckSlots")
    public String showAppointmentsByDateAndHour(@ModelAttribute AppointmentDTO appointmentDTO,
                                                Model model) {
        Date date = appointmentDTO.getDate();
        int testCenterId = appointmentDTO.getTestCenters_id();
        List<Map<Time, Integer>> availableSlots = secretaryService.slotsPerTestCenterAvailable(testCenterId, date);
        model.addAttribute("availableSlots", availableSlots);
        List<Map<Time, Integer>> fullSlots = secretaryService.slotsPerTestCenterFull(testCenterId, date);
        model.addAttribute("fullSlots", fullSlots);
        model.addAttribute(date);
        String testCenterName = secretaryService.findTestCenterById(testCenterId).getName();
        model.addAttribute("testCenterName", testCenterName);
        return "secretaryAppointmentsBySlots";
    }

}
