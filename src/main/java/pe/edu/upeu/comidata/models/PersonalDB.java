package pe.edu.upeu.comidata.models;

import lombok.*;
import jakarta.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "personal_db")
public class PersonalDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_personal")
    private Long idPersonal;

    @Column(name = "dni", unique = true, length = 8, nullable = false)
    private String dni;

    @Column(name = "nombres_completos", nullable = false)
    private String nombresCompletos;

    @Column(name = "nombre_usuario", unique = true, nullable = false)
    private String nombreUsuario;

    @Column(name = "correo", unique = true, nullable = false)
    private String correo;

    @Column(name = "clave", nullable = false)
    private String clave; // La contraseña debe ser hasheada antes de guardar

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "estado", nullable = false)
    private String estado; // e.g., "ACTIVO", "DESPEDIDO"

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private RolesDB rol;

    // Nota: La gestión de permisos se haría con una tabla 'PermisosDB' y una
    // relación Many-to-Many aquí. Se omite 'PermisosDB' por simplicidad,
    // pero la columna lógica es 'permisos'.
}