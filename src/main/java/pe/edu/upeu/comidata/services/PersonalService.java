package pe.edu.upeu.comidata.services;

import pe.edu.upeu.comidata.models.PersonalDB;

public interface PersonalService extends CrudGenericoService<PersonalDB, Long>{

    PersonalDB login(String usuario, String clave);

}
