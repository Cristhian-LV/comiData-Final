package pe.edu.upeu.comidata.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pe.edu.upeu.comidata.dto.ComboBoxOption;
import pe.edu.upeu.comidata.models.CategoriaDB;
import pe.edu.upeu.comidata.repository.CategoriaRepository;
import pe.edu.upeu.comidata.services.CategoriasService;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoriasServiceImp extends CrudGenericoServiceImp<CategoriaDB, Long> implements CategoriasService {

    private final CategoriaRepository categoriasRepository;

    @Override
    protected JpaRepository<CategoriaDB, Long> getRepo() {
        return categoriasRepository;
    }

    @Override
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar=new ArrayList<>();
        ComboBoxOption cb;
        for(CategoriaDB cate : categoriasRepository.findAll()) {
            cb=new ComboBoxOption();
            cb.setKey(String.valueOf(cate.getIdCategoria()));
            cb.setValue(cate.getNombreCategoria());
            listar.add(cb);
        }
        return listar;
    }
}