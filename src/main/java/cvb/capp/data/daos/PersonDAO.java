package cvb.capp.data.daos;

import cvb.capp.business.models.Person;

import java.util.List;

public interface PersonDAO {
    List<Person> fetchAllPersons();

    void addPerson(Person person, int addressId, int userId);

    Person findPersonByPersonId(int personId);

    Person findPersonByUserId(int userId);

    boolean removePerson(int personId);

    boolean updatePersonsDetails(Person person, int personId);
}
