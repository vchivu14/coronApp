package cvb.capp.business.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    private String name;

    public static enum Type {
        PCR, RAPID, BLOOD
    }
}
