package pe.edu.upeu.comidata.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.comidata.components.StageManager;
import pe.edu.upeu.comidata.dto.SessionManager;
import pe.edu.upeu.comidata.models.PersonalDB;
import pe.edu.upeu.comidata.services.PersonalService;
import pe.edu.upeu.comidata.utils.Constantes;
import pe.edu.upeu.comidata.utils.InterfaceManager;

import java.io.IOException;
import java.util.Properties;

@Controller
public class LoginController {

    // --- Servicios de Spring ---
    @Autowired private PersonalService personalService;
    @Autowired private ConfigurableApplicationContext applicationContext;
    @Autowired private InterfaceManager interfaceManager;

    @FXML private Label lblTituloLogin;
    @FXML private Label lblCorreoLogin;
    @FXML private Label lblPasswordLogin;
    @FXML private TextField txtCorreoLogin;
    @FXML private PasswordField txtPasswordLogin;
    @FXML private Label lblCorreoVacioLogin;
    @FXML private Label lblPasswordVacioLogin;
    @FXML private Label lblErrorLogin;
    @FXML private Button btnLogin;
    @FXML private Label lblRegister;

    @FXML private ImageView imgLogoLogin;

    @FXML
    public void initialize() {

        btnLogin.setOnAction(e -> entrarSistema());
        lblRegister.setOnMouseClicked(e -> registrarUsuario());

        actualizarInterfazLogin();
    }

    public void aplicarLogoTema() {
        if (imgLogoLogin != null) {
            interfaceManager.aplicarLogoTema(imgLogoLogin);
        } else {
            System.err.println("Advertencia: imgLogoLogin no está inyectado en LoginController.");
        }
    }

    private void entrarSistema() {

        String user = txtCorreoLogin.getText().trim();
        String pass = txtPasswordLogin.getText().trim();

        if (!validarCampos(user, pass)) {
            return;
        }
        PersonalDB personal = personalService.login(user, pass);

        if (personal == null){
            lblErrorLogin.setVisible(true);
            return;
        }

        System.out.println("Login exitoso para: " + personal.getNombreUsuario() + " (Rol: " + personal.getRol().getNombre() + ")");

        SessionManager.getInstance().setUserId(personal.getIdPersonal());
        SessionManager.getInstance().setUserName(personal.getNombreUsuario());
        SessionManager.getInstance().setUserRol(personal.getRol().getNombre());

        cargarEscena(Constantes.fxml_main, "comiData");
    }

    private void cargarEscena(String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlLoader.load();

            Stage stage = StageManager.getPrimaryStage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResource(Constantes.ic_comidata).toExternalForm()));
            stage.setTitle(title);
            stage.centerOnScreen();
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validarCampos(String user, String pass) {
        lblCorreoVacioLogin.setVisible(false);
        lblPasswordVacioLogin.setVisible(false);
        lblErrorLogin.setVisible(false);

        boolean valid = true;

        if (user.isEmpty()) {
            lblCorreoVacioLogin.setVisible(true);
            valid = false;
        }
        if (pass.isEmpty()) {
            lblPasswordVacioLogin.setVisible(true);
            valid = false;
        }
        return valid;
    }

    private void registrarUsuario() {
        System.out.println("Usuario registrado");
    }

    public void actualizarInterfazLogin() {
        Properties properties = interfaceManager.getProperties();

        lblTituloLogin.setText(properties.getProperty("login.label.titulo", "Iniciar Sesión"));
        lblCorreoLogin.setText(properties.getProperty("login.label.correo", "Correo o usuario:"));
        lblPasswordLogin.setText(properties.getProperty("login.label.clave", "Contraseña:"));
        lblCorreoVacioLogin.setText(properties.getProperty("login.error.correo", "Campo necesario"));
        lblPasswordVacioLogin.setText(properties.getProperty("login.error.clave", "Campo necesario"));
        btnLogin.setText(properties.getProperty("login.button.entrar", "Entrar"));
        lblRegister.setText(properties.getProperty("login.label.registro", "¿Aún no tienes cuenta? Regístrate"));
        lblErrorLogin.setText(properties.getProperty("login.error.credenciales", "Credenciales incorrectas"));
        txtCorreoLogin.setPromptText(properties.getProperty("login.placeholder.correo", "Ingrese su correo o usuario"));
        txtPasswordLogin.setPromptText(properties.getProperty("login.placeholder.clave", "Ingrese su contraseña"));

        aplicarLogoTema();
    }
}