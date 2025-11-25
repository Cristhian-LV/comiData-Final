package pe.edu.upeu.comidata.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.comidata.components.ColumnInfo;
import pe.edu.upeu.comidata.components.TableViewHelper;
import pe.edu.upeu.comidata.models.ProductosDB;
import pe.edu.upeu.comidata.services.CategoriasService;
import pe.edu.upeu.comidata.services.ProductoService;
import pe.edu.upeu.comidata.utils.Constantes;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GaProductosController {

    @Autowired private ProductoService productoService;
    @Autowired private CategoriasService categoriaService;
    @Autowired private ConfigurableApplicationContext applicationContext;

    private ObservableList<ProductosDB> listaProductosObservable;
    private Long idProductoEditando = null;

    @FXML private TextField txtFiltroProductos;
    @FXML private TableView<ProductosDB> tableViewProductos;

    @FXML
    public void initialize() {
        configurarTablaProductos();
        listarProductos();
        txtFiltroProductos.textProperty().addListener((observable, oldValue, newValue) -> filtrarProductos(newValue));
    }

    private void configurarTablaProductos() {
        TableViewHelper<ProductosDB> helper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columnas = new LinkedHashMap<>();
        columnas.put("ID", new ColumnInfo("idProducto", 60.0));
        columnas.put("Nombre", new ColumnInfo("nombre", 200.0));
        columnas.put("Precio", new ColumnInfo("precio", 100.0));
        columnas.put("Categoría", new ColumnInfo("categoria.nombreCategoria", 150.0));
        columnas.put("Stock", new ColumnInfo("stock", 80.0));

        helper.addColumnsInOrderWithSize(tableViewProductos, columnas, this::editarProducto, this::eliminarProducto);
        tableViewProductos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    public void listarProductos() {
        try {
            listaProductosObservable = FXCollections.observableArrayList(productoService.findAll());
            tableViewProductos.setItems(listaProductosObservable);

        } catch (Exception e) {
            System.out.println("Error al listar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editarProducto(ProductosDB producto) {
        idProductoEditando = producto.getIdProducto();
        abrirFormularioProducto(producto);
        System.out.println("MANDAR OTRA PANTALLA");
    }

    private void abrirFormularioProducto(ProductosDB productoEditando) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constantes.fxml_new_producto));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = fxmlLoader.load();

            GaNewProductoController controller = fxmlLoader.getController();
            controller.setProductoAEditar(productoEditando);
            controller.setGaProductosController(this);

            Stage stage = new Stage();
            stage.setTitle(productoEditando == null ? "Agregar Nuevo Producto" : "Editar Producto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Hace que la ventana sea modal
            stage.showAndWait(); // Espera hasta que la ventana se cierre

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar el formulario de producto.");
        }
    }
    private void eliminarProducto(ProductosDB producto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de eliminar el producto '" + producto.getNombre() + "'?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    productoService.deleteById(producto.getIdProducto());
                    System.out.println("Producto eliminado exitosamente.");
                    listarProductos();
                } catch (Exception e) {
                    System.out.println("Error al eliminar producto: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void filtrarProductos(String filtro) {

        if (filtro == null || filtro.isEmpty()) {
            tableViewProductos.setItems(listaProductosObservable);
        }
        else {
            String lowerCaseFilter = filtro.toLowerCase();
            List<ProductosDB> filtradosList = listaProductosObservable.stream()
                    .filter(p -> (p.getNombre() != null && p.getNombre().toLowerCase().contains(lowerCaseFilter)) ||
                            (p.getCategoria() != null && p.getCategoria().getNombreCategoria() != null && p.getCategoria().getNombreCategoria().toLowerCase().contains(lowerCaseFilter)) ||
                            (String.valueOf(p.getPrecio()).contains(lowerCaseFilter)) )
                    .collect(Collectors.toList()); // <-- 1. Recolecta a una List normal
            ObservableList<ProductosDB> filtradosObservable = FXCollections.observableArrayList(filtradosList); // <-- 2. Convierte a ObservableList
            tableViewProductos.setItems(filtradosObservable);
        }
    }


    public void abrirFormularioAgregar(ActionEvent actionEvent) {
        abrirFormularioProducto(null);
    }

    public void eliminarTodosLosProductos(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de eliminar todos los productos?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    productoService.deleteAll();
                    System.out.println("Productos eliminados");
                    listarProductos();
                } catch (Exception e) {
                    System.out.println("Error al eliminar productos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}
