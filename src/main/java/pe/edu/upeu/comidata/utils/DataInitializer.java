package pe.edu.upeu.comidata.utils;

import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pe.edu.upeu.comidata.repository.RolesRepository;
import pe.edu.upeu.comidata.repository.PersonalRepository;
import pe.edu.upeu.comidata.models.RolesDB;
import pe.edu.upeu.comidata.models.PersonalDB;

@Component
public class DataInitializer implements CommandLineRunner {

    // Inyección de dependencias de los repositorios
    private final RolesRepository rolesRepository;
    private final PersonalRepository personalRepository;

    public DataInitializer(RolesRepository rolesRepository, PersonalRepository personalRepository) {
        this.rolesRepository = rolesRepository;
        this.personalRepository = personalRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Iniciando la inicialización de datos (Roles y Personal)...");

        // --- 1. Inicializar Roles ---
        if (rolesRepository.count()==0){
            RolesDB rolAdmin = RolesDB.builder().nombre("ADMINISTRADOR").build();
            RolesDB rolVendedor = RolesDB.builder().nombre("VENDEDOR").build();
            RolesDB rolRepartidor = RolesDB.builder().nombre("REPARTIDOR").build();

            rolesRepository.saveAll(Arrays.asList(rolAdmin, rolVendedor, rolRepartidor));
            System.out.println("✅ Roles inicializados: ADMINISTRADOR, VENDEDOR, REPARTIDOR.");

            // --- 2. Inicializar Personal (Asociando roles) ---

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
            System.out.println("✨ Inicialización de datos completada con éxito.");
        }
    }
}