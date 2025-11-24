package pe.edu.upeu.comidata;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pe.edu.upeu.comidata.components.StageManager;
import pe.edu.upeu.comidata.controllers.LoginController;
import pe.edu.upeu.comidata.utils.Constantes;
import pe.edu.upeu.comidata.utils.InterfaceManager;


@SpringBootApplication
public class ComiDataApplication extends Application {


    private ConfigurableApplicationContext applicationContext;
    private Parent parent;
    private FXMLLoader fxmlLoader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ComiDataApplication.class);
        builder.application().setWebApplicationType(WebApplicationType.NONE);
        applicationContext=builder.run(getParameters().getRaw().toArray(new String[0]));

        fxmlLoader = new FXMLLoader(getClass().getResource(Constantes.fxml_login));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        parent = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(parent);

        InterfaceManager interfaceManager = applicationContext.getBean(InterfaceManager.class);
        LoginController loginController = fxmlLoader.getController();
        interfaceManager.aplicarTema(scene,false);
        loginController.aplicarLogoTema();

        StageManager.setPrimaryStage(stage);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource(Constantes.ic_comidata).toExternalForm()));
        stage.setTitle("comiData - Login");
        stage.setResizable(false);
        stage.show();
    }

}
