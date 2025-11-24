package pe.edu.upeu.comidata.utils;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;
import java.util.Properties;
import java.util.prefs.Preferences;

@Component
public class InterfaceManager {

    private final Preferences userPrefs = Preferences.userRoot().node(Constantes.key_path);
    private final UtilsX util = new UtilsX();

    public void aplicarTema(Scene scene, boolean isLogin) {
        if (scene == null) return;

        String tema = userPrefs.get(Constantes.key_tema, "Claro");
        scene.getStylesheets().clear();

        String basePath = "/css/";
        // Si es login, usamos 'login-tema-'. Si es Main, usamos 'tema-'.
        String cssFileName = isLogin ? "login-tema-" : "estilo-";

        String themeSuffix;
        switch (tema) {
            case "Oscuro": themeSuffix = "oscuro.css"; break;
            case "Azul":   themeSuffix = "azul.css"; break;
            case "Verde":  themeSuffix = "verde.css"; break;
            case "Rosado": themeSuffix = "rosado.css"; break;
            default: themeSuffix = "claro.css"; break;
        }

        String cssPath = basePath + cssFileName + themeSuffix;

        try {
            scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Advertencia: No se encontró el archivo CSS: " + cssPath);
        }
    }

    public void aplicarLogoTema(ImageView logoImageView) {
        if (logoImageView == null) return;

        String tema = userPrefs.get(Constantes.key_tema, "Claro");
        String logoPath = Constantes.logo_comidata_white; // Default

        switch (tema) {
            case "Oscuro": logoPath = Constantes.logo_comidata_black; break;
            case "Azul":   logoPath = Constantes.logo_comidata_white; break;
            case "Verde":  logoPath = Constantes.logo_comidata_white; break;
            case "Rosado": logoPath = Constantes.logo_comidata_white; break;
        }

        try {
            Image nuevaImagen = new Image(getClass().getResourceAsStream(logoPath));
            logoImageView.setImage(nuevaImagen);
        } catch (Exception e) {
            System.err.println("Advertencia: No se encontró la imagen del logo para el tema: " + logoPath);
        }
    }

    public void guardarTema(String tema) {
        userPrefs.put(Constantes.key_tema, tema);
    }

    public Properties cambiarIdioma(String idioma) {
        String langCode;
        switch (idioma) {
            case "Ingles": langCode = "en"; break;
            case "Frances": langCode = "fr"; break;
            default: langCode = "es"; break; // Español
        }
        userPrefs.put("IDIOMAX", langCode);
        return util.detectLanguage(langCode);
    }

    public Properties getProperties() {
        return util.detectLanguage(userPrefs.get("IDIOMAX", "es"));
    }
}