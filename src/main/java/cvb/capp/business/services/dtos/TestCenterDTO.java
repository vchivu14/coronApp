package cvb.capp.business.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCenterDTO {
    private int id;
    private String name;
    private int operatingMinutes;
    private int slotSizeMinutes;
    private int personsPerSlot;
    private int slots;
    private int capacity;
    private int addresses_Id;
    private String openingTime;
}
