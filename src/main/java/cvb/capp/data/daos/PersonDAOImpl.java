package cvb.capp.data.daos;

import cvb.capp.business.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class PersonDAOImpl implements PersonDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void addPerson(Person person, int addressId, int userId) {
        String firstName = person.getFirstName();
        String middleName = person.getMiddleName();
        String lastName = person.getLastName();
        Date DOB = person.getDob();
        long CPR = person.getCPR();
        String sql = "INSERT INTO Persons (FirstName, MiddleName, LastName, DOB, CPR, Addresses_id, Users_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, firstName);
            ps.setString(2, middleName);
            ps.setString(3, lastName);
            ps.setString(4, String.valueOf(DOB));
            ps.setString(5, String.valueOf(CPR));
            ps.setString(6, String.valueOf(addressId));
            ps.setString(7, String.valueOf(userId));
            return ps;
        });
    }

    @Override
    public Person findPersonByPersonId(int personId) {
        String sql = "SELECT * FROM Persons WHERE id = ?";
        RowMapper<Person> rowMapper = new BeanPropertyRowMapper<>(Person.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, personId);
    }

    @Override
    public Person findPersonByUserId(int userId) {
        String sql = "SELECT * FROM Persons WHERE Users_id = ?";
        RowMapper<Person> rowMapper = new BeanPropertyRowMapper<>(Person.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, userId);
    }

    @Override
    public boolean removePerson(int personId) {
        String sql = "DELETE FROM Persons WHERE id = ?";
        return jdbcTemplate.update(sql, personId) >= 0;
    }

    @Override
    public boolean updatePersonsDetails(Person person, int personId) {
        String firstName = person.getFirstName();
        String middleName = person.getMiddleName();
        String lastName = person.getLastName();
        Date DOB = person.getDob();
        long CPR = person.getCPR();
        String sql = "UPDATE Persons SET Firstname = ?, MiddleName = ?, LastName = ?, " +
                "DOB = ?, CPR = ? WHERE id = ?";
        return jdbcTemplate.update(sql, firstName, middleName, lastName, DOB, CPR, personId) >= 0;
    }

    @Override
    public List<Person> fetchAllPersons() {
        String sql = "SELECT * FROM Persons";
        RowMapper<Person> rowMapper = new BeanPropertyRowMapper<>(Person.class);
        return jdbcTemplate.query(sql, rowMapper);
    }

}
