package pe.edu.upeu.comidata.services.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.comidata.exception.ModelNotFoundException;
import pe.edu.upeu.comidata.services.CrudGenericoService;
import java.util.List;
import java.util.Optional;

/**
 * Implementación base para las operaciones CRUD genéricas.
 * @param <T> La entidad del modelo.
 * @param <ID> El tipo de la clave primaria.
 */
public abstract class CrudGenericoServiceImp<T, ID> implements CrudGenericoService<T, ID> {

    protected abstract JpaRepository<T,ID> getRepo();

    @Override
    public T save(T t) {
        return getRepo().save(t);
    }

    @Override
    public T update(ID id, T t) {
        getRepo().findById(id).orElseThrow(()->new ModelNotFoundException("ID NOT FOUND "+id));
        return getRepo().save(t);
    }

    @Override
    public List<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    public T findById(ID id) {
        return getRepo().findById(id).orElseThrow(()->new ModelNotFoundException("ID NOT FOUND "+id));
    }

    @Override
    public void deleteById(ID id) {
        if(!getRepo().existsById(id)){
            throw new ModelNotFoundException("ID NOT FOUND "+id);
        }
        getRepo().deleteById(id);
    }

    @Override
    public void deleteAll() {
        getRepo().deleteAll();
    }

    @Override
    public long count() {
        return getRepo().count();
    }

}