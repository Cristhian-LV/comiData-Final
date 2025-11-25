package pe.edu.upeu.comidata.services;

import pe.edu.upeu.comidata.dto.ModeloDataAutocomplet;
import pe.edu.upeu.comidata.models.ClientesDB;
import java.util.List;
import java.util.Optional;

public interface IClientesService extends CrudGenericoService<ClientesDB, Long> {
    Optional<ClientesDB> findByDni(String dni);
    List<ModeloDataAutocomplet> listAutoCompletCliente();
}