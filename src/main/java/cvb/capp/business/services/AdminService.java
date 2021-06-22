package cvb.capp.business.services;

import cvb.capp.business.models.*;
import cvb.capp.business.services.dtos.AddressDTO;
import cvb.capp.business.services.dtos.TestCenterDTO;
import cvb.capp.business.services.dtos.UserDTO;

import java.sql.Date;
import java.util.List;

public interface AdminService {
    int addAddress(Address address);

    void addTestCenter(TestCenterDTO testCenterDTO, int addressId);

    boolean removeTestCenter(int testCenterId);

    boolean updateTestCenter(TestCenterDTO testCenterDTO, int testCenterId);

    boolean updateAddressForTestCenter(AddressDTO addressDTO, int testCenterId);

    List<TestCenterDTO> fetchAllTestCenters();

    TestCenterDTO findTestCenterById(int testCenterId);

    List<Appointment> fetchAllAppointmentsByDatePerTestCenter(Date date, int testCenterId);

    List<User> fetchAllUsers();

    UserDTO findUserByUsername(String username);

    UserDTO findUserByUserId(int userId);

    boolean removeUser(int userId);

    boolean updateUser(UserDTO userDTO, int userId);

    boolean disableUser(int userId);

    boolean enableUser(int userId);

    AddressDTO getAddressForTestCenter(int testCenterId);
}
