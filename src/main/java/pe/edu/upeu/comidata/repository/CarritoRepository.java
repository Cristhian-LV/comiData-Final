package pe.edu.upeu.comidata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.comidata.models.CarritoDB;

import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<CarritoDB, Long> {

    List<CarritoDB> findByPersonalIdPersonal(Long idPersonal);

    void deleteByPersonalIdPersonal(Long idPersonal);

    Optional<CarritoDB> findByPersonalIdPersonalAndProductoIdProducto(Long idPersonal, Long idProducto);
}