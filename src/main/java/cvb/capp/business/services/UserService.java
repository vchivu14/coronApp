package cvb.capp.business.services;

import cvb.capp.business.models.User;
import cvb.capp.business.services.dtos.*;

import java.util.List;

public interface UserService {
    PersonDTO findPersonByPersonId(int personId);

    PersonDTO findPersonByUserId(int userId);

    boolean updatePersonsDetails(PersonDTO personDTO, int personId);

    UserDTO findUserByUserId(int userId);

    UserDTO findUserByUsername(String username);

    AddressDTO findAddressById(int addressId);

    int getPersonIdFromUser(int userId);

    List<AppointmentDTO> getAllAppointments(int personId);

    TestCenterDTO findTestCenterById(int testCenterId);

    void updateResetPasswordToken(String token, String email);

    User getByResetPasswordToken(String token);

    void updatePassword(int userId, String newPassword);

    boolean deleteAppointment(int personId, int appointmentId);

    boolean updateAddressDetails(AddressDTO addressDTO, int personId);
}
