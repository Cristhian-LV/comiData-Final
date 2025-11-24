package pe.edu.upeu.comidata.models;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "productos_db")
public class ProductosDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio; // Precio de venta sugerido

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaDB categoria;

    @ManyToOne
    @JoinColumn(name = "id_promocion") // Puede ser nulo si no hay promoción
    private PromocionesDB promocion;

    // Nota: El inventario se gestiona a través de la tabla InventarioDB (OneToOne/OneToMany)
    // Para simplificar la relación, InventarioDB tendrá la FK a ProductosDB.
}