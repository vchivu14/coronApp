package cvb.capp.business.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.sql.Date;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dob;
    private long CPR;
    private int addresses_Id;
    private int users_id;
    private Address address;
    private Collection<Appointment> appointments;
}
