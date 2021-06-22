package cvb.capp.business.services;

import cvb.capp.business.models.Appointment;
import cvb.capp.business.models.TestCenter;
import cvb.capp.business.services.dtos.AddressDTO;
import cvb.capp.business.services.dtos.AppointmentDTO;
import cvb.capp.business.services.dtos.PersonDTO;
import cvb.capp.business.services.dtos.TestCenterDTO;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

public interface SecretaryService {

    PersonDTO findPersonByPersonId(int personId);

    boolean updatePersonsDetails(PersonDTO personDTO, int personId);

    boolean removePerson(int personId);

    List<AppointmentDTO> getAppointmentsByDateAndTestCenter(Date date, int testCenterId);

    List<AppointmentDTO> fetchAllAppointmentsByDateAndHourPerTestCenter(Date date, int testCenterId, String time);

    List<TestCenterDTO> fetchAllTestCenters();

    AddressDTO getAddressForPerson(int personId);

    List<PersonDTO> getAllPersons();

    boolean updateAddressDetails(AddressDTO addressDTO, int personId);

    List<AppointmentDTO> fetchAllAppointments();

    List<AppointmentDTO> fetchAllPresentAppointments();

    List<Time> getSlotTimes(int testCenterId);

    List<Map<Time, Integer>> slotsPerTestCenterAvailable(int testCenterId, Date date);

    List<Map<Time, Integer>> slotsPerTestCenterFull(int testCenterId, Date date);

    TestCenter findTestCenterById(int testCenterId);

    List<AppointmentDTO> fetchAppointmentsForPerson(int personId);

    boolean deleteAppointment(int appointmentId);
}
