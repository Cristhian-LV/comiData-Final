package pe.edu.upeu.comidata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModeloDataAutocomplet {
    String idx;
    String label;
    String searchKey;

    @Override
    public String toString() {
        return label +" "+idx+" " + searchKey;
    }
}