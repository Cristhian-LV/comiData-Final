package pe.edu.upeu.comidata.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.comidata.dto.ModeloDataAutocomplet;
import pe.edu.upeu.comidata.dto.PersonaDto;
import pe.edu.upeu.comidata.models.*;
import pe.edu.upeu.comidata.services.*;
import pe.edu.upeu.comidata.components.Autocompletado;
import pe.edu.upeu.comidata.components.DialogHelper;
import pe.edu.upeu.comidata.exception.ModelNotFoundException; // Importación necesaria
import pe.edu.upeu.comidata.utils.ConsultaDNI;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

@Controller
public class PePedidosController {

    @Autowired private IClientesService clientesService;
    @Autowired private ProductoService productosService;
    @Autowired private ICarritoService carritoService;
    @Autowired private IPedidosService pedidosService;

    @FXML private TextField autocompCliente;
    @FXML private TextField dniRuc;
    @FXML private TextField razonSocial;
    @FXML private TextField txtDireccion;
    @FXML private TextField autocompProducto;
    @FXML private TextField nombreProducto;
    @FXML private TextField codigoPro;
    @FXML private TextField stockPro;
    @FXML private TextField cantidadPro;
    @FXML private TextField punitPro;
    @FXML private TextField preTPro;
    @FXML private TableView<CarritoDB> tableView;
    @FXML private TextField txtBaseImp;
    @FXML private TextField txtIgv;
    @FXML private TextField txtDescuento;
    @FXML private TextField txtImporteT;
    @FXML private ComboBox<String> cbxTipoDocumento;
    @FXML private ComboBox<String> cbxTipoPedido;

    private ClientesDB clienteSeleccionado = null;
    private ProductosDB productoSeleccionado = null;

    // NOTA: Reemplaza este ID con la forma real de obtener el ID del Personal logeado
    private final Long ID_PERSONAL_LOGEADO = 1L;

    @FXML
    public void initialize() {
        configurarTabla();
        cargarCombobox();
        configurarAutocompletado();
        listarCarrito();
    }

    private void cargarCombobox() {
        cbxTipoDocumento.setItems(FXCollections.observableArrayList(Arrays.asList("BOLETA", "FACTURA")));
        cbxTipoPedido.setItems(FXCollections.observableArrayList(Arrays.asList("TIENDA", "DELIVERY")));
        cbxTipoDocumento.getSelectionModel().selectFirst();
        cbxTipoPedido.getSelectionModel().selectFirst();
    }

    private void configurarAutocompletado() {
        List<ModeloDataAutocomplet> clientesData = clientesService.listAutoCompletCliente();
        Autocompletado.autocompletarTextField(autocompCliente, clientesData, this::seleccionarClientePorData);

        // Lógica para Autocompletado de Producto (Requiere IProductosService.listAutoCompletProducto())
        // List<ModeloDataAutocomplet> productosData = productosService.listAutoCompletProducto();
        // Autocompletado.autocompletarTextField(autocompProducto, productosData, this::seleccionarProductoPorData);
    }

    private void seleccionarClientePorData(ModeloDataAutocomplet data) {
        Long idCliente = Long.parseLong(data.getIdx());
        try {
            // Adaptado para CrudGenericoService.findById que devuelve la entidad T o lanza ModelNotFoundException
            ClientesDB cliente = clientesService.findById(idCliente);
            clienteSeleccionado = cliente;

            dniRuc.setText(clienteSeleccionado.getDni());
            razonSocial.setText(clienteSeleccionado.getNombresCompletos());
            txtDireccion.setText(clienteSeleccionado.getUltimaDireccion());

        } catch (ModelNotFoundException e) {
            DialogHelper.mostrarAlerta("Error", "Cliente no encontrado (ID: " + idCliente + ").", Alert.AlertType.ERROR);
            limpiarCamposCliente();
        }
    }

    // Método para seleccionar producto (Debe implementarse la lógica de búsqueda por ID)
    private void seleccionarProductoPorData(ModeloDataAutocomplet data) {
        // ... Lógica de búsqueda por ID, similar a seleccionarClientePorData
    }

    @Autowired private ConsultaDNI consultaDNI;
    @FXML
    public void buscarClienteCdni() {
        String dni = autocompCliente.getText().trim();
        if (dni.isEmpty()) return;

        Optional<ClientesDB> clienteOpt = clientesService.findByDni(dni);

        if (clienteOpt.isPresent()) {
            // Caso 1: Cliente encontrado en la BD local
            ClientesDB cliente = clienteOpt.get();
            ModeloDataAutocomplet data = new ModeloDataAutocomplet(
                    cliente.getIdCliente().toString(),
                    cliente.getNombresCompletos(),
                    cliente.getDni()
            );
            seleccionarClientePorData(data);

        } else {
            // Caso 2: Cliente no encontrado en BD local -> Consultar en línea
            DialogHelper.mostrarAlerta("Buscando en línea", "Buscando datos del DNI: " + dni + "...", Alert.AlertType.INFORMATION);

            // >> CORRECCIÓN: Llamar a la instancia inyectada (consultaDNI)
            PersonaDto persona = consultaDNI.consultarDNI(dni);
            // << FIN CORRECCIÓN

            if (persona.getDni() != null && !persona.getDni().isEmpty()) {
                // Caso 2a: Datos encontrados en línea -> Registrar nuevo cliente
                String nombresCompletos = persona.getNombre() + " " + persona.getApellidoPaterno() + " " + persona.getApellidoMaterno();

                ClientesDB nuevoCliente = ClientesDB.builder()
                        .dni(persona.getDni())
                        .nombresCompletos(nombresCompletos)
                        .ultimaDireccion("SIN DIRECCION")
                        .build();

                ClientesDB clienteGuardado = clientesService.save(nuevoCliente);

                DialogHelper.mostrarAlerta("Cliente Registrado", "Cliente " + nombresCompletos + " registrado exitosamente.", Alert.AlertType.INFORMATION);

                // Seleccionar el cliente recién guardado
                ModeloDataAutocomplet data = new ModeloDataAutocomplet(
                        clienteGuardado.getIdCliente().toString(),
                        clienteGuardado.getNombresCompletos(),
                        clienteGuardado.getDni()
                );
                seleccionarClientePorData(data);

            } else {
                // Caso 2b: DNI inválido o no encontrado en línea
                DialogHelper.mostrarAlerta("Cliente no encontrado", "El DNI/RUC no existe ni en la base de datos ni en la consulta en línea. Por favor, regístrelo manualmente.", Alert.AlertType.WARNING);
                limpiarCamposCliente();
            }
        }
    }

    @FXML
    public void guardarCliente() {
        DialogHelper.mostrarAlerta("Funcionalidad Pendiente", "Se debe implementar la apertura de un formulario modal para registrar un nuevo cliente.", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void calcularPT() {
        // Lógica para calcular P.Total (cantidadPro * punitPro)
    }

    @FXML
    public void registrarCarrito() {
        if (productoSeleccionado == null) {
            DialogHelper.mostrarAlerta("Error", "Debe seleccionar un producto.", Alert.AlertType.ERROR);
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadPro.getText());
            if (cantidad <= 0) {
                throw new NumberFormatException();
            }
            if (cantidad > productoSeleccionado.getStock()) {
                DialogHelper.mostrarAlerta("Stock insuficiente", "Solo hay " + productoSeleccionado.getStock() + " unidades en stock.", Alert.AlertType.WARNING);
                return;
            }

            Optional<CarritoDB> carritoExistente = carritoService.findByPersonalIdPersonalAndProductoIdProducto(ID_PERSONAL_LOGEADO, productoSeleccionado.getIdProducto());

            if (carritoExistente.isPresent()) {
                CarritoDB carrito = carritoExistente.get();
                carrito.setCantidad(carrito.getCantidad() + cantidad);
                carritoService.save(carrito);
            } else {
                CarritoDB nuevoCarrito = CarritoDB.builder()
                        .personal(PersonalDB.builder().idPersonal(ID_PERSONAL_LOGEADO).build())
                        .producto(productoSeleccionado)
                        .cantidad(cantidad)
                        .build();
                carritoService.save(nuevoCarrito);
            }

            listarCarrito();
            limpiarCamposProducto();

        } catch (NumberFormatException e) {
            DialogHelper.mostrarAlerta("Error de cantidad", "Ingrese una cantidad válida y positiva.", Alert.AlertType.ERROR);
        }
    }

    private void configurarTabla() {
        TableColumn<CarritoDB, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProducto().getNombre()));

        TableColumn<CarritoDB, Integer> colCantidad = new TableColumn<>("Cant.");
        colCantidad.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCantidad()));

        TableColumn<CarritoDB, BigDecimal> colPUnit = new TableColumn<>("P. Unit.");
        colPUnit.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getProducto().getPrecio()));

        TableColumn<CarritoDB, BigDecimal> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(cellData -> {
            ProductosDB p = cellData.getValue().getProducto();
            Integer c = cellData.getValue().getCantidad();
            return new SimpleObjectProperty<>(p.getPrecio().multiply(new BigDecimal(c)).setScale(2, RoundingMode.HALF_UP));
        });

        tableView.getColumns().clear();
        tableView.getColumns().addAll(colProducto, colCantidad, colPUnit, colSubtotal);
    }

    private void listarCarrito() {
        List<CarritoDB> lista = carritoService.listaCarritoPersonal(ID_PERSONAL_LOGEADO);
        tableView.setItems(FXCollections.observableArrayList(lista));
        calcularTotales(lista);
    }

    private void calcularTotales(List<CarritoDB> items) {
        BigDecimal baseImponible = BigDecimal.ZERO;
        BigDecimal totalDescuento = BigDecimal.ZERO;

        final BigDecimal TASA_IGV = new BigDecimal("0.18");
        final BigDecimal FACTOR_IGV = BigDecimal.ONE.add(TASA_IGV);

        for (CarritoDB item : items) {
            BigDecimal subtotalConIgv = item.getProducto().getPrecio()
                    .multiply(new BigDecimal(item.getCantidad()));

            // Calculamos la base imponible dividiendo el subtotal_con_IGV
            baseImponible = baseImponible.add(subtotalConIgv.divide(FACTOR_IGV, 4, RoundingMode.HALF_UP));
        }

        baseImponible = baseImponible.setScale(2, RoundingMode.HALF_UP);
        BigDecimal igv = baseImponible.multiply(TASA_IGV).setScale(2, RoundingMode.HALF_UP);
        BigDecimal importeTotal = baseImponible.add(igv).subtract(totalDescuento).setScale(2, RoundingMode.HALF_UP);

        txtBaseImp.setText(baseImponible.toString());
        txtIgv.setText(igv.toString());
        txtDescuento.setText(totalDescuento.toString());
        txtImporteT.setText(importeTotal.toString());
    }

    @FXML
    public void registrarPedido() {
        if (clienteSeleccionado == null) {
            DialogHelper.mostrarAlerta("Error de Pedido", "Debe seleccionar un cliente.", Alert.AlertType.ERROR);
            return;
        }
        if (tableView.getItems().isEmpty()) {
            DialogHelper.mostrarAlerta("Error de Pedido", "El carrito está vacío.", Alert.AlertType.ERROR);
            return;
        }

        try {
            String tipoPedido = cbxTipoPedido.getSelectionModel().getSelectedItem();
            String tipoDocumento = cbxTipoDocumento.getSelectionModel().getSelectedItem();

            PedidosDB nuevoPedido = pedidosService.registrarPedido(
                    ID_PERSONAL_LOGEADO,
                    clienteSeleccionado.getIdCliente(),
                    tipoPedido,
                    tipoDocumento
            );

            DialogHelper.mostrarAlerta("Pedido Registrado", "El pedido N° " + nuevoPedido.getIdPedido() + " se ha registrado correctamente.", Alert.AlertType.INFORMATION);
            limpiarFormulario();

        } catch (Exception e) {
            DialogHelper.mostrarAlerta("Error al Registrar", "Ocurrió un error: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void limpiarCamposCliente() {
        clienteSeleccionado = null;
        dniRuc.clear();
        razonSocial.clear();
        txtDireccion.clear();
    }

    private void limpiarCamposProducto() {
        productoSeleccionado = null;
        autocompProducto.clear();
        nombreProducto.clear();
        codigoPro.clear();
        stockPro.clear();
        cantidadPro.clear();
        punitPro.clear();
        preTPro.clear();
    }

    private void limpiarFormulario() {
        limpiarCamposCliente();
        limpiarCamposProducto();
        tableView.getItems().clear();
        listarCarrito(); // Llama a listar carrito para recalcular totales a cero
        autocompCliente.clear();
    }
}