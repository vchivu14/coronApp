package cvb.capp.business.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO implements Serializable {
    private int personId;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dob;
    private long CPR;
    private int addresses_Id;
}
