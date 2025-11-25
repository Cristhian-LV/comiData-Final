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

    // Se mantiene ManyToOne, un Producto puede tener muchos registros de inventario (movimientos)
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductosDB producto;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    // >> INICIO CAMBIO: Indica si el movimiento es de ENTRADA (+) o SALIDA (-)
    @Column(name = "tipo_movimiento", nullable = false, length = 10)
    private String tipoMovimiento; // "ENTRADA" o "SALIDA"

    // La cantidad del movimiento (siempre positiva, el tipo_movimiento define el signo)
    @Column(name = "cantidad_movimiento", nullable = false)
    private Integer cantidadMovimiento;
    // << FIN CAMBIO

    @Column(name = "costo_compra", precision = 10, scale = 2)
    private BigDecimal costoCompra; // Costo por unidad si se aumenta el stock (solo para ENTRADA)

    @Column(name = "observaciones", length = 500)
    private String observaciones;
}