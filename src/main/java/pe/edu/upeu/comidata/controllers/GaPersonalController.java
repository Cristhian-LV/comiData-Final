package pe.edu.upeu.comidata.controllers;

import javafx.application.Platform;
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
import pe.edu.upeu.comidata.models.PersonalDB; // Entidad Personal
import pe.edu.upeu.comidata.services.PersonalService; // Servicio de Personal
import pe.edu.upeu.comidata.utils.Constantes;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GaPersonalController {

    @Autowired private PersonalService personalService;
    @Autowired private ConfigurableApplicationContext applicationContext; // Necesario para abrir el nuevo FXML

    private ObservableList<PersonalDB> listaPersonalObservable;

    @FXML private TextField txtFiltroPersonal; // Asumiendo este ID en el FXML de la tabla
    @FXML private TableView<PersonalDB> tableViewPersonal;

    @FXML
    public void initialize() {
        configurarTablaPersonal();
        listarPersonal();
        txtFiltroPersonal.textProperty().addListener((observable, oldValue, newValue) -> filtrarPersonal(newValue));
    }

    private void configurarTablaPersonal() {
        TableViewHelper<PersonalDB> helper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columnas = new LinkedHashMap<>();

        // Columnas requeridas: dni, nombresCompletos, correo, telefono, direccion, rol, estado
        columnas.put("DNI", new ColumnInfo("dni", 80.0)); // [cite: 183]
        columnas.put("Nombres", new ColumnInfo("nombresCompletos", 200.0)); // [cite: 184]
        columnas.put("Correo", new ColumnInfo("correo", 150.0)); // [cite: 186]
        columnas.put("Teléfono", new ColumnInfo("telefono", 100.0)); // [cite: 188]
        columnas.put("Dirección", new ColumnInfo("direccion", 150.0)); // [cite: 189]
        columnas.put("Rol", new ColumnInfo("rol.nombre", 100.0)); // RolDB [cite: 190, 180]
        columnas.put("Estado", new ColumnInfo("estado", 100.0)); // [cite: 189]

        // Añadir las columnas y configurar los listeners de acción
        helper.addColumnsInOrderWithSize(tableViewPersonal, columnas, this::editarPersonal, this::eliminarPersonal);
        tableViewPersonal.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    public void listarPersonal() {
        try {
            // Usar findAll del PersonalService [cite: 200]
            listaPersonalObservable = FXCollections.observableArrayList(personalService.findAll());
            tableViewPersonal.setItems(listaPersonalObservable);

        } catch (Exception e) {
            System.err.println("Error al listar personal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editarPersonal(PersonalDB personal) {
        // Llamar al método genérico para abrir el formulario en modo edición
        abrirFormularioPersonal(personal);
    }

    // Método que maneja la acción del botón 'Agregar personal' en el FXML
    @FXML
    public void abrirFormularioAgregar(ActionEvent actionEvent) {
        // Llamar al método genérico para abrir el formulario en modo agregar (personal = null)
        abrirFormularioPersonal(null);
    }

    // Método que abre el formulario de nuevo/editar personal
    private void abrirFormularioPersonal(PersonalDB personalEditando) {
        try {
            // Carga el FXML de la nueva ventana
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constantes.fxml_new_personal));
            // Importante: establece el ControllerFactory para que Spring inyecte el controlador
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = fxmlLoader.load();

            // Obtener el controlador para pasarle los datos
            GaNewPersonalController controller = fxmlLoader.getController();
            controller.setPersonalAEditar(personalEditando);
            controller.setGaPersonalController(this); // Referencia para refrescar la tabla

            Stage stage = new Stage();
            stage.setTitle(personalEditando == null ? "Agregar Nuevo Personal" : "Editar Personal");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar el formulario de personal.");
        }
    }

    private void eliminarPersonal(PersonalDB personal) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de eliminar a '" + personal.getNombresCompletos() + "'?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    personalService.deleteById(personal.getIdPersonal()); // [cite: 200]
                    System.out.println("Personal eliminado exitosamente.");
                    listarPersonal();
                } catch (Exception e) {
                    System.err.println("Error al eliminar personal: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void filtrarPersonal(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tableViewPersonal.setItems(listaPersonalObservable);
        }
        else {
            String lowerCaseFilter = filtro.toLowerCase();
            List<PersonalDB> filtradosList = listaPersonalObservable.stream()
                    .filter(p -> (p.getNombresCompletos() != null && p.getNombresCompletos().toLowerCase().contains(lowerCaseFilter)) || // [cite: 184]
                            (p.getDni() != null && p.getDni().toLowerCase().contains(lowerCaseFilter)) || // [cite: 183]
                            (p.getCorreo() != null && p.getCorreo().toLowerCase().contains(lowerCaseFilter)) ) // [cite: 186]
                    .collect(Collectors.toList());
            ObservableList<PersonalDB> filtradosObservable = FXCollections.observableArrayList(filtradosList);
            tableViewPersonal.setItems(filtradosObservable);
        }
    }

    @FXML
    public void eliminarTodoElPersonal(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de eliminar todo el personal?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    personalService.deleteAll(); // [cite: 200]
                    System.out.println("Todo el personal eliminado.");
                    listarPersonal();
                } catch (Exception e) {
                    System.err.println("Error al eliminar todo el personal: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}