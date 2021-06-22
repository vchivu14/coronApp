package cvb.capp.data.daos;

import cvb.capp.business.models.TestCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class TestCenterDAOImpl implements TestCenterDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;
    Connection conn = getMySqlConnection();

    private Connection getMySqlConnection() {
        return this.conn;
    }

    @Override
    public void addTestCenter(TestCenter testCenter, int addressId) {
        String name = testCenter.getName();
        int operatingMinutes = testCenter.getOperatingMinutes();
        int slotSizeMinutes = testCenter.getSlotSizeMinutes();
        int personsPerSlot = testCenter.getPersonsPerSlot();
        int slots = testCenter.getSlots();
        int capacity = testCenter.getCapacity();
        Time openingTime = testCenter.getOpeningTime();
        String sql = "INSERT INTO TestCenters (Name, OperatingMinutes, SlotSizeMinutes, PersonsPerSlot, Slots, Capacity, " +
                "Addresses_id, OpeningTime)"
                + "VALUES(?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, String.valueOf(operatingMinutes));
            ps.setString(3, String.valueOf(slotSizeMinutes));
            ps.setString(4, String.valueOf(personsPerSlot));
            ps.setString(5, String.valueOf(slots));
            ps.setString(6, String.valueOf(capacity));
            ps.setString(7, String.valueOf(addressId));
            ps.setString(8, String.valueOf(openingTime));
            return ps;
        });
    }

    @Override
    public boolean removeTestCenter(int testCenterId) {
        String sql = "DELETE FROM TestCenters WHERE id = ?";
        return jdbcTemplate.update(sql, testCenterId) >= 0;
    }

    @Override
    public boolean updateTestCenter(TestCenter testCenter, int testCenterId) {
        String name = testCenter.getName();
        int operatingMinutes = testCenter.getOperatingMinutes();
        int slotSizeMinutes = testCenter.getSlotSizeMinutes();
        int personsPerSlot = testCenter.getPersonsPerSlot();
        int slots = testCenter.getSlots();
        int capacity = testCenter.getCapacity();
        Time openingTime = testCenter.getOpeningTime();
        String sql = "UPDATE TestCenters SET Name = ?, OperatingMinutes = ?, SlotSizeMinutes = ?, " +
                "PersonsPerSlot = ?, Slots = ?, Capacity = ?, OpeningTime = ? WHERE id = ?";
        return jdbcTemplate.update(sql, name, operatingMinutes, slotSizeMinutes,
                personsPerSlot, slots, capacity, openingTime, testCenterId) >= 0;
    }

    @Override
    public List<TestCenter> fetchAllTestCenters() {
        String sql = "SELECT * FROM TestCenters";
        RowMapper<TestCenter> rowMapper = new BeanPropertyRowMapper<>(TestCenter.class);
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public TestCenter findTestCenterById(int testCenterId) {
        String sql = "SELECT * FROM TestCenters WHERE id = ?";
        RowMapper<TestCenter> rowMapper = new BeanPropertyRowMapper<>(TestCenter.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, testCenterId);
    }

    @Override
    public TestCenter findTestCenterByName(String name) {
        String sql = "SELECT * FROM TestCenters WHERE Name = ?";
        RowMapper<TestCenter> rowMapper = new BeanPropertyRowMapper<>(TestCenter.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }
}
