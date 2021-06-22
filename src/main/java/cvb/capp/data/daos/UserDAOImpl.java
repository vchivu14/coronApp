package cvb.capp.data.daos;

import cvb.capp.business.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDAOImpl implements UserDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int add(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        String phone = user.getPhone();
        String password = user.getPassword();
        String role = user.getRole();
        short enabled = user.getEnabled();
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        String sql = "INSERT INTO Users (Username, Email, Phone, Password, Role, Enabled, CreatedAt) " +
                "VALUES (?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, password);
            ps.setString(5, role);
            ps.setString(6, String.valueOf(enabled));
            ps.setString(7, String.valueOf(createdAt));
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE Username = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, username);
    }

    @Override
    public User findUserByUserId(int userId) {
        String sql = "SELECT * FROM Users WHERE id = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, userId);
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, email);
    }

    @Override
    public User findByResetPasswordToken(String token) {
        String sql = "SELECT * FROM Users WHERE Reset_Password_Token = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, token);
    }

    @Override
    public boolean removeUser(int userId) {
        String sql = "DELETE FROM Users WHERE id = ?";
        return jdbcTemplate.update(sql, userId) >= 0;
    }

    @Override
    public boolean updateUser(User user, int userId) {
        String username = user.getUsername();
        String email = user.getEmail();
        String phone = user.getPhone();
        String sql = "UPDATE Users SET Username = ?, Email = ?, Phone = ? WHERE id = ?";
        return jdbcTemplate.update(sql, username, email, phone, userId) >= 0;
    }

    @Override
    public List<User> fetchAllUsers() {
        String sql = "SELECT * FROM Users";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean disableUser(int userId) {
        String sql = "UPDATE Users SET Enabled = ? WHERE id = ?";
        return jdbcTemplate.update(sql, 0, userId) >= 0;
    }

    @Override
    public boolean enableUser(int userId) {
        String sql = "UPDATE Users SET Enabled = ? WHERE id = ?";
        return jdbcTemplate.update(sql,1, userId) >= 0;
    }

    @Override
    public void savePasswordToken(int userId, String token) {
        String sql = "UPDATE Users SET Reset_Password_Token = ? WHERE id = ?";
        jdbcTemplate.update(sql, token, userId);
    }

    @Override
    public void savePassword(int userId, String newPassword) {
        String sql = "UPDATE Users SET Password = ? WHERE id = ?";
        jdbcTemplate.update(sql, newPassword, userId);
    }

    @Override
    public void setTokenNull(int userId) {
        String sql = "UPDATE Users SET Reset_Password_Token = ? WHERE id = ?";
        jdbcTemplate.update(sql, null, userId);
    }
}
