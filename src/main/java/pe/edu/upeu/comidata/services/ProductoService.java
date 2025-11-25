package pe.edu.upeu.comidata.services;

import pe.edu.upeu.comidata.dto.ModeloDataAutocomplet;
import pe.edu.upeu.comidata.models.ProductosDB;

import java.util.List;

public interface ProductoService extends CrudGenericoService<ProductosDB, Long>{
    List<ModeloDataAutocomplet> listAutoCompletProducto(String nombre);
    public List<ModeloDataAutocomplet> listAutoCompletProducto();
}
