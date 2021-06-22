package cvb.capp.business.services;

import cvb.capp.business.models.Address;
import cvb.capp.business.models.Person;
import cvb.capp.business.models.User;
import cvb.capp.business.services.dtos.PersonRegistrationDTO;
import cvb.capp.business.services.dtos.UserDTO;
import cvb.capp.business.services.dtos.UserRegistrationDTO;
import cvb.capp.data.daos.AddressDAO;
import cvb.capp.data.daos.PersonDAO;
import cvb.capp.data.daos.UserDAO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {
    private UserDAO userDAO;
    private AddressDAO addressDAO;
    private PersonDAO personDAO;
    private BCryptPasswordEncoder passwordEncoder;

    public UserRegistrationServiceImpl(UserDAO userDAO, AddressDAO addressDAO, PersonDAO personDAO) {
        this.userDAO = userDAO;
        this.addressDAO = addressDAO;
        this.personDAO = personDAO;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public int addUser(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPhone(userRegistrationDTO.getPhone());
        user.setRole("ROLE_USER");
        user.setEnabled((short) 1);
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        return userDAO.add(user);
    }

    @Override
    public void addPerson(PersonRegistrationDTO personRegistrationDTO, int userId) {
        Address address = new Address();
        address.setStreetName(personRegistrationDTO.getStreetName());
        address.setStreetNo(personRegistrationDTO.getStreetNo());
        address.setApartment(personRegistrationDTO.getApartment());
        address.setLocality(personRegistrationDTO.getLocality());
        address.setZipcode(personRegistrationDTO.getZipcode());
        int addressId = addressDAO.addAddress(address);
        Person person = new Person();
        person.setFirstName(personRegistrationDTO.getFirstName());
        person.setMiddleName(personRegistrationDTO.getMiddleName());
        person.setLastName(personRegistrationDTO.getLastName());
        person.setDob(personRegistrationDTO.getDob());
        person.setCPR(personRegistrationDTO.getCPR());
        person.setAddresses_Id(addressId);
        personDAO.addPerson(person, addressId, userId);
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
}
