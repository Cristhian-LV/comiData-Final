package pe.edu.upeu.comidata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.comidata.models.CategoriaDB;

public interface CategoriaRepository extends JpaRepository<CategoriaDB,Long> {
}
