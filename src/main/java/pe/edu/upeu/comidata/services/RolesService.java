package pe.edu.upeu.comidata.services;

import pe.edu.upeu.comidata.dto.ComboBoxOption;
import pe.edu.upeu.comidata.models.RolesDB;

import java.util.List;

public interface RolesService extends CrudGenericoService<RolesDB, Long>{
    List<ComboBoxOption> listarCombobox();
}
