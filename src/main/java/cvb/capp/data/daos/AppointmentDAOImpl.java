package cvb.capp.data.daos;

import cvb.capp.business.models.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.util.List;

@Repository
public class AppointmentDAOImpl implements AppointmentDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Appointment> fetchAllAppointmentsByDatePerTestCenter(Date date, int testCenterId) {
        String sql = "SELECT * FROM appointments WHERE Date = ? AND TestCenters_id = ?";
        RowMapper<Appointment> rowMapper = new BeanPropertyRowMapper<>(Appointment.class);
        return jdbcTemplate.query(sql, rowMapper, date, testCenterId);
    }

    @Override
    public List<Appointment> fetchAllAppointmentsByDateAndHourPerTestCenter(Date date, int testCenterId, Time time) {
        String sql = "SELECT * FROM appointments WHERE Date = ? AND TestCenters_id = ? AND Time = ?";
        RowMapper<Appointment> rowMapper = new BeanPropertyRowMapper<>(Appointment.class);
        return jdbcTemplate.query(sql, rowMapper, date, testCenterId, time);
    }

    @Override
    public boolean addAppointment(Appointment appointment, int persons_id) {
        Date date = appointment.getDate();
        Time time = appointment.getTime();
        int testCenterId = appointment.getTestCenters_id();
        String testType = appointment.getTestType();
        String sql = "INSERT INTO appointments(Date, Time, TestCenters_id, TestType, Persons_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, String.valueOf(date));
            ps.setString(2, String.valueOf(time));
            ps.setString(3, String.valueOf(testCenterId));
            ps.setString(4, String.valueOf(testType));
            ps.setString(5, String.valueOf(persons_id));
            return ps;
        }) >= 0;
    }

    @Override
    public boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE Id = ?";
        return jdbcTemplate.update(sql, appointmentId) >= 0;

    }

    @Override
    public boolean checkForDoubleAppointments(Date date, int personId) {
        String sql = "SELECT * FROM appointments WHERE Date = ? AND Persons_id = ?";
        RowMapper<Appointment> rowMapper = new BeanPropertyRowMapper<>(Appointment.class);
        List<Appointment> appointments = jdbcTemplate.query(sql,rowMapper,date,personId);
        return appointments.size() > 0;
    }

    @Override
    public List<Appointment> fetchAppointmentsForPerson(int personId) {
        String sql = "SELECT * FROM appointments WHERE Persons_id = ?";
        RowMapper<Appointment> rowMapper = new BeanPropertyRowMapper<>(Appointment.class);
        return jdbcTemplate.query(sql, rowMapper, personId);
    }

    @Override
    public List<Appointment> fetchAllAppointments() {
        String sql = "SELECT * FROM appointments";
        RowMapper<Appointment> rowMapper = new BeanPropertyRowMapper<>(Appointment.class);
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Appointment> fetchAllPresentAppointments(Date date) {
        String sql = "SELECT * FROM appointments WHERE Date = ?";
        RowMapper<Appointment> rowMapper = new BeanPropertyRowMapper<>(Appointment.class);
        return jdbcTemplate.query(sql, rowMapper, date);
    }

    @Override
    public boolean removeAppointmentForPerson(int personId, int appointmentId) {
        String sql = "DELETE FROM appointments WHERE Id = ? AND Persons_id = ?";
        return jdbcTemplate.update(sql, appointmentId, personId) >= 0;
    }
}
