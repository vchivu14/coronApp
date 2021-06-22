package cvb.capp.business.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PersonRegistrationDTO implements Serializable {
    private String streetName;
    private int streetNo;
    private int apartment;
    private String locality;
    private int zipcode;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dob;
    private long CPR;
    private int userId;
}
