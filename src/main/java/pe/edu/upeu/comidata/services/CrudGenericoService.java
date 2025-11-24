package pe.edu.upeu.comidata.services;

import java.util.List;
import java.util.Optional;

/**
 * Define las operaciones CRUD básicas que deben implementar todos los servicios.
 * @param <T> La entidad del modelo (ej: RolDB, ProductosDB).
 * @param <ID> El tipo de la clave primaria (ej: Long).
 */
public interface CrudGenericoService<T, ID> {

    T save(T t);
    T update(ID id,T t);
    List<T> findAll();
    T findById(ID id);
    void deleteById(ID id);
    void deleteAll();
    long count();

}