package pe.edu.upeu.comidata.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.comidata.dto.ComboBoxOption;
import pe.edu.upeu.comidata.models.PersonalDB;
import pe.edu.upeu.comidata.models.RolesDB;
import pe.edu.upeu.comidata.services.PersonalService;
import pe.edu.upeu.comidata.services.RolesService;

import java.util.Arrays;

@Controller
@NoArgsConstructor
public class GaNewPersonalController {

    @Autowired private PersonalService personalService; // [cite: 200]
    @Autowired private RolesService rolesService; // [cite: 197]

    private GaPersonalController gaPersonalController;
    private PersonalDB personalAEditar = null;

    // Campos del FXML (Asumiendo IDs según el FXML de productos adaptado)
    @FXML private Label lblTituloFormulario;
    @FXML private TextField txtDni; // [cite: 183]
    @FXML private TextField txtNombresCompletos; // [cite: 184]
    @FXML private TextField txtNombreUsuario; // [cite: 185]
    @FXML private TextField txtCorreo; // [cite: 186]
    @FXML private PasswordField pfClave; // [cite: 187]
    @FXML private TextField txtTelefono; // [cite: 188]
    @FXML private TextField txtDireccion; // [cite: 189]
    @FXML private ComboBox<ComboBoxOption> cbxRol; // [cite: 190]
    @FXML private ComboBox<String> cbxEstado; // [cite: 189]
    @FXML private Button btnGuardarPersonal; // (se renombra en el FXML)
    @FXML private Label lblMensajePersonal;

    // Constantes de Estado según el modelo [cite: 190]
    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_DESPEDIDO = "DESPEDIDO";

    @FXML
    public void initialize() {
        cargarRoles();
        cargarEstados();
    }

    // Setters llamados desde GaPersonalController
    public void setPersonalAEditar(PersonalDB personal) {
        this.personalAEditar = personal;
        if (personal != null) {
            cargarDatosPersonal(personal);
        }
    }

    public void setGaPersonalController(GaPersonalController controller) {
        this.gaPersonalController = controller;
    }

    private void cargarRoles() {
        try {
            cbxRol.setItems(FXCollections.observableArrayList(rolesService.listarCombobox()));
        } catch (Exception e) {
            System.err.println("Error al cargar roles: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarEstados() {
        // Opciones de estado fijas según PersonalDB [cite: 190]
        cbxEstado.setItems(FXCollections.observableArrayList(Arrays.asList(ESTADO_ACTIVO, ESTADO_DESPEDIDO)));
    }

    private void cargarDatosPersonal(PersonalDB personal) {
        lblTituloFormulario.setText("Editar Personal: " + personal.getNombresCompletos()); // [cite: 184]
        btnGuardarPersonal.setText("Actualizar");

        txtDni.setText(personal.getDni()); // [cite: 183]
        txtNombresCompletos.setText(personal.getNombresCompletos()); // [cite: 184]
        txtNombreUsuario.setText(personal.getNombreUsuario()); // [cite: 185]
        txtCorreo.setText(personal.getCorreo()); // [cite: 186]
        pfClave.setText(personal.getClave()); // [cite: 187] (NOTA: Muestra el hash, no la clave)
        txtTelefono.setText(personal.getTelefono()); // [cite: 188]
        txtDireccion.setText(personal.getDireccion()); // [cite: 189]
        cbxEstado.getSelectionModel().select(personal.getEstado()); // [cite: 189]

        // Seleccionar el Rol
        if (personal.getRol() != null) { // [cite: 190]
            for (ComboBoxOption option : cbxRol.getItems()) {
                if (option.getKey().equals(String.valueOf(personal.getRol().getIdRol()))) {
                    cbxRol.getSelectionModel().select(option);
                    break;
                }
            }
        }
    }

    @FXML
    public void guardarPersonal() {
        if (!validarCampos()) {
            return;
        }

        try {
            PersonalDB personal;
            String mensajeExito;
            boolean esNuevo = (personalAEditar == null);

            if (esNuevo) {
                personal = new PersonalDB();
                mensajeExito = "Personal guardado exitosamente.";
            } else {
                personal = personalAEditar;
                mensajeExito = "Personal actualizado exitosamente.";
            }

            // Mapear campos
            personal.setDni(txtDni.getText()); // [cite: 183]
            personal.setNombresCompletos(txtNombresCompletos.getText()); // [cite: 184]
            personal.setNombreUsuario(txtNombreUsuario.getText()); // [cite: 185]
            personal.setCorreo(txtCorreo.getText()); // [cite: 186]
            personal.setTelefono(txtTelefono.getText()); // [cite: 188]
            personal.setDireccion(txtDireccion.getText()); // [cite: 189]
            personal.setEstado(cbxEstado.getSelectionModel().getSelectedItem()); // [cite: 189]

            // Manejar la Contraseña (Clave)
            String nuevaClave = pfClave.getText();
            if (esNuevo || !nuevaClave.equals(personal.getClave())) {
                // NOTA CRÍTICA: La contraseña DEBE ser hasheada antes de guardarla. [cite: 187, 188]
                // Aquí solo se guarda el texto, lo cual es INSEGURO.
                // Asumo que tu proyecto tiene un servicio de hashing (ej. BCryptPasswordEncoder)
                // y lo usarías aquí: personal.setClave(hashingService.encode(nuevaClave));
                personal.setClave(nuevaClave); // Reemplaza esto por tu lógica de hashing
            }

            // Mapear el Rol
            ComboBoxOption selectedRole = cbxRol.getSelectionModel().getSelectedItem();
            RolesDB rol = RolesDB.builder()
                    .idRol(Long.parseLong(selectedRole.getKey()))
                    .build();
            personal.setRol(rol); // [cite: 190]

            personalService.save(personal); // Guardar/Actualizar [cite: 200]

            lblMensajePersonal.setText(mensajeExito);
            lblMensajePersonal.setStyle("-fx-text-fill: green;");

            if (gaPersonalController != null) {
                gaPersonalController.listarPersonal();
            }

            Platform.runLater(this::cerrarVentana);

        } catch (Exception e) {
            e.printStackTrace();
            lblMensajePersonal.setText("Error al guardar/actualizar el personal: " + e.getMessage());
            lblMensajePersonal.setStyle("-fx-text-fill: red;");
        }
    }

    private boolean validarCampos() {
        if (txtDni.getText().trim().isEmpty() || // [cite: 183]
                txtNombresCompletos.getText().trim().isEmpty() || // [cite: 184]
                txtNombreUsuario.getText().trim().isEmpty() || // [cite: 185]
                txtCorreo.getText().trim().isEmpty() || // [cite: 186]
                pfClave.getText().trim().isEmpty() || // [cite: 187]
                cbxRol.getSelectionModel().isEmpty() || // [cite: 190]
                cbxEstado.getSelectionModel().isEmpty()) { // [cite: 189]

            lblMensajePersonal.setText("Todos los campos obligatorios (DNI, Nombres, Usuario, Correo, Clave, Rol, Estado) deben ser llenados.");
            lblMensajePersonal.setStyle("-fx-text-fill: orange;");
            return false;
        }
        return true;
    }

    @FXML
    public void cancelarFormulario() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) (btnGuardarPersonal != null ? btnGuardarPersonal : txtDni).getScene().getWindow();
        stage.close();
    }
}