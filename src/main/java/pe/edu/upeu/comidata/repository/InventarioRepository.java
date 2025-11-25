package pe.edu.upeu.comidata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.comidata.models.InventarioDB;

public interface InventarioRepository extends JpaRepository<InventarioDB,Long> {
}
