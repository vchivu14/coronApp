package cvb.capp.business.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTempDTO implements Serializable {
    private int id;
    private Date date;
    private int testCenterId;
    private int personId;
}
