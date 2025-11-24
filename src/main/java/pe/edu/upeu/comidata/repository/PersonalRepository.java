package pe.edu.upeu.comidata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.comidata.models.PersonalDB;

@Repository
public interface PersonalRepository extends JpaRepository<PersonalDB, Long> {

    @Query(value = "SELECT * FROM personal_db p " +
            "WHERE (p.correo = :user OR p.nombre_usuario = :user) " +
            "AND p.clave = :pass",
            nativeQuery = true)
    PersonalDB login(@Param("user") String usuario, @Param("pass") String clave);

}
