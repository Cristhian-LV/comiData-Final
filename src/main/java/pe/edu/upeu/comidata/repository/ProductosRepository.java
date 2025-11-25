package pe.edu.upeu.comidata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.comidata.models.ProductosDB;

import java.util.List;

public interface ProductosRepository extends JpaRepository<ProductosDB, Long> {

    @Query(value = "SELECT p.* FROM productos_db p " +
            "WHERE p.nombre LIKE :filter",
            nativeQuery = true)
    List<ProductosDB> listAutoCompletProducto(@Param("filter") String filter);

}
