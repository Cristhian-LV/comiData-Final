package pe.edu.upeu.comidata.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pe.edu.upeu.comidata.dto.ComboBoxOption;
import pe.edu.upeu.comidata.models.RolesDB;
import pe.edu.upeu.comidata.repository.RolesRepository;
import pe.edu.upeu.comidata.services.RolesService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RolesServiceImp extends CrudGenericoServiceImp<RolesDB,Long> implements RolesService {

    private final RolesRepository rolesRepository;

    @Override
    protected JpaRepository<RolesDB, Long> getRepo() {
        return rolesRepository;
    }

    @Override
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar=new ArrayList<>();
        ComboBoxOption cb;
        for(RolesDB rol : rolesRepository.findAll()) {
            cb=new ComboBoxOption();
            cb.setKey(String.valueOf(rol.getIdRol()));
            cb.setValue(rol.getNombre());
            listar.add(cb);
        }
        return listar;
    }
}
