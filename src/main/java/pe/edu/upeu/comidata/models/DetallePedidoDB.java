package pe.edu.upeu.comidata.models;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "detalle_pedido_db")
public class DetallePedidoDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_pedido")
    private Long idDetallePedido;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidosDB pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductosDB producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio; // Precio unitario al momento de la venta

    @Column(name = "descuento", precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal; // Precio * Cantidad - Descuento

    @Column(name = "tasa_igv", nullable = false, precision = 5, scale = 4)
    private BigDecimal tasaIgv; // IGV aplicado (e.g., 0.18 para 18%)
}