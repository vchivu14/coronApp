package cvb.capp.business.services;

import cvb.capp.business.models.*;
import cvb.capp.business.services.dtos.AddressDTO;
import cvb.capp.business.services.dtos.TestCenterDTO;
import cvb.capp.business.services.dtos.UserDTO;
import cvb.capp.data.daos.*;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    PersonDAO personDAO;
    UserDAO userDAO;
    TestCenterDAO testCenterDAO;
    AddressDAO addressDAO;
    AppointmentDAO appointmentDAO;

    public AdminServiceImpl(PersonDAO personDAO, UserDAO userDAO, TestCenterDAO testCenterDAO,
                            AddressDAO addressDAO, AppointmentDAO appointmentDAO) {
        this.personDAO = personDAO;
        this.userDAO = userDAO;
        this.testCenterDAO = testCenterDAO;
        this.addressDAO = addressDAO;
        this.appointmentDAO = appointmentDAO;
    }

    @Override
    public int addAddress(Address address) {
        return addressDAO.addAddress(address);
    }

    @Override
    public AddressDTO getAddressForTestCenter(int testCenterId) {
        TestCenter testCenter = testCenterDAO.findTestCenterById(testCenterId);
        int addressId = testCenter.getAddresses_Id();
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
    public void addTestCenter(TestCenterDTO testCenterDTO, int addressId) {
        TestCenter testCenter = new TestCenter();
        testCenter.setName(testCenterDTO.getName());
        int operatingMinutes = testCenterDTO.getOperatingMinutes() * 60;
        testCenter.setOperatingMinutes(operatingMinutes);
        int slotSizeMinutes = testCenterDTO.getSlotSizeMinutes();
        testCenter.setSlotSizeMinutes(slotSizeMinutes);
        int personsPerSlot = testCenterDTO.getPersonsPerSlot();
        testCenter.setPersonsPerSlot(personsPerSlot);
        int slots = (operatingMinutes / slotSizeMinutes);
        testCenter.setSlots(slots);
        testCenter.setCapacity(personsPerSlot * slots);
        String timeString = testCenterDTO.getOpeningTime().concat(":00");
        Time openingTime = Time.valueOf(timeString);
        LocalTime localTime = openingTime.toLocalTime();
        Time times = Time.valueOf(localTime);
        testCenter.setOpeningTime(times);
        testCenterDAO.addTestCenter(testCenter, addressId);
    }

    @Override
    public boolean removeTestCenter(int testCenterId) {
        return testCenterDAO.removeTestCenter(testCenterId);
    }

    @Override
    public boolean updateTestCenter(TestCenterDTO testCenterDTO, int testCenterId) {
        TestCenter testCenter = new TestCenter();
        testCenter.setName(testCenterDTO.getName());
        int operatingMinutes = testCenterDTO.getOperatingMinutes() * 60;
        testCenter.setOperatingMinutes(operatingMinutes);
        int slotSizeMinutes = testCenterDTO.getSlotSizeMinutes();
        testCenter.setSlotSizeMinutes(slotSizeMinutes);
        int personsPerSlot = testCenterDTO.getPersonsPerSlot();
        testCenter.setPersonsPerSlot(personsPerSlot);
        int slots = (operatingMinutes / slotSizeMinutes);
        testCenter.setSlots(slots);
        testCenter.setCapacity(personsPerSlot * slots);
        String timeString = testCenterDTO.getOpeningTime().concat(":00");
        Time openingTime = Time.valueOf(timeString);
        LocalTime localTime = openingTime.toLocalTime();
        Time times = Time.valueOf(localTime);
        testCenter.setOpeningTime(times);
        return testCenterDAO.updateTestCenter(testCenter, testCenterId);
    }

    @Override
    public boolean updateAddressForTestCenter(AddressDTO addressDTO, int testCenterId) {
        TestCenter testCenter = testCenterDAO.findTestCenterById(testCenterId);
        int addressId = testCenter.getAddresses_Id();
        Address address = addressDAO.findAddressById(addressId);
        address.setStreetName(addressDTO.getStreetName());
        address.setStreetNo(addressDTO.getStreetNo());
        address.setApartment(addressDTO.getApartment());
        address.setLocality(addressDTO.getLocality());
        address.setZipcode(addressDTO.getZipcode());
        return addressDAO.updateAddress(address, addressId);
    }

    @Override
    public List<TestCenterDTO> fetchAllTestCenters() {
        List<TestCenter> testCenters = testCenterDAO.fetchAllTestCenters();
        List<TestCenterDTO> testCenterDTOS = new ArrayList<>(testCenters.size());
        for (TestCenter t: testCenters) {
            TestCenterDTO testCenterDTO = new TestCenterDTO();
            testCenterDTO.setId(t.getId());
            testCenterDTO.setName(t.getName());
            testCenterDTO.setOperatingMinutes(t.getOperatingMinutes());
            testCenterDTO.setSlotSizeMinutes(t.getSlotSizeMinutes());
            testCenterDTO.setSlots(t.getSlots());
            testCenterDTO.setCapacity(t.getCapacity());
            testCenterDTO.setAddresses_Id(t.getAddresses_Id());
            testCenterDTO.setOpeningTime(String.valueOf(t.getOpeningTime()));
            testCenterDTOS.add(testCenterDTO);
        }
        return testCenterDTOS;
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
        testCenterDTO.setOpeningTime(String.valueOf(testCenter.getOpeningTime()));
        return testCenterDTO;
    }

    @Override
    public List<Appointment> fetchAllAppointmentsByDatePerTestCenter(Date date, int testCenterId) {
        return appointmentDAO.fetchAllAppointmentsByDatePerTestCenter(date, testCenterId);
    }

    @Override
    public List<User> fetchAllUsers() {
        return userDAO.fetchAllUsers();
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
    public UserDTO findUserByUserId(int userId) {
        User user = userDAO.findUserByUserId(userId);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setCreatedAt(user.getCreatedAt());
        return userDTO;
    }

    @Override
    public boolean removeUser(int userId) {
        return userDAO.removeUser(userId);
    }

    @Override
    public boolean updateUser(UserDTO userDTO, int userId) {
        User user = userDAO.findUserByUserId(userId);
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        return userDAO.updateUser(user, userId);
    }

    @Override
    public boolean disableUser(int userId) {
        return userDAO.disableUser(userId);
    }

    @Override
    public boolean enableUser(int userId) {
        return userDAO.enableUser(userId);
    }


}
