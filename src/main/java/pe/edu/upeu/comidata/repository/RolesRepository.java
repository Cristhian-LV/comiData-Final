package pe.edu.upeu.comidata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.comidata.models.RolesDB;

public interface RolesRepository extends JpaRepository<RolesDB,Long> {
}
