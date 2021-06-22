package cvb.capp.business.services;

import cvb.capp.business.models.Address;
import cvb.capp.business.models.Appointment;
import cvb.capp.business.models.Person;
import cvb.capp.business.models.TestCenter;
import cvb.capp.business.services.dtos.AddressDTO;
import cvb.capp.business.services.dtos.AppointmentDTO;
import cvb.capp.business.services.dtos.PersonDTO;
import cvb.capp.business.services.dtos.TestCenterDTO;
import cvb.capp.data.daos.AddressDAO;
import cvb.capp.data.daos.AppointmentDAO;
import cvb.capp.data.daos.PersonDAO;
import cvb.capp.data.daos.TestCenterDAO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class SecretaryServiceImpl implements SecretaryService {
    PersonDAO personDAO;
    AppointmentDAO appointmentDAO;
    TestCenterDAO testCenterDAO;
    AddressDAO addressDAO;
    AppointmentsService appointmentsService;

    public SecretaryServiceImpl(PersonDAO personDAO, AppointmentDAO appointmentDAO, TestCenterDAO testCenterDAO,
                                AddressDAO addressDAO, AppointmentsService appointmentsService) {
        this.personDAO = personDAO;
        this.appointmentDAO = appointmentDAO;
        this.testCenterDAO = testCenterDAO;
        this.addressDAO = addressDAO;
        this.appointmentsService = appointmentsService;
    }

    @Override
    public boolean updatePersonsDetails(PersonDTO personDTO, int personId) {
        Person person = personDAO.findPersonByPersonId(personId);
        person.setFirstName(personDTO.getFirstName());
        person.setMiddleName(personDTO.getMiddleName());
        person.setLastName(personDTO.getLastName());
        person.setDob(personDTO.getDob());
        person.setCPR(personDTO.getCPR());
        return personDAO.updatePersonsDetails(person, personId);
    }

    @Override
    public boolean removePerson(int personId) {
        return personDAO.removePerson(personId);
    }

    @Override
    public List<AppointmentDTO> fetchAllAppointments() {
        List<Appointment> appointments = appointmentDAO.fetchAllAppointments();
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>(appointments.size());
        for (Appointment appointment: appointments) {
            PersonDTO personDTO = findPersonByPersonId(appointment.getPersons_id());
            TestCenter testCenter = testCenterDAO.findTestCenterById(appointment.getTestCenters_id());
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setPersonLastName(personDTO.getLastName());
            appointmentDTO.setTestCenterName(testCenter.getName());
            appointmentDTO.setId(appointment.getId());
            appointmentDTO.setDate(appointment.getDate());
            appointmentDTO.setTime(appointment.getTime().toString());
            appointmentDTO.setTestCenters_id(appointment.getTestCenters_id());
            appointmentDTO.setPersons_id(appointment.getPersons_id());
            appointmentDTO.setTestType(appointment.getTestType());
            appointmentDTOS.add(appointmentDTO);
        }
        return appointmentDTOS;
    }

    @Override
    public List<AppointmentDTO> fetchAllPresentAppointments() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        List<Appointment> appointments = appointmentDAO.fetchAllPresentAppointments(date);
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>(appointments.size());
        for (Appointment appointment: appointments) {
            PersonDTO personDTO = findPersonByPersonId(appointment.getPersons_id());
            TestCenter testCenter = testCenterDAO.findTestCenterById(appointment.getTestCenters_id());
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setPersonLastName(personDTO.getLastName());
            appointmentDTO.setTestCenterName(testCenter.getName());
            appointmentDTO.setId(appointment.getId());
            appointmentDTO.setDate(appointment.getDate());
            appointmentDTO.setTime(appointment.getTime().toString());
            appointmentDTO.setTestCenters_id(appointment.getTestCenters_id());
            appointmentDTO.setPersons_id(appointment.getPersons_id());
            appointmentDTO.setTestType(appointment.getTestType());
            appointmentDTOS.add(appointmentDTO);
        }
        return appointmentDTOS;
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByDateAndTestCenter(Date date, int testCenterId) {
        List<Appointment> appointments = appointmentDAO.fetchAllAppointmentsByDatePerTestCenter(date, testCenterId);
        return getAppointmentDTOS(appointments);
    }

    @Override
    public List<AppointmentDTO> fetchAllAppointmentsByDateAndHourPerTestCenter(Date date, int testCenterId, String time) {
        Time times = Time.valueOf(time);
        List<Appointment> appointments = appointmentDAO.fetchAllAppointmentsByDateAndHourPerTestCenter(date, testCenterId, times);
        return getAppointmentDTOS(appointments);
    }

    @NotNull
    private List<AppointmentDTO> getAppointmentDTOS(List<Appointment> appointments) {
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>(appointments.size());
        for (Appointment app : appointments) {
            PersonDTO personDTO = findPersonByPersonId(app.getPersons_id());
            TestCenter testCenter = testCenterDAO.findTestCenterById(app.getTestCenters_id());
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setId(app.getId());
            appointmentDTO.setDate(app.getDate());
            appointmentDTO.setTime(app.getTime().toString());
            appointmentDTO.setTestCenters_id(app.getTestCenters_id());
            appointmentDTO.setPersons_id(app.getPersons_id());
            appointmentDTO.setTestType(app.getTestType());
            appointmentDTO.setPersonLastName(personDTO.getLastName());
            appointmentDTO.setTestCenterName(testCenter.getName());
            appointmentDTOS.add(appointmentDTO);
        }
        return appointmentDTOS;
    }

    @Override
    public boolean deleteAppointment(int appointmentId) {
        return appointmentDAO.deleteAppointment(appointmentId);
    }

    @Override
    public List<TestCenterDTO> fetchAllTestCenters() {
        List<TestCenter> testCenters = testCenterDAO.fetchAllTestCenters();
        List<TestCenterDTO> testCenterDTOS = new ArrayList<>();
        for (TestCenter t: testCenters) {
            TestCenterDTO testCenterDTO = new TestCenterDTO();
            testCenterDTO.setId(t.getId());
            testCenterDTO.setName(t.getName());
            testCenterDTO.setOperatingMinutes(t.getOperatingMinutes());
            testCenterDTO.setSlotSizeMinutes(t.getSlotSizeMinutes());
            testCenterDTO.setPersonsPerSlot(t.getPersonsPerSlot());
            testCenterDTO.setSlots(t.getSlots());
            testCenterDTO.setCapacity(t.getCapacity());
            testCenterDTO.setAddresses_Id(t.getAddresses_Id());
            testCenterDTOS.add(testCenterDTO);
        }
        return testCenterDTOS;
    }

    @Override
    public AddressDTO getAddressForPerson(int personId) {
        Person person = personDAO.findPersonByPersonId(personId);
        int addressId = person.getAddresses_Id();
        Address address = addressDAO.findAddressById(addressId);
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(address.getId());
        addressDTO.setStreetName(address.getStreetName());
        addressDTO.setStreetNo(address.getStreetNo());
        addressDTO.setApartment(address.getApartment());
        addressDTO.setLocality(address.getLocality());
        addressDTO.setZipcode(address.getZipcode());
        return addressDTO;
    }

    @Override
    public List<PersonDTO> getAllPersons() {
        List<Person> personList = personDAO.fetchAllPersons();
        List<PersonDTO> personDTOList = new ArrayList<>(personList.size());
        for (Person person: personList) {
            PersonDTO personDTO = new PersonDTO();
            personDTO.setPersonId(person.getId());
            personDTO.setFirstName(person.getFirstName());
            personDTO.setMiddleName(person.getMiddleName());
            personDTO.setLastName(person.getLastName());
            personDTO.setDob(person.getDob());
            personDTO.setCPR(person.getCPR());
            personDTO.setAddresses_Id(person.getAddresses_Id());
            personDTOList.add(personDTO);
        }
        return personDTOList;
    }

    @Override
    public PersonDTO findPersonByPersonId(int personId) {
        Person person = personDAO.findPersonByPersonId(personId);
        PersonDTO personDTO = new PersonDTO();
        personDTO.setPersonId(person.getId());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setMiddleName(person.getMiddleName());
        personDTO.setLastName(person.getLastName());
        personDTO.setDob(person.getDob());
        personDTO.setCPR(person.getCPR());
        personDTO.setAddresses_Id(person.getAddresses_Id());
        return personDTO;
    }

    @Override
    public boolean updateAddressDetails(AddressDTO addressDTO, int personId) {
        Person person = personDAO.findPersonByPersonId(personId);
        int addressId = person.getAddresses_Id();
        Address address = addressDAO.findAddressById(addressId);
        address.setStreetName(addressDTO.getStreetName());
        address.setStreetNo(addressDTO.getStreetNo());
        address.setApartment(addressDTO.getApartment());
        address.setLocality(addressDTO.getLocality());
        address.setZipcode(addressDTO.getZipcode());
        return addressDAO.updateAddress(address, addressId);
    }

    @Override
    public List<Time> getSlotTimes(int testCenterId) {
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
    public List<Map<Time, Integer>> slotsPerTestCenterAvailable(int testCenterId, Date date) {
        TestCenter testCenter = testCenterDAO.findTestCenterById(testCenterId);
        int personsPerSlot = testCenter.getPersonsPerSlot();

        List<Map<Time, List<Person>>> savedSlotListPerTestCenter = appointmentsService.slotsPerTestCenterSaved(testCenterId, date);

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
    public List<Map<Time, Integer>> slotsPerTestCenterFull(int testCenterId, Date date) {
        List<Map<Time, Integer>> availableSlotsListPerTestCenter = new ArrayList<>();
        TestCenter testCenter = testCenterDAO.findTestCenterById(testCenterId);
        int personsPerSlot = testCenter.getPersonsPerSlot();

        List<Time> times = getSlotTimes(testCenterId);
        List<Time> timesTaken = new ArrayList<>();
        List<AppointmentDTO> appointmentList = appointmentsService.fetchAllAppointmentsByDatePerTestCenter(date, testCenterId);
        for (AppointmentDTO app: appointmentList) {
            timesTaken.add(Time.valueOf(app.getTime()));
        }
        for (Time t: times) {
            Map<Time, Integer> oneTimeSlot = new HashMap<>();
            if (!timesTaken.contains(t)) {
                oneTimeSlot.put(t,personsPerSlot);
                availableSlotsListPerTestCenter.add(oneTimeSlot);
            }
        }
        return availableSlotsListPerTestCenter;
    }

    @Override
    public TestCenter findTestCenterById(int testCenterId) {
        return testCenterDAO.findTestCenterById(testCenterId);
    }

    @Override
    public List<AppointmentDTO> fetchAppointmentsForPerson(int personId) {
        List<Appointment> appointments = appointmentDAO.fetchAppointmentsForPerson(personId);
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>(appointments.size());
        for (Appointment app: appointments) {
            PersonDTO personDTO = findPersonByPersonId(app.getPersons_id());
            TestCenter testCenter = testCenterDAO.findTestCenterById(app.getTestCenters_id());
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setId(app.getId());
            appointmentDTO.setDate(app.getDate());
            appointmentDTO.setTime(app.getTime().toString());
            appointmentDTO.setTestCenters_id(app.getTestCenters_id());
            appointmentDTO.setPersons_id(app.getPersons_id());
            appointmentDTO.setTestType(app.getTestType());
            appointmentDTO.setPersonLastName(personDTO.getLastName());
            appointmentDTO.setTestCenterName(testCenter.getName());
            appointmentDTOS.add(appointmentDTO);
        }
        return appointmentDTOS;
    }
}

