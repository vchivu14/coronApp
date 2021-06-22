package cvb.capp.presentation;

import cvb.capp.business.models.Test;
import cvb.capp.business.services.AppointmentsService;
import cvb.capp.business.services.dtos.AppointmentDTO;
import cvb.capp.business.services.dtos.AppointmentTempDTO;
import cvb.capp.business.services.dtos.TestCenterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Controller
@RequestMapping("/{personId}/appointments")
public class AppointmentsController {
    @Autowired
    AppointmentsService appointmentsService;

    @ModelAttribute("appointmentTempDTO")
    public AppointmentTempDTO appointmentTempDTO() {
        return new AppointmentTempDTO();
    }

    @ModelAttribute("appointmentDTO")
    public AppointmentDTO appointmentDTO() {
        return new AppointmentDTO();
    }

    @GetMapping("/check")
    public String checkForAppointment(@PathVariable("personId") int personId,
                                      @ModelAttribute AppointmentTempDTO appointmentTempDTO,
                                      @ModelAttribute TestCenterDTO testCenterDTO,
                                      Model model) {
        List<TestCenterDTO> testCenterDTOS = appointmentsService.fetchAllTestCenterDTOs();
        model.addAttribute(personId);
        model.addAttribute("testCenterDTOList", testCenterDTOS);
        return "userFormAppointmentCheck";
    }

    @PostMapping("/check")
    public String getAppointmentCheck(@PathVariable("personId") int personId,
                                      @ModelAttribute AppointmentTempDTO appointmentTempDTO,
                                      Model model) {
        Date dateRequested = appointmentTempDTO.getDate();
        int testCenterId = appointmentTempDTO.getTestCenterId();
        boolean checkForDoubleAppointments = appointmentsService.checkForDoubleAppointments
                (dateRequested, personId);
        long startTime = System.currentTimeMillis();
        boolean checkForAvailableTimeSlots = appointmentsService.checkForAppointments(testCenterId, dateRequested);
        long stopTime = System.currentTimeMillis();
        System.out.println(stopTime - startTime);
        if (checkForDoubleAppointments) {
            model.addAttribute(personId);
            return "errorDuplicate";
        } else if (!checkForAvailableTimeSlots) {
            model.addAttribute(personId);
            return "errorFull";
        } else {
            int appointmentTempId = appointmentsService.addAppointmentTemp(appointmentTempDTO, personId, testCenterId);
            model.addAttribute(personId);
            return "redirect:/{personId}/appointments/registration/"+ appointmentTempId;
        }
    }

        @GetMapping("/registration/{tempApp}")
        public String getAppointmentRegistrationForm(@PathVariable("personId") int personId,
                                                     @PathVariable("tempApp") int tempApp,
                                                     @ModelAttribute Test test,
                                                     @ModelAttribute AppointmentDTO appointmentDTO,
                                                     Model model) {
            AppointmentTempDTO appointmentTempDTO = appointmentsService.findAppointmentTempById(tempApp);
            model.addAttribute(appointmentTempDTO);
            Date dateRequested = appointmentTempDTO.getDate();
            int testCenterId = appointmentTempDTO.getTestCenterId();
            long startTime = System.currentTimeMillis();
            List<Time> availableTimes = appointmentsService.getSlotTimesAvailable(testCenterId, dateRequested);
            long stopTime = System.currentTimeMillis();
            System.out.println(stopTime - startTime);
            model.addAttribute("availableTimes", availableTimes);
            TestCenterDTO testCenterDTO = appointmentsService.findTestCenter(testCenterId);
            model.addAttribute(testCenterDTO);
            Test.Type[] types = Test.Type.values();
            model.addAttribute(types);
            model.addAttribute(test);
            model.addAttribute(appointmentDTO);
            model.addAttribute(personId);
            return "userFormAppointmentRegistration";
        }

        @PostMapping("/registration/{tempApp}")
        public String completeAppointmentRegistration(@PathVariable ("personId") int personId,
                                                      @PathVariable("tempApp") int tempApp,
                                                      @ModelAttribute Test test,
                                                      @ModelAttribute AppointmentDTO appointmentDTO,
                                                      Model model) {
            Date dateRequested = appointmentDTO.getDate();
            boolean checkForDoubleAppointments = appointmentsService.checkForDoubleAppointments
                    (dateRequested,personId);
            model.addAttribute(personId);
            if (checkForDoubleAppointments) {
                return "errorDuplicate";
            } else {
                String username = appointmentsService.getUserUsername(personId);
                model.addAttribute("username", username);
                boolean appCompleted = appointmentsService.addAppointment(appointmentDTO, tempApp, personId);
                if (appCompleted) {
                    // send a message
                    boolean appTempDeleted = appointmentsService.deleteAppointmentTemp(tempApp);
                    if (appTempDeleted) {
                        System.out.println("Temp Appointments have been deleted from database");
                    }
                }
                return "redirect:/user/appointments/{personId}/";
            }
        }

}
