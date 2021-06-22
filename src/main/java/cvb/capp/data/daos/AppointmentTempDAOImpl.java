package cvb.capp.data.daos;

import cvb.capp.business.models.AppointmentTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class AppointmentTempDAOImpl implements AppointmentTempDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public AppointmentTemp findAppointmentTempById(int appointmentTempId) {
        String sql = "SELECT * FROM appointmentstemp WHERE id = ?";
        RowMapper<AppointmentTemp> rowMapper = new BeanPropertyRowMapper<>(AppointmentTemp.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, appointmentTempId);
    }

    @Override
    public boolean deleteAppointmentTemp(int personId) {
        String sql = "DELETE FROM appointmentstemp WHERE Persons_id = ?";
        return jdbcTemplate.update(sql, personId) >= 0;
    }

    @Override
    public int addAppointmentTemp(AppointmentTemp appointmentTemp) {
        Date date = appointmentTemp.getDate();
        int personId = appointmentTemp.getPersons_id();
        int testCenterId = appointmentTemp.getTestCenters_id();
        String sql = "INSERT INTO appointmentstemp (Persons_id, TestCenters_id, Date) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, String.valueOf(personId));
            ps.setString(2, String.valueOf(testCenterId));
            ps.setString(3, String.valueOf(date));
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }
}
