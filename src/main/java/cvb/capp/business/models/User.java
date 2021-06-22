package cvb.capp.business.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private int id;
    private String username;
    private String email;
    private String phone;
    private String password;
    private short enabled;
    private String role;
    private int personId;
    private String reset_Password_Token;
    private Timestamp createdAt;
}
