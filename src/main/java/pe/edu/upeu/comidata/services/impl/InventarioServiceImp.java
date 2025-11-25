package pe.edu.upeu.comidata.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pe.edu.upeu.comidata.models.InventarioDB;
import pe.edu.upeu.comidata.repository.InventarioRepository;
import pe.edu.upeu.comidata.services.InventarioService;

@RequiredArgsConstructor
@Service
public class InventarioServiceImp extends CrudGenericoServiceImp<InventarioDB,Long> implements InventarioService {

    private final InventarioRepository inventarioRepository;

    @Override
    protected JpaRepository<InventarioDB, Long> getRepo() {
        return inventarioRepository;
    }

}
