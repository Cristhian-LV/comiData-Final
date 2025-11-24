package pe.edu.upeu.comidata.models;

import lombok.*;
import jakarta.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roles_db")
public class RolesDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre; // e.g., "ADMINISTRADOR", "VENDEDOR", "REPARTIDOR"
}