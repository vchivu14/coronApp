package cvb.capp.business.services;

import cvb.capp.business.models.*;
import cvb.capp.business.services.dtos.*;
import cvb.capp.data.daos.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    PersonDAO personDAO;
    TestCenterDAO testCenterDAO;
    AppointmentDAO appointmentDAO;
    UserDAO userDAO;
    AddressDAO addressDAO;

    public UserServiceImpl(PersonDAO personDAO, TestCenterDAO testCenterDAO, AppointmentDAO appointmentDAO,
                           UserDAO userDAO, AddressDAO addressDAO) {
        this.personDAO = personDAO;
        this.testCenterDAO = testCenterDAO;
        this.appointmentDAO = appointmentDAO;
        this.userDAO = userDAO;
        this.addressDAO = addressDAO;
    }

    @Override
    public PersonDTO findPersonByPersonId(int personId) {
        Person person = personDAO.findPersonByPersonId(personId);
        return getPersonDTO(person);
    }

    @NotNull
    private PersonDTO getPersonDTO(Person person) {
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
    public PersonDTO findPersonByUserId(int userId) {
        Person person = personDAO.findPersonByUserId(userId);
        return getPersonDTO(person);
    }

    @Override
    public boolean updatePersonsDetails(PersonDTO personDTO, int personId) {
        Person person = new Person();
        person.setFirstName(personDTO.getFirstName());
        person.setMiddleName(personDTO.getMiddleName());
        person.setLastName(personDTO.getLastName());
        person.setDob(personDTO.getDob());
        person.setCPR(personDTO.getCPR());
        return personDAO.updatePersonsDetails(person,personId);
    }

    @Override
    public UserDTO findUserByUserId(int userId) {
        User user = userDAO.findUserByUserId(userId);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        return userDTO;
    }

    @Override
    public UserDTO findUserByUsername(String username) {
        User user = userDAO.findUserByUsername(username);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        return userDTO;
    }

    @Override
    public AddressDTO findAddressById(int addressId) {
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
    public int getPersonIdFromUser(int userId) {
        Person person = personDAO.findPersonByUserId(userId);
        return person.getId();
    }

    @Override
    public List<AppointmentDTO> getAllAppointments(int personId) {
        List<Appointment> appointments = appointmentDAO.fetchAppointmentsForPerson(personId);
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();
        for (Appointment app: appointments) {
            int testCenterId = app.getTestCenters_id();
            String testCenterName = testCenterDAO.findTestCenterById(testCenterId).getName();
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setId(app.getId());
            appointmentDTO.setTestType(app.getTestType());
            appointmentDTO.setDate(app.getDate());
            String time = app.getTime().toString();
            appointmentDTO.setTime(time);
            appointmentDTO.setTestCenters_id(app.getTestCenters_id());
            appointmentDTO.setTestCenterName(testCenterName);
            appointmentDTO.setPersons_id(app.getPersons_id());
            appointmentDTOS.add(appointmentDTO);
        }
        return appointmentDTOS;
    }

    @Override
    public TestCenterDTO findTestCenterById(int testCenterId) {
        TestCenter testCenter = testCenterDAO.findTestCenterById(testCenterId);
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
    public void updateResetPasswordToken(String token, String email) {
        User user = userDAO.findByEmail(email);
       if (user != null) {
           userDAO.savePasswordToken(user.getId(), token);
       } else {
           throw new UsernameNotFoundException("Could not find any user with the email: " + email);
       }

    }

    @Override
    public User getByResetPasswordToken(String token) {
        return userDAO.findByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(int userId, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        userDAO.savePassword(userId, encodedPassword);
        userDAO.setTokenNull(userId);
    }

    @Override
    public boolean deleteAppointment(int personId, int appointmentId) {
        return appointmentDAO.removeAppointmentForPerson(personId, appointmentId);
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
}

