package pe.edu.upeu.comidata.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pe.edu.upeu.comidata.models.PersonalDB;
import pe.edu.upeu.comidata.repository.PersonalRepository;
import pe.edu.upeu.comidata.services.PersonalService;

@RequiredArgsConstructor
@Service
public class PersonalServiceImp extends CrudGenericoServiceImp<PersonalDB, Long> implements PersonalService {

    private final PersonalRepository personalRepository;

    @Override
    protected JpaRepository<PersonalDB, Long> getRepo() {
        return personalRepository;
    }

    @Override
    public PersonalDB login(String usuario, String clave) {
        return personalRepository.login(usuario,clave);
    }
}
