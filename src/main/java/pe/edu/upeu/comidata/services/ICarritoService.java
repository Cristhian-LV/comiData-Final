package pe.edu.upeu.comidata.services;

import pe.edu.upeu.comidata.models.CarritoDB;
import java.util.List;
import java.util.Optional;

public interface ICarritoService extends CrudGenericoService<CarritoDB, Long> {
    List<CarritoDB> listaCarritoPersonal(Long idPersonal);
    Optional<CarritoDB> findByPersonalIdPersonalAndProductoIdProducto(Long idPersonal, Long idProducto);
    void deleteByPersonalIdPersonal(Long idPersonal);
}