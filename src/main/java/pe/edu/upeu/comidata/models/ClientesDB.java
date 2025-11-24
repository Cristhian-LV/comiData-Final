package pe.edu.upeu.comidata.models;

import lombok.*;
import jakarta.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "clientes_db")
public class ClientesDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "dni", unique = true, length = 8)
    private String dni;

    @Column(name = "nombres_completos", nullable = false)
    private String nombresCompletos;

    @Column(name = "correo")
    private String correo;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "ultima-direccion")
    private String ultimaDireccion;
}