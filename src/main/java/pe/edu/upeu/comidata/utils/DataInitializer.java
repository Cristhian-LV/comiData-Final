package pe.edu.upeu.comidata.utils;

import java.util.Arrays;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import pe.edu.upeu.comidata.repository.RolesRepository;
import pe.edu.upeu.comidata.repository.PersonalRepository;
import pe.edu.upeu.comidata.repository.CategoriaRepository; // Importación asumida
import pe.edu.upeu.comidata.repository.ProductosRepository; // Importación asumida
import pe.edu.upeu.comidata.repository.InventarioRepository; // Importación asumida

import pe.edu.upeu.comidata.models.RolesDB;
import pe.edu.upeu.comidata.models.PersonalDB;
import pe.edu.upeu.comidata.models.CategoriaDB;
import pe.edu.upeu.comidata.models.ProductosDB;
import pe.edu.upeu.comidata.models.InventarioDB;


@Component
public class DataInitializer implements CommandLineRunner {

    // Inyección de dependencias de los repositorios
    private final RolesRepository rolesRepository;
    private final PersonalRepository personalRepository;
    private final CategoriaRepository categoriaRepository; // Asumida
    private final ProductosRepository productosRepository; // Asumida
    private final InventarioRepository inventarioRepository; // Asumida

    public DataInitializer(RolesRepository rolesRepository,
                           PersonalRepository personalRepository,
                           CategoriaRepository categoriaRepository,
                           ProductosRepository productosRepository,
                           InventarioRepository inventarioRepository) {
        this.rolesRepository = rolesRepository;
        this.personalRepository = personalRepository;
        this.categoriaRepository = categoriaRepository;
        this.productosRepository = productosRepository;
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Iniciando la inicialización de datos (Roles, Personal, Productos e Inventario)...");

        // --- 1. Inicializar Roles y Personal (Lógica existente) ---
        if (rolesRepository.count() == 0) {
            RolesDB rolAdmin = RolesDB.builder().nombre("ADMINISTRADOR").build();
            RolesDB rolVendedor = RolesDB.builder().nombre("VENDEDOR").build();
            RolesDB rolRepartidor = RolesDB.builder().nombre("REPARTIDOR").build();

            rolesRepository.saveAll(Arrays.asList(rolAdmin, rolVendedor, rolRepartidor));
            System.out.println("✅ Roles inicializados: ADMINISTRADOR, VENDEDOR, REPARTIDOR.");

            // Personal 1: Administrador
            PersonalDB admin = PersonalDB.builder()
                    .dni("12345678")
                    .nombresCompletos("Ana Maria Lopez")
                    .nombreUsuario("anam")
                    .correo("admin@comidata.com")
                    .clave("clave123") // ¡Recuerda hashear en producción!
                    .telefono("990000001")
                    .direccion("Av. Central 100")
                    .estado("ACTIVO")
                    .rol(rolAdmin)
                    .build();

            // Personal 2: Vendedor
            PersonalDB vendedor = PersonalDB.builder()
                    .dni("87654321")
                    .nombresCompletos("Carlos Daniel Soto")
                    .nombreUsuario("carlosv")
                    .correo("vendedor@comidata.com")
                    .clave("clave123")
                    .telefono("990000002")
                    .direccion("Calle Los Girasoles 200")
                    .estado("ACTIVO")
                    .rol(rolVendedor)
                    .build();

            // Personal 3: Repartidor
            PersonalDB repartidor = PersonalDB.builder()
                    .dni("98765432")
                    .nombresCompletos("Pedro Luis Ramos")
                    .nombreUsuario("pedror")
                    .correo("repartidor@comidata.com")
                    .clave("clave123")
                    .telefono("990000003")
                    .direccion("Jr. El Sol 300")
                    .estado("ACTIVO")
                    .rol(rolRepartidor)
                    .build();

            personalRepository.saveAll(Arrays.asList(admin, vendedor, repartidor));
            System.out.println("✅ Personal inicializado: admin, carlosv, pedror.");
        }

        // --- 2. Inicializar Categorías, Productos e Inventario ---
        if (productosRepository.count() == 0) {

            // Inicializar categorías primero
            CategoriaDB catBebida = CategoriaDB.builder().nombreCategoria("Bebidas").build();
            CategoriaDB catComida = CategoriaDB.builder().nombreCategoria("Platos Fuertes").build();
            CategoriaDB catPostre = CategoriaDB.builder().nombreCategoria("Postres").build();
            categoriaRepository.saveAll(Arrays.asList(catBebida, catComida, catPostre));
            System.out.println("✅ Categorías inicializadas.");

            // Inicializar Productos con Stock
            ProductosDB p1 = ProductosDB.builder()
                    .nombre("Hamburguesa Clásica")
                    .descripcion("Deliciosa hamburguesa de res, queso y lechuga.")
                    .precio(new BigDecimal("15.50"))
                    .stock(50) // Stock inicial
                    .categoria(catComida)
                    .promocion(null)
                    .build();

            ProductosDB p2 = ProductosDB.builder()
                    .nombre("Refresco de Cola")
                    .descripcion("Lata de 355ml.")
                    .precio(new BigDecimal("4.00"))
                    .stock(100) // Stock inicial
                    .categoria(catBebida)
                    .promocion(null)
                    .build();

            List<ProductosDB> productos = productosRepository.saveAll(Arrays.asList(p1, p2));
            System.out.println("✅ Productos inicializados.");


            // Inicializar movimientos de Inventario para reflejar el stock inicial
            LocalDateTime now = LocalDateTime.now();

            InventarioDB i1 = InventarioDB.builder()
                    .producto(p1)
                    .fechaRegistro(now)
                    .tipoMovimiento("ENTRADA")
                    .cantidadMovimiento(p1.getStock())
                    .costoCompra(new BigDecimal("8.00"))
                    .observaciones("Carga inicial de stock.")
                    .build();

            InventarioDB i2 = InventarioDB.builder()
                    .producto(p2)
                    .fechaRegistro(now)
                    .tipoMovimiento("ENTRADA")
                    .cantidadMovimiento(p2.getStock())
                    .costoCompra(new BigDecimal("2.50"))
                    .observaciones("Carga inicial de stock.")
                    .build();

            inventarioRepository.saveAll(Arrays.asList(i1, i2));
            System.out.println("✅ Registros de inventario iniciales creados.");
        }

        System.out.println("✨ Inicialización de datos completada con éxito.");
    }
}