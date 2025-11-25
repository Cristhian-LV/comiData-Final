package pe.edu.upeu.comidata.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import pe.edu.upeu.comidata.models.CarritoDB;
import pe.edu.upeu.comidata.repository.CarritoRepository;
import pe.edu.upeu.comidata.services.ICarritoService;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarritoServiceImp extends CrudGenericoServiceImp<CarritoDB, Long> implements ICarritoService {

    private final CarritoRepository carritoRepository;

    @Override
    protected JpaRepository<CarritoDB, Long> getRepo() {
        return carritoRepository;
    }

    @Override
    public List<CarritoDB> listaCarritoPersonal(Long idPersonal) {
        return carritoRepository.findByPersonalIdPersonal(idPersonal);
    }

    @Override
    public Optional<CarritoDB> findByPersonalIdPersonalAndProductoIdProducto(Long idPersonal, Long idProducto) {
        return carritoRepository.findByPersonalIdPersonalAndProductoIdProducto(idPersonal, idProducto);
    }

    @Override
    public void deleteByPersonalIdPersonal(Long idPersonal) {
        carritoRepository.deleteByPersonalIdPersonal(idPersonal);
    }
}