package pe.edu.upeu.comidata.models;

import lombok.*;
import jakarta.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "configuraciones_db")
public class ConfiguracionesDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuracion")
    private Long idConfiguracion;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "valor", nullable = false)
    private String valor;
}