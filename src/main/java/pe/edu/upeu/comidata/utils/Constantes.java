package pe.edu.upeu.comidata.utils;

import java.util.Arrays;
import java.util.List;

public interface Constantes {

// RUTAS FXML:
    String fxml_login= "/fxml/login.fxml";
    String fxml_main= "/fxml/main.fxml";
    String fxml_personal= "/fxml/ga_personal.fxml";
    String fxml_producto= "/fxml/ga_productos.fxml";
    String fxml_new_producto= "/fxml/ga_new_producto.fxml";
    String fxml_new_personal= "/fxml/ga_new_personal.fxml";
    String fxml_reporte= "/fxml/.fxml";
    String fxml_venta= "/fxml/pe_pedidos.fxml";


// RUTAS IMAGENES & PREFERENCES
    String key_path ="pe/edu/upeu/comidata/prefs";
    String key_tema ="appTema";
    String ic_comidata="/img/ic_comidata.png";
    String logo_comidata_black ="/img/logo_comidata_black.png";
    String logo_comidata_white ="/img/logo_comidata_white.png";


//.......................................................
// ESTRUCTURAS INTERNAS PARA MENÚS

    class MenuItemStruct {
        public final String key;
        public final String actionKey;
        public final String fxmlPath;

        public MenuItemStruct(String key, String actionKey, String fxmlPath) {
            this.key = key;
            this.actionKey = actionKey;
            this.fxmlPath = fxmlPath;
        }
        public MenuItemStruct(String key, String actionKey) {
            this(key, actionKey, null);
        }
    }

    class MenuStruct {
        public final String key;
        public final List<MenuItemStruct> items;

        public MenuStruct(String key, List<MenuItemStruct> items) {
            this.key = key;
            this.items = items;
        }
    }

//.......................................................
// DEFINICIÓN DE MENUS Y MENU ITEMS

// --- ITEM STRUCTS ---
    // ARCHIVO
    MenuItemStruct miDatosUsuario = new MenuItemStruct("menuitem.nombre.datos_usuario", "DATOS_USUARIO");
    MenuItemStruct miDatosNegocio = new MenuItemStruct("menuitem.nombre.datos_negocio", "DATOS_NEGOCIO");
    MenuItemStruct miCerrarSesion = new MenuItemStruct("menuitem.nombre.cerrar_sesion", "CERRAR_SESION");
    MenuItemStruct miSalir = new MenuItemStruct("menuitem.nombre.salir", "SALIR");

    // ADMINISTRACION
    MenuItemStruct miGestionProductos = new MenuItemStruct("menuitem.nombre.gestion_productos", "GESTION_PRODUCTOS", fxml_producto);
    MenuItemStruct miGestionPersonal = new MenuItemStruct("menuitem.nombre.gestion_personal", "GESTION_PERSONAL", fxml_personal);
    MenuItemStruct miGestionInventario = new MenuItemStruct("menuitem.nombre.gestion_inventario", "GESTION_INVENTARIO", null); // Asumimos la misma ruta de productos temporalmente

    // CAJA
    MenuItemStruct miPedidoRapido = new MenuItemStruct("menuitem.nombre.pedido_rapido", "PEDIDO_RAPIDO", fxml_venta);
    MenuItemStruct miPedidosCaja = new MenuItemStruct("menuitem.nombre.pedidos", "PEDIDOS_CAJA", null);
    MenuItemStruct miGestionClientes = new MenuItemStruct("menuitem.nombre.gestion_clientes", "GESTION_CLIENTES", null); // Asumimos ruta temporal
    MenuItemStruct miEstadoInventario = new MenuItemStruct("menuitem.nombre.estado_inventario", "ESTADO_INVENTARIO", null);
    MenuItemStruct miInformeCaja = new MenuItemStruct("menuitem.nombre.informe", "INFORME_CAJA", null);

    // REPARTO
    MenuItemStruct miPedidosReparto = new MenuItemStruct("menuitem.nombre.pedidos", "PEDIDOS_REPARTO", null);
    MenuItemStruct miInformeReparto = new MenuItemStruct("menuitem.nombre.informe", "INFORME_REPARTO", null);

    // AYUDA
    MenuItemStruct miManualUsuario = new MenuItemStruct("menuitem.nombre.manual_usuario", "MANUAL_USUARIO",null);
    MenuItemStruct miAcercaDe = new MenuItemStruct("menuitem.nombre.acerca_de", "ACERCA_DE",null);

    // --- MENU STRUCTS ---
    MenuStruct menuArchivo = new MenuStruct("menu.nombre.archivo", Arrays.asList(miDatosUsuario, miDatosNegocio, miCerrarSesion, miSalir));
    MenuStruct menuAdministracion = new MenuStruct("menu.nombre.administracion", Arrays.asList(miGestionProductos, miGestionPersonal, miGestionInventario));
    MenuStruct menuCaja = new MenuStruct("menu.nombre.caja", Arrays.asList(miPedidoRapido, miPedidosCaja, miGestionClientes, miEstadoInventario, miInformeCaja));
    MenuStruct menuReparto = new MenuStruct("menu.nombre.reparto", Arrays.asList(miPedidosReparto, miInformeReparto));
    MenuStruct menuAjustes = new MenuStruct("menu.nombre.ajustes", null); // null indica que se genera dinámicamente
    MenuStruct menuAyuda = new MenuStruct("menu.nombre.ayuda", Arrays.asList(miManualUsuario, miAcercaDe));

    // --- ESTRUCTURA POR ROL ---
    String ROL_ADMINISTRADOR = "ADMINISTRADOR";
    String ROL_VENDEDOR = "VENDEDOR";
    String ROL_REPARTIDOR = "REPARTIDOR";

    List<MenuStruct> MENUS_ADMINISTRADOR = Arrays.asList(menuArchivo, menuAdministracion, menuCaja, menuReparto, menuAjustes, menuAyuda);
    List<MenuStruct> MENUS_VENDEDOR = Arrays.asList(menuArchivo, menuCaja, menuAjustes, menuAyuda);
    List<MenuStruct> MENUS_REPARTIDOR = Arrays.asList(menuArchivo, menuReparto, menuAjustes, menuAyuda);

    // Nombres para submenús de Ajustes (No usan claves de Properties)
    String[] miIdiomas=new String[]{"español","ingles","frances"};
    String[] miTemas=new String[]{"claro","oscuro","azul","verde","rosado"};

}