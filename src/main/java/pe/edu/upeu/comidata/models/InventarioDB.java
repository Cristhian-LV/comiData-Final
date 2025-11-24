package pe.edu.upeu.comidata.models;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "inventario_db")
public class InventarioDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Long idInventario;

    @OneToOne // o ManyToOne si un producto tiene varios registros de inventario (histórico)
    @JoinColumn(name = "id_producto", nullable = false, unique = true)
    private ProductosDB producto;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad; // Stock actual

    @Column(name = "costo_compra", precision = 10, scale = 2)
    private BigDecimal costoCompra; // Costo por unidad si se aumenta el stock
}