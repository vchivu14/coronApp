package cvb.capp.business.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCenter {
    @Id
    private int id;
    private String name;
    private int operatingMinutes;
    private int slotSizeMinutes;
    private int personsPerSlot;
    private int slots;
    private int capacity;
    private int addresses_Id;
    private Time openingTime;
}
