package cvb.capp.business.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Results {
    @Id
    private int id;
    private short value;
    private boolean conclusive;
    private int appointments_id;
    private int persons_id;
}
