package cvb.capp.business.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    private int id;
    private String streetName;
    private int streetNo;
    private int apartment;
    private String locality;
    private int zipcode;
}
