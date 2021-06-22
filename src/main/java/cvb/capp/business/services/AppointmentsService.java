package cvb.capp.business.services;

import cvb.capp.business.models.Person;
import cvb.capp.business.models.TestCenter;
import cvb.capp.business.services.dtos.AppointmentDTO;
import cvb.capp.business.services.dtos.AppointmentTempDTO;
import cvb.capp.business.services.dtos.TestCenterDTO;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

public interface AppointmentsService {
    List<AppointmentDTO> fetchAllAppointmentsByDatePerTestCenter(Date date, int testCenterId);

    List<TestCenter> fetchAllTestCenters();

    List<TestCenterDTO> fetchAllTestCenterDTOs();

    TestCenter findTestCenterByName(String name);

    TestCenter findTestCenterById(int testCenterId);

    TestCenterDTO findTestCenter(int testCenterId);

    boolean addAppointment(AppointmentDTO appointmentDTO, int tempAppId, int persons_id);

    int addAppointmentTemp(AppointmentTempDTO appointmentTempDTO, int personId, int testCenterId);

    boolean deleteAppointmentTemp(int appointmentTempDTOId);

    AppointmentTempDTO findAppointmentTempById(int appointmentTempDTOId);

    boolean checkForAvailableTimeSlotsPerDay(Date date, int testCenterId);

    boolean checkForDoubleAppointments(Date date, int personId);

    String getUserUsername(int personId);

    String getTestCenterName(int testCenterId);

    List<Map<Time, List<Person>>> slotsPerTestCenterSaved(int testCenterId, Date date);

    List<Map<Time, Integer>> slotsPerTestCenterAvailable(int testCenterId, Date date);

    List<Time> getSlotTimesAvailable(int testCenterId, Date date);

    boolean checkForAppointments(int testCenterId, Date date);
}
