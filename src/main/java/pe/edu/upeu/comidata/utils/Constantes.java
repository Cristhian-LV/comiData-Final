package pe.edu.upeu.comidata.utils;

import java.util.Arrays;
import java.util.List;

public interface Constantes {

    //.......................................................
    // ESTRUCTURAS INTERNAS PARA MENÚS
    //.......................................................

    /**
     * Define la estructura de un MenuItem.
     * key: Clave en el archivo .properties para el nombre (ej: "menuitem.nombre.salir")
     * actionKey: Clave interna para la acción (ej: "SALIR")
     * fxmlPath: Ruta al FXML si es una acción de apertura de pestaña.
     */
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

    /**
     * Define la estructura de un Menu.
     * key: Clave en el archivo .properties para el nombre (ej: "menu.nombre.archivo")
     * items: Lista de MenuItemStruct que contiene (null para menus especiales como Ajustes)
     */
    class MenuStruct {
        public final String key;
        public final List<MenuItemStruct> items;

        public MenuStruct(String key, List<MenuItemStruct> items) {
            this.key = key;
            this.items = items;
        }
    }


    //.......................................................
    // RUTAS FXML:
    //.......................................................
    String fxml_login="/view/login.fxml";
    String fxml_main="/view/admin_main.fxml";
    String fxml_personal="/view/admin_main.fxml"; // Asumiendo que esta es la ruta real de Personal
    String fxml_producto="/view/gestion_productos.fxml";
    String fxml_reporte="/view/main_reporte.fxml";
    String fxml_venta="/view/main_venta.fxml";


    //.......................................................
    // RUTAS IMAGENES & PREFERENCES
    //.......................................................
    String key_path ="pe/edu/upeu/comidata/prefs";
    String key_tema ="appTema";
    String ic_comidata="/img/ic_comidata.png";
    String logo_comidata_black ="/img/logo_comidata_black.png";
    String logo_comidata_white ="/img/logo_comidata_white.png";
    // ... (otras constantes de imagen/tema) ...

    //.......................................................
    // DEFINICIÓN DE MENUS Y MENU ITEMS
    //.......................................................

    // --- ITEM STRUCTS ---
    // ARCHIVO
    MenuItemStruct miDatosUsuario = new MenuItemStruct("menuitem.nombre.datos_usuario", "DATOS_USUARIO");
    MenuItemStruct miDatosNegocio = new MenuItemStruct("menuitem.nombre.datos_negocio", "DATOS_NEGOCIO");
    MenuItemStruct miCerrarSesion = new MenuItemStruct("menuitem.nombre.cerrar_sesion", "CERRAR_SESION");
    MenuItemStruct miSalir = new MenuItemStruct("menuitem.nombre.salir", "SALIR");

    // ADMINISTRACION
    MenuItemStruct miGestionProductos = new MenuItemStruct("menuitem.nombre.gestion_productos", "GESTION_PRODUCTOS", fxml_producto);
    MenuItemStruct miGestionPersonal = new MenuItemStruct("menuitem.nombre.gestion_personal", "GESTION_PERSONAL", fxml_personal);
    MenuItemStruct miGestionInventario = new MenuItemStruct("menuitem.nombre.gestion_inventario", "GESTION_INVENTARIO", fxml_producto); // Asumimos la misma ruta de productos temporalmente

    // CAJA
    MenuItemStruct miPedidoRapido = new MenuItemStruct("menuitem.nombre.pedido_rapido", "PEDIDO_RAPIDO", fxml_venta);
    MenuItemStruct miPedidosCaja = new MenuItemStruct("menuitem.nombre.pedidos", "PEDIDOS_CAJA", fxml_venta);
    MenuItemStruct miGestionClientes = new MenuItemStruct("menuitem.nombre.gestion_clientes", "GESTION_CLIENTES", fxml_producto); // Asumimos ruta temporal
    MenuItemStruct miEstadoInventario = new MenuItemStruct("menuitem.nombre.estado_inventario", "ESTADO_INVENTARIO", fxml_producto);
    MenuItemStruct miInformeCaja = new MenuItemStruct("menuitem.nombre.informe", "INFORME_CAJA", fxml_reporte);

    // REPARTO
    MenuItemStruct miPedidosReparto = new MenuItemStruct("menuitem.nombre.pedidos", "PEDIDOS_REPARTO", fxml_venta);
    MenuItemStruct miInformeReparto = new MenuItemStruct("menuitem.nombre.informe", "INFORME_REPARTO", fxml_reporte);

    // AYUDA
    MenuItemStruct miManualUsuario = new MenuItemStruct("menuitem.nombre.manual_usuario", "MANUAL_USUARIO");
    MenuItemStruct miAcercaDe = new MenuItemStruct("menuitem.nombre.acerca_de", "ACERCA_DE");

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
    String[] miIdiomas=new String[]{"Español","Ingles","Frances"};
    String[] miTemas=new String[]{"Claro","Oscuro","Azul","Verde","Rosado"};
}