package pe.edu.upeu.comidata.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import pe.edu.upeu.comidata.utils.Constantes;
import pe.edu.upeu.comidata.utils.InterfaceManager;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Controller
public class MainController {
    @Autowired private ConfigurableApplicationContext applicationContext; // Contexto de Spring
    @Autowired private InterfaceManager interfaceManager;

    @FXML private BorderPane bpAdminMain;
    @FXML private MenuBar menuBarAdmin;
    @FXML private TabPane tabPaneAdmin;

    private String userRole = Constantes.ROL_ADMINISTRADOR;

    @FXML
    public void initialize() {
        crearMenuBar();
        actualizarInterfaz();
        Platform.runLater(() -> {
            //cambiarEstilo(userPrefs.get(Constantes.key_tema, "Claro"));
            interfaceManager.aplicarTema(bpAdminMain.getScene(),false);
        });
        //abrirTabConFXML(Constantes.fxml_producto, "Gestionar Productos");
//        abrirTabConFXML("/fxml/gestion_usuarios.fxml", "Gestionar Usuarios");
//        abrirTabConFXML("/fxml/main_venta.fxml", "Gestionar Ventas");
//        abrirTabConFXML("/fxml/gestion_productos.fxml", "Gestionar Productos");
//        abrirTabConFXML("/fxml/main_reporte.fxml", "Reporte ventas");
    }

// --- Configuración de Menús ---
private void crearMenuBar() {
    // Limpiamos los menús existentes (esencial para el cambio de idioma/rol)
    menuBarAdmin.getMenus().clear();

    // 1. Obtener los menús según el rol del usuario
    List<Constantes.MenuStruct> menus = obtenerMenusPorRol(userRole);
    Properties currentProperties = interfaceManager.getProperties();

    // 2. Iterar sobre la estructura de menús
    for (Constantes.MenuStruct menuStruct : menus) {

        // Usar la clave de idioma para obtener el nombre del menú
        String menuName = currentProperties.getProperty(menuStruct.key, "Missing Menu Name");
        Menu menu = new Menu(menuName);

        if (menuStruct.items != null) {
            // Menús normales con items definidos en la estructura
            for (Constantes.MenuItemStruct itemStruct : menuStruct.items) {
                crearMenuItemEstandar(menu, itemStruct, currentProperties);
            }
        } else if (menuStruct.key.equals("menu.nombre.ajustes")) {
            // Menú de Ajustes: Se generan submenús de Idioma y Tema (es la excepción)
            crearMenuAjustes(menu);
        }

        menuBarAdmin.getMenus().add(menu);
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
        // Usar la clave de idioma para obtener el nombre del MenuItem
        String itemName = properties.getProperty(itemStruct.key, "Missing Item Name");
        MenuItem mi = new MenuItem(itemName);

        // Asignar una acción centralizada
        mi.setOnAction(e -> manejarAccionMenu(itemStruct));
        parentMenu.getItems().add(mi);
    }

    private void crearMenuAjustes(Menu menuAjustes) {
        Properties currentProperties = interfaceManager.getProperties();

        // Submenú Idioma
        Menu mIdioma = new Menu(currentProperties.getProperty("menu.nombre.idioma"));
        for (String idioma: Constantes.miIdiomas){
            MenuItem mi=new MenuItem(idioma);
            mi.setOnAction(actionEvent -> cambiarIdioma(idioma));
            mIdioma.getItems().add(mi);
        }
        menuAjustes.getItems().add(mIdioma);

        // Submenú Tema
        Menu mTema = new Menu(currentProperties.getProperty("menu.nombre.tema"));
        for (String tema: Constantes.miTemas){
            MenuItem mi=new MenuItem(tema);
            mi.setOnAction(actionEvent -> cambiarTema(tema));
            mTema.getItems().add(mi);
        }
        menuAjustes.getItems().add(mTema);
    }

    // --- Manejo Centralizado de Acciones ---

    private void manejarAccionMenu(Constantes.MenuItemStruct itemStruct) {
        // 1. Manejar acciones directas (Cerrar sesión, Salir, etc.)
        switch (itemStruct.actionKey) {
            case "CERRAR_SESION":
                cerrarSesion();
                return;
            case "SALIR":
                salirAplicacion();
                return;
            case "DATOS_USUARIO":
                System.out.println("Abriendo datos de usuario");
                return;
            case "DATOS_NEGOCIO":
                System.out.println("Abriendo datos del negocio");
                return;
            case "MANUAL_USUARIO":
                System.out.println("Abriendo Manual de Usuario");
                return;
            case "ACERCA_DE":
                System.out.println("Mostrando Acerca De");
                return;
            default:
                // Continuar a la lógica de Tab
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

    // --- Lógica de Interfaz ---

    private void cambiarTema(String tema) {
        interfaceManager.guardarTema(tema);
        // Aplicar el tema completo de la aplicación principal
        interfaceManager.aplicarTema(bpAdminMain.getScene(), false);
    }

    private void cambiarIdioma(String idioma) {
        interfaceManager.cambiarIdioma(idioma);
        actualizarInterfaz(); // Redibuja todos los textos
    }

    private void actualizarInterfaz(){
        // Al cambiar el idioma, recreamos completamente la MenuBar
        // para que todos los textos se muestren en el idioma seleccionado.
        crearMenuBar();
    }

    private void cerrarSesion() {
        System.out.println("Cerrando sesión...");

        try {
            Stage stage = StageManager.getPrimaryStage();
            if (stage == null) {
                stage = (Stage) bpAdminMain.getScene().getWindow();
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constantes.fxml_login));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent loginRoot = fxmlLoader.load();
            Scene scene = new Scene(loginRoot);

            LoginController loginController = fxmlLoader.getController();

            // ¡IMPORTANTE! Usar TRUE para aplicar el CSS de Login (Solo fondo)
            interfaceManager.aplicarTema(scene, false);

            loginController.aplicarLogoTema();

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

    // El resto de los métodos como abrirTabConFXML se mantiene
    private void abrirTabConFXML(String fxmlPath, String tituloTab) {
        // Buscar si ya existe una pestaña con ese título
        for (Tab tab : tabPaneAdmin.getTabs()) {
            if (tab.getText().equals(tituloTab)) {
                tabPaneAdmin.getSelectionModel().select(tab); // Seleccionar la existente
                return; // Salir, no crear una nueva
            }
        }
        // Si no existe, crear la nueva pestaña
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(applicationContext::getBean); // IMPORTANTE para Spring
            Parent root = loader.load();

            Tab nuevaPestana = new Tab(tituloTab);
            nuevaPestana.setContent(root); // Añadir el contenido cargado

            tabPaneAdmin.getTabs().add(nuevaPestana); // Añadir la nueva pestaña
            tabPaneAdmin.getSelectionModel().select(nuevaPestana); // Seleccionarla

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
