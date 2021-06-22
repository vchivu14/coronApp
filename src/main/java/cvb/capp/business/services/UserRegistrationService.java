package cvb.capp.business.services;

import cvb.capp.business.services.dtos.PersonRegistrationDTO;
import cvb.capp.business.services.dtos.UserDTO;
import cvb.capp.business.services.dtos.UserRegistrationDTO;

public interface UserRegistrationService {
    int addUser(UserRegistrationDTO userRegistrationDTO);

    void addPerson(PersonRegistrationDTO personRegistrationDTO, int userId);

    UserDTO findUserByUserId(int userId);
}
