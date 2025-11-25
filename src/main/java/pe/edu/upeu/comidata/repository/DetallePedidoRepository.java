package pe.edu.upeu.comidata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.comidata.models.DetallePedidoDB;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedidoDB, Long> {

}