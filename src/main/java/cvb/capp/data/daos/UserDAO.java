package cvb.capp.data.daos;

import cvb.capp.business.models.User;

import java.util.List;

public interface UserDAO {
    int add(User user);

    User findUserByUsername(String username);

    User findUserByUserId(int userId);

    User findByEmail(String email);

    User findByResetPasswordToken(String token);

    boolean removeUser(int userId);

    boolean updateUser(User user, int userId);

    List<User> fetchAllUsers();

    boolean disableUser(int userId);

    boolean enableUser(int userId);

    void savePasswordToken(int userId, String token);

    void savePassword(int userId, String newPassword);

    void setTokenNull(int userId);
}
