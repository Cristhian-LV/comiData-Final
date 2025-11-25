package pe.edu.upeu.comidata.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.comidata.components.StageManager;
import pe.edu.upeu.comidata.dto.SessionManager;
import pe.edu.upeu.comidata.utils.Constantes;
import pe.edu.upeu.comidata.utils.InterfaceManager;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Controller
public class MainController {
    @Autowired private ConfigurableApplicationContext applicationContext; // Contexto de Spring
    @Autowired private InterfaceManager interfaceManager;

    @FXML private BorderPane borderPaneMain;
    @FXML private MenuBar menuBarMain;
    @FXML private TabPane tabPaneMain;

    private String userRole;

    @FXML
    public void initialize() {

        this.userRole = SessionManager.getInstance().getUserRol();
        if (this.userRole == null || this.userRole.isEmpty()) {
            // Fallback: si por alguna razón no se establece, asumimos el rol principal
            this.userRole = Constantes.ROL_ADMINISTRADOR;
        }

        crearMenuBar();
        actualizarInterfaz();
        Platform.runLater(() -> {
            interfaceManager.aplicarTema(borderPaneMain.getScene(),false);
        });

        // abrirTabConFXML(Constantes.fxml_producto, "Gestionar Productos");
        // abrirTabConFXML("/fxml/main_reporte.fxml", "Reporte ventas");
    }

    // --- Configuración de Menús ---
    private void crearMenuBar() {
        menuBarMain.getMenus().clear();

        List<Constantes.MenuStruct> menus = obtenerMenusPorRol(userRole);
        Properties currentProperties = interfaceManager.getProperties();

        for (Constantes.MenuStruct menuStruct : menus) {

            String menuName = currentProperties.getProperty(menuStruct.key, "Missing Menu Name");
            Menu menu = new Menu(menuName);

            if (menuStruct.items != null) {
                for (Constantes.MenuItemStruct itemStruct : menuStruct.items) {
                    crearMenuItemEstandar(menu, itemStruct, currentProperties);
                }
            } else if (menuStruct.key.equals("menu.nombre.ajustes")) {
                crearMenuAjustes(menu);
            }
            menuBarMain.getMenus().add(menu);
        }
    }

    private List<Constantes.MenuStruct> obtenerMenusPorRol(String rol) {
        switch (rol) {
            case Constantes.ROL_VENDEDOR:
                return Constantes.MENUS_VENDEDOR;
            case Constantes.ROL_REPARTIDOR:
                return Constantes.MENUS_REPARTIDOR;
            case Constantes.ROL_ADMINISTRADOR:
            default:
                return Constantes.MENUS_ADMINISTRADOR;
        }
    }

    private void crearMenuItemEstandar(Menu parentMenu, Constantes.MenuItemStruct itemStruct, Properties properties) {
        String itemName = properties.getProperty(itemStruct.key, "Missing Item Name");
        MenuItem mi = new MenuItem(itemName);

        mi.setOnAction(e -> manejarAccionMenu(itemStruct));
        parentMenu.getItems().add(mi);
    }

    private void crearMenuAjustes(Menu menuAjustes) {
        Properties currentProperties = interfaceManager.getProperties();

        Menu mIdioma = new Menu(currentProperties.getProperty("menu.nombre.idioma"));
        for (String idioma: Constantes.miIdiomas){
            MenuItem mi=new MenuItem(currentProperties.getProperty("idioma."+idioma));
            mi.setOnAction(actionEvent -> cambiarIdioma(idioma));
            mIdioma.getItems().add(mi);
        }
        menuAjustes.getItems().add(mIdioma);

        Menu mTema = new Menu(currentProperties.getProperty("menu.nombre.tema"));
        for (String tema: Constantes.miTemas){
            MenuItem mi=new MenuItem(currentProperties.getProperty("tema."+tema));
            mi.setOnAction(actionEvent -> cambiarTema(tema));
            mTema.getItems().add(mi);
        }
        menuAjustes.getItems().add(mTema);
    }

    private void manejarAccionMenu(Constantes.MenuItemStruct itemStruct) {

        switch (itemStruct.actionKey) {
            case "CERRAR_SESION":
                cerrarSesion();
                break;
            case "SALIR":
                salirAplicacion();
                break;
            case "GESTION_PRODUCTOS":
                break;
            case "DATOS_USUARIO":
                break;
            case "DATOS_NEGOCIO":
                break;
            case "MANUAL_USUARIO":
                break;
            case "ACERCA_DE":
                break;
            default:
                break;
        }

        // 2. Manejar apertura de Tabs (si tiene FXML Path)
        if (itemStruct.fxmlPath != null) {
            // Usamos la clave del item para obtener el título traducido de la pestaña
            Properties currentProperties = interfaceManager.getProperties();
            String tabTitle = currentProperties.getProperty(itemStruct.key, "Pestaña Sin Título");
            abrirTabConFXML(itemStruct.fxmlPath, tabTitle);
        }
    }

//Lógica de Interfaz ---------------------------------------------------------------------------------

    private void cambiarTema(String tema) {
        interfaceManager.guardarTema(tema);
        // Aplicar el tema completo de la aplicación principal
        interfaceManager.aplicarTema(borderPaneMain.getScene(), false);
    }

    private void cambiarIdioma(String idioma) {
        interfaceManager.cambiarIdioma(idioma);
        actualizarInterfaz();
    }

    private void actualizarInterfaz(){
        crearMenuBar();
    }

    private void cerrarSesion() {
        System.out.println("Cerrando sesión...");

        try {
            Stage stage = StageManager.getPrimaryStage();
            if (stage == null) {
                stage = (Stage) borderPaneMain.getScene().getWindow();
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constantes.fxml_login));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent loginRoot = fxmlLoader.load();
            Scene scene = new Scene(loginRoot);

            LoginController loginController = fxmlLoader.getController();

            interfaceManager.aplicarTema(scene, false); //true para usar otro fondo en el login

            loginController.actualizarInterfazLogin();

            stage.setScene(scene);
            stage.setTitle("comiData - Login");
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al volver a la pantalla de login.");
        }
    }

    private void salirAplicacion() {
        Platform.exit();
        System.exit(0);
    }

    private void abrirTabConFXML(String fxmlPath, String tituloTab) {
        // Buscar si ya existe una pestaña con ese título
//        for (Tab tab : tabPaneMain.getTabs()) {
//            if (tab.getText().equals(tituloTab)) {
//                tabPaneMain.getSelectionModel().select(tab);
//                return;
//            }
//        }
        tabPaneMain.getTabs().clear();
        // Si no existe, crear la nueva pestaña
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(applicationContext::getBean); // IMPORTANTE para Spring
            Parent root = loader.load();

            Tab nuevaPestana = new Tab(tituloTab);
            nuevaPestana.setContent(root); // Añadir el contenido cargado
            nuevaPestana.setClosable(false);

            tabPaneMain.getTabs().add(nuevaPestana); // Añadir la nueva pestaña
            tabPaneMain.getSelectionModel().select(nuevaPestana); // Seleccionarla

        } catch (IOException e) {
            e.printStackTrace();
            // Mostrar un Alert al usuario
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al Cargar Módulo");
            alert.setHeaderText("No se pudo cargar la vista: " + tituloTab);
            alert.setContentText("Detalle: " + e.getMessage());
            alert.showAndWait();
        }
    }

}
