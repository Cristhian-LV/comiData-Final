package pe.edu.upeu.comidata.services.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pe.edu.upeu.comidata.dto.ModeloDataAutocomplet;
import pe.edu.upeu.comidata.models.ProductosDB;
import pe.edu.upeu.comidata.repository.ProductosRepository;
import pe.edu.upeu.comidata.services.ProductoService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductoServiceImp extends CrudGenericoServiceImp<ProductosDB,Long> implements ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImp.class);

    private final ProductosRepository productosRepository;

    @Override
    protected JpaRepository<ProductosDB, Long> getRepo() {
        return productosRepository;
    }

    @Override
    public List<ModeloDataAutocomplet> listAutoCompletProducto(String nombre) {
        List<ModeloDataAutocomplet> listarProducto = new ArrayList<>();
        try {
            for (ProductosDB producto :
                    productosRepository.listAutoCompletProducto(nombre + "%")) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();data.setIdx(producto.getNombre());
                data.setLabel(String.valueOf(producto.getIdProducto()));
                data.setSearchKey(producto.getPrecio() + ":" +
                        producto.getStock());
                listarProducto.add(data);
            }
        } catch (Exception e) {
            logger.error("Error al realizar la busqueda", e);
        }
        return listarProducto;
    }

    @Override
    public List<ModeloDataAutocomplet> listAutoCompletProducto() {
        List<ModeloDataAutocomplet> listarProducto = new ArrayList<>();
        try {
            for (ProductosDB producto : productosRepository.findAll())
            {ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                data.setIdx(String.valueOf(producto.getIdProducto()));
                data.setLabel(producto.getNombre());
                data.setSearchKey(producto.getPrecio() + ":" +
                        producto.getStock());
                listarProducto.add(data);
            }
        } catch (Exception e) {
            logger.error("Error al realizar la busqueda", e);
        }
        return listarProducto;
    }

}