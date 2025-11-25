package pe.edu.upeu.comidata.services;

import pe.edu.upeu.comidata.dto.ComboBoxOption;
import pe.edu.upeu.comidata.models.CategoriaDB;

import java.util.List;

public interface CategoriasService extends CrudGenericoService<CategoriaDB, Long>{
    List<ComboBoxOption> listarCombobox();
}
