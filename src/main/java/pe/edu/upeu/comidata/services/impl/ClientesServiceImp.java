package pe.edu.upeu.comidata.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import pe.edu.upeu.comidata.dto.ModeloDataAutocomplet;
import pe.edu.upeu.comidata.models.ClientesDB;
import pe.edu.upeu.comidata.repository.ClientesRepository;
import pe.edu.upeu.comidata.services.IClientesService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientesServiceImp extends CrudGenericoServiceImp<ClientesDB, Long> implements IClientesService {

    private final ClientesRepository clientesRepository;

    @Override
    protected JpaRepository<ClientesDB, Long> getRepo() {
        return clientesRepository;
    }

    @Override
    public Optional<ClientesDB> findByDni(String dni) {
        return clientesRepository.findByDni(dni);
    }

    @Override
    public List<ModeloDataAutocomplet> listAutoCompletCliente() {
        List<ModeloDataAutocomplet> listar = new ArrayList<>();
        ModeloDataAutocomplet cb;
        for (ClientesDB cliente : clientesRepository.findAll()) {
            cb = new ModeloDataAutocomplet();
            cb.setIdx(String.valueOf(cliente.getIdCliente()));
            cb.setLabel(cliente.getNombresCompletos());
            cb.setSearchKey(cliente.getDni());
            listar.add(cb);
        }
        return listar;
    }
}