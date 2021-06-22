package cvb.capp.business.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTemp {
    @Id
    private int id;
    private Date date;
    private int testCenters_id;
    private int persons_id;
}
