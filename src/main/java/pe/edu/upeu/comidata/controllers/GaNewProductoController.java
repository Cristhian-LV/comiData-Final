package pe.edu.upeu.comidata.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.comidata.dto.ComboBoxOption;
import pe.edu.upeu.comidata.models.CategoriaDB;
import pe.edu.upeu.comidata.models.InventarioDB;
import pe.edu.upeu.comidata.models.ProductosDB;
import pe.edu.upeu.comidata.services.CategoriasService;
import pe.edu.upeu.comidata.services.InventarioService;
import pe.edu.upeu.comidata.services.ProductoService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class GaNewProductoController {

    @Autowired private ProductoService productoService;
    @Autowired private CategoriasService categoriaService;
    @Autowired private InventarioService inventarioService;

    private GaProductosController gaProductosController;

    private ProductosDB productoAEditar = null;

    @FXML private Label lblTituloFormulario;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtPrecioUnitario;
    @FXML private TextField txtStockProducto;
    @FXML private ComboBox<ComboBoxOption> cbxCategoriaProducto;
    @FXML private TextArea txtDescripcionProducto;
    @FXML private Button btnGuardarProducto;
    @FXML private Label lblMensajeProducto;

    @FXML
    public void initialize() {
        cargarCategorias();

        txtPrecioUnitario.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));
        txtStockProducto.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
    }

    public void setProductoAEditar(ProductosDB producto) {
        this.productoAEditar = producto;
        if (producto != null) {
            cargarDatosProducto(producto);
        }
    }

    public void setGaProductosController(GaProductosController controller) {
        this.gaProductosController = controller;
    }

    private void cargarDatosProducto(ProductosDB producto) {
        lblTituloFormulario.setText("Editar Producto: " + producto.getNombre());
        txtNombreProducto.setText(producto.getNombre());
        txtDescripcionProducto.setText(producto.getDescripcion());
        txtPrecioUnitario.setText(producto.getPrecio().toString());
        txtStockProducto.setText(producto.getStock().toString());
        btnGuardarProducto.setText("Actualizar");

        // Seleccionar la categoría
        if (producto.getCategoria() != null) {
            for (ComboBoxOption option : cbxCategoriaProducto.getItems()) {
                if (option.getKey().equals(String.valueOf(producto.getCategoria().getIdCategoria()))) {
                    cbxCategoriaProducto.getSelectionModel().select(option);
                    break;
                }
            }
        }
    }

    private void cargarCategorias() {
        try {
            List<ComboBoxOption> opciones = categoriaService.listarCombobox();
            cbxCategoriaProducto.setItems(FXCollections.observableArrayList(opciones));
        } catch (Exception e) {
            System.out.println("Error al cargar categorías: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void guardarProducto() {
        if (!validarCampos()) {
            return;
        }

        try {
            ProductosDB producto;
            String mensajeExito;
            boolean esNuevo = (productoAEditar == null);
            Integer stockAnterior = 0;

            if (esNuevo) {
                producto = new ProductosDB();
                mensajeExito = "Producto guardado exitosamente.";
            } else {
                producto = productoAEditar;
                stockAnterior = producto.getStock();
                mensajeExito = "Producto actualizado exitosamente.";
            }

            producto.setNombre(txtNombreProducto.getText());
            producto.setDescripcion(txtDescripcionProducto.getText());
            producto.setPrecio(new BigDecimal(txtPrecioUnitario.getText()));
            producto.setStock(Integer.parseInt(txtStockProducto.getText()));


            ComboBoxOption selectedCategory = cbxCategoriaProducto.getSelectionModel().getSelectedItem();
            CategoriaDB categoria = CategoriaDB.builder()
                    .idCategoria(Long.parseLong(selectedCategory.getKey()))
                    .build();
            producto.setCategoria(categoria);


            productoService.save(producto);

            int nuevoStock = Integer.parseInt(txtStockProducto.getText());
            if (esNuevo && nuevoStock > 0) {
                registrarMovimientoInventario(producto, "ENTRADA", nuevoStock, "Inventario inicial");

            } else if (!esNuevo) {
                int diferenciaStock = nuevoStock - stockAnterior;
                if (diferenciaStock > 0) {
                    registrarMovimientoInventario(producto, "ENTRADA", diferenciaStock, "Ajuste/Reposición por edición");
                }
            }

            lblMensajeProducto.setText(mensajeExito);
            lblMensajeProducto.setStyle("-fx-text-fill: green;");

            if (gaProductosController != null) {
                gaProductosController.listarProductos();
            }

            Platform.runLater(this::cerrarVentana);

        } catch (Exception e) {
            e.printStackTrace();
            lblMensajeProducto.setText("Error al guardar/actualizar el producto: " + e.getMessage());
            lblMensajeProducto.setStyle("-fx-text-fill: red;");
        }
    }

    private boolean validarCampos() {
        if (txtNombreProducto.getText().trim().isEmpty() ||
                txtPrecioUnitario.getText().trim().isEmpty() ||
                txtStockProducto.getText().trim().isEmpty() ||
                cbxCategoriaProducto.getSelectionModel().isEmpty()) {

            lblMensajeProducto.setText("Todos los campos marcados son obligatorios (Nombre, Precio, Stock, Categoría)."); // [cite: 30]
            lblMensajeProducto.setStyle("-fx-text-fill: orange;");
            return false;
        }
        return true;
    }

    // Método auxiliar para registrar un movimiento de inventario
    private void registrarMovimientoInventario(ProductosDB producto, String tipo, int cantidad, String observaciones) {
        InventarioDB inventario = InventarioDB.builder()
                .producto(producto)
                .fechaRegistro(LocalDateTime.now())
                .tipoMovimiento(tipo)
                .cantidadMovimiento(cantidad)
                .observaciones(observaciones)
                .build();

        inventarioService.save(inventario);
    }

    @FXML
    public void cancelarFormulario() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnGuardarProducto.getScene().getWindow();
        stage.close();
    }
}