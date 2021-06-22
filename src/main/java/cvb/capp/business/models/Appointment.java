package cvb.capp.business.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    private int id;
    private Date date;
    private Time time;
    private TestCenter testCenter;
    private int testCenters_id;
    private Person person;
    private int persons_id;
    private String testType;
    private int appointmentsTemp_id;
    private AppointmentTemp appointmentTemp;
}
