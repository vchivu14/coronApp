package cvb.capp.business.services;

import cvb.capp.business.models.*;
import cvb.capp.business.services.dtos.AppointmentDTO;
import cvb.capp.business.services.dtos.AppointmentTempDTO;
import cvb.capp.business.services.dtos.TestCenterDTO;
import cvb.capp.data.daos.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class AppointmentsServiceImpl implements AppointmentsService{
    AppointmentDAO appointmentDAO;
    AppointmentTempDAO appointmentTempDAO;
    TestCenterDAO testCenterDAO;
    PersonDAO personDAO;
    UserDAO userDAO;

    public AppointmentsServiceImpl(TestCenterDAO testCenterDAO, AppointmentDAO appointmentDAO,
                                   PersonDAO personDAO, UserDAO userDAO, AppointmentTempDAO appointmentTempDAO) {
        this.testCenterDAO = testCenterDAO;
        this.appointmentDAO = appointmentDAO;
        this.personDAO = personDAO;
        this.userDAO = userDAO;
        this.appointmentTempDAO = appointmentTempDAO;
    }

    @Override
    public List<TestCenter> fetchAllTestCenters() {
        return testCenterDAO.fetchAllTestCenters();
    }

    @Override
    public List<TestCenterDTO> fetchAllTestCenterDTOs() {
        List<TestCenter> testCenters = testCenterDAO.fetchAllTestCenters();
        List<TestCenterDTO> testCenterDTOS = new ArrayList<>();
        for (TestCenter t: testCenters) {
            TestCenterDTO testCenterDTO = findTestCenter(t.getId());
            testCenterDTOS.add(testCenterDTO);
        }
        return testCenterDTOS;
    }

    @Override
    public TestCenter findTestCenterByName(String name) {
        return testCenterDAO.findTestCenterByName(name);
    }

    @Override
    public TestCenter findTestCenterById(int testCenterId) {
        return testCenterDAO.findTestCenterById(testCenterId);
    }

    @Override
    public TestCenterDTO findTestCenter(int testCenterId) {
        TestCenter testCenter = testCenterDAO.findTestCenterById(testCenterId);
        return getTestCenterDTO(testCenter);
    }

    @NotNull
    private TestCenterDTO getTestCenterDTO(TestCenter testCenter) {
        TestCenterDTO testCenterDTO = new TestCenterDTO();
        testCenterDTO.setId(testCenter.getId());
        testCenterDTO.setName(testCenter.getName());
        testCenterDTO.setOperatingMinutes(testCenter.getOperatingMinutes());
        testCenterDTO.setSlotSizeMinutes(testCenter.getSlotSizeMinutes());
        testCenterDTO.setPersonsPerSlot(testCenter.getPersonsPerSlot());
        testCenterDTO.setSlots(testCenter.getSlots());
        testCenterDTO.setCapacity(testCenter.getCapacity());
        testCenterDTO.setAddresses_Id(testCenter.getAddresses_Id());
        return testCenterDTO;
    }

    @Override
    public boolean checkForDoubleAppointments(Date date, int personId) {
        return appointmentDAO.checkForDoubleAppointments(date, personId);
    }

    @Override
    public boolean checkForAvailableTimeSlotsPerDay(Date date, int testCenterId) {
        List<Time> savedSlotListPerTestCenter = getSlotTimesAvailable(testCenterId, date);
        return savedSlotListPerTestCenter.size() > 0;
    }

    @Override
    public boolean addAppointment(AppointmentDTO appointmentDTO, int tempAppId, int persons_id) {
        AppointmentTempDTO appointmentTempDTO = findAppointmentTempById(tempAppId);
        Appointment appointment = new Appointment();
        appointment.setDate(appointmentTempDTO.getDate());
        appointment.setTestCenters_id(appointmentTempDTO.getTestCenterId());
        appointment.setTestType(appointmentDTO.getTestType());
        String time = appointmentDTO.getTime();
        Time times = Time.valueOf(time);
        appointment.setTime(times);
        return appointmentDAO.addAppointment(appointment, persons_id);
    }

    @Override
    public int addAppointmentTemp(AppointmentTempDTO appointmentTempDTO, int personId, int testCenterId) {
        AppointmentTemp appointmentTemp = new AppointmentTemp();
        appointmentTemp.setDate(appointmentTempDTO.getDate());
        appointmentTemp.setTestCenters_id(appointmentTempDTO.getTestCenterId());
        appointmentTemp.setPersons_id(appointmentTempDTO.getPersonId());
        return appointmentTempDAO.addAppointmentTemp(appointmentTemp);
    }

    @Override
    public boolean deleteAppointmentTemp(int appointmentTempDTOId) {
        return appointmentTempDAO.deleteAppointmentTemp(appointmentTempDTOId);
    }

    @Override
    public AppointmentTempDTO findAppointmentTempById(int appointmentTempDTOId) {
        AppointmentTemp appointmentTemp = appointmentTempDAO.findAppointmentTempById(appointmentTempDTOId);
        AppointmentTempDTO appointmentTempDTO = new AppointmentTempDTO();
        appointmentTempDTO.setId(appointmentTemp.getId());
        appointmentTempDTO.setDate(appointmentTemp.getDate());
        appointmentTempDTO.setTestCenterId(appointmentTemp.getTestCenters_id());
        appointmentTempDTO.setPersonId(appointmentTemp.getPersons_id());
        return appointmentTempDTO;
    }

    @Override
    public String getUserUsername(int personId) {
        Person person = personDAO.findPersonByPersonId(personId);
        int userId = person.getUsers_id();
        User user = userDAO.findUserByUserId(userId);
        return user.getUsername();
    }

    @Override
    public String getTestCenterName(int testCenterId) {
        TestCenter testCenter = testCenterDAO.findTestCenterById(testCenterId);
        return testCenter.getName();
    }

    private List<Time> getSlotTimes(int testCenterId) {
        TestCenter testCenter = testCenterDAO.findTestCenterById(testCenterId);
        int slots = testCenter.getSlots();
        int slotSizeMinutes = testCenter.getSlotSizeMinutes();
        Time openingTime = testCenter.getOpeningTime();

        LocalTime localTimeChange = openingTime.toLocalTime();
        Time time = Time.valueOf(localTimeChange);
        ArrayList<Time> times = new ArrayList<>();
        for(int i = 0; i < slots; i++) {
            times.add(time);
            localTimeChange = localTimeChange.plusMinutes(slotSizeMinutes);
            time = Time.valueOf(localTimeChange);
        }
        return times;
    }

    @Override
    public List<Map<Time, List<Person>>> slotsPerTestCenterSaved(int testCenterId, Date date) {

        List<Time> times = getSlotTimes(testCenterId);

        List<Map<Time, List<Person>>> savedSlotListPerTestCenter = new ArrayList<>();

        List<AppointmentDTO> appointmentList = fetchAllAppointmentsByDatePerTestCenter(date, testCenterId);
        for (Time t : times) {
            Map<Time, List<Person>> oneTimeSlot = new HashMap<>();
            ArrayList<Person> listPersonsPerSlot = new ArrayList<>();
            for (AppointmentDTO app: appointmentList) {
                if (app.getTime().equals(t.toString())) {
                    Person person = personDAO.findPersonByPersonId(app.getPersons_id());
                    listPersonsPerSlot.add(person);
                }
            }
            if (listPersonsPerSlot.size() > 0) {
                oneTimeSlot.put(t,listPersonsPerSlot);
                savedSlotListPerTestCenter.add(oneTimeSlot);
            }
        }
        return savedSlotListPerTestCenter;
    }

    @Override
    public List<Map<Time, Integer>> slotsPerTestCenterAvailable(int testCenterId, Date date) {
        TestCenter testCenter = testCenterDAO.findTestCenterById(testCenterId);
        int personsPerSlot = testCenter.getPersonsPerSlot();

        List<Map<Time, List<Person>>> savedSlotListPerTestCenter = slotsPerTestCenterSaved(testCenterId, date);

        List<Map<Time, Integer>> availableSlotsListPerTestCenter = new ArrayList<>();

        Map<Time, Integer> oneTimeSlot = new HashMap<>();

        Iterator<Map<Time, List<Person>>> iterator = savedSlotListPerTestCenter.iterator();
        while (iterator.hasNext()) {
            Map<Time, List<Person>> timeListMap = iterator.next();
            Set<Map.Entry<Time, List<Person>>> entrySet = timeListMap.entrySet();
            for(Map.Entry<Time, List<Person>> entry : entrySet) {
                int available = personsPerSlot - entry.getValue().size();
                if (available > 0) {
                    oneTimeSlot.put(entry.getKey(), available);
                }
                availableSlotsListPerTestCenter.add(oneTimeSlot);
            }
        }
        return availableSlotsListPerTestCenter;
    }

    @Override
    public List<AppointmentDTO> fetchAllAppointmentsByDatePerTestCenter(Date date, int testCenterId) {
        List<Appointment> appointments = appointmentDAO.fetchAllAppointmentsByDatePerTestCenter(date, testCenterId);
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();
        for (Appointment app: appointments) {
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setId(app.getId());
            appointmentDTO.setDate(app.getDate());
            String time = app.getTime().toString();
            appointmentDTO.setTime(time);
            appointmentDTO.setTestCenters_id(app.getTestCenters_id());
            appointmentDTO.setPersons_id(app.getPersons_id());
            appointmentDTOS.add(appointmentDTO);
        }
        return appointmentDTOS;
    }

    @Override
    public List<Time> getSlotTimesAvailable(int testCenterId, Date date) {
        TestCenter testCenter = findTestCenterById(testCenterId);
        int personsPerSlot = testCenter.getPersonsPerSlot();
        int slots = testCenter.getSlots();
        Time openingTime = testCenter.getOpeningTime();

        LocalTime localTimeChange = openingTime.toLocalTime();
        Time time = Time.valueOf(localTimeChange);
        ArrayList<Time> times = new ArrayList<>();
        for(int i = 0; i < slots; i++) {
            times.add(time);
            localTimeChange = localTimeChange.plusMinutes(10);
            time = Time.valueOf(localTimeChange);
        }

        List<Appointment> appointmentList = appointmentDAO.fetchAllAppointmentsByDatePerTestCenter(date, testCenterId);

        ArrayList<Time> timesAvailable = new ArrayList<>();
        for (Time t : times) {
            List<Appointment> appointments = getAppointmentsByHour(appointmentList, t);
            if(appointments.size() < personsPerSlot) {
                timesAvailable.add(t);
            }
        }
        return timesAvailable;
    }

    private List<Appointment> getAppointmentsByHour(List<Appointment> appointments, Time time) {
        List<Appointment> appointmentList = new ArrayList<>();
        for (Appointment appointment: appointments) {
            if (appointment.getTime().equals(time)) {
                appointmentList.add(appointment);
            }
        }
        return  appointmentList;
    }

    @Override
    public boolean checkForAppointments(int testCenterId, Date date) {
        List<Appointment> appointments = appointmentDAO.fetchAllAppointmentsByDatePerTestCenter(date, testCenterId);
        TestCenter testCenter = testCenterDAO.findTestCenterById(testCenterId);
        return appointments.size() < testCenter.getCapacity();
    }
}
