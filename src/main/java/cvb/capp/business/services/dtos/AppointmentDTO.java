package cvb.capp.business.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO implements Serializable {
    private int id;
    private Date date;
    private String time;
    private int testCenters_id;
    private int persons_id;
    private String testType;
    private String personLastName;
    private String testCenterName;
}
