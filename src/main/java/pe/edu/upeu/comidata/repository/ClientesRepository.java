package pe.edu.upeu.comidata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.comidata.models.ClientesDB;
import java.util.Optional;

public interface ClientesRepository extends JpaRepository<ClientesDB, Long> {

    Optional<ClientesDB> findByDni(String dni);
}