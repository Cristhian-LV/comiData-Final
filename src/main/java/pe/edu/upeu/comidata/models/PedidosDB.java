package pe.edu.upeu.comidata.models;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "pedidos_db")
public class PedidosDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    @ManyToOne
    @JoinColumn(name = "id_personal", nullable = false)
    private PersonalDB personal; // Vendedor/Cajero que tomó el pedido

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClientesDB cliente;

    @Column(name = "fecha_hora_pedido", nullable = false)
    private LocalDateTime fechaHoraPedido;

    @Column(name = "tipo_pedido", nullable = false)
    private String tipoPedido; // e.g., "TIENDA", "DELIVERY"

    @Column(name = "estado_pedido", nullable = false)
    private String estadoPedido; // e.g., "PENDIENTE", "EN_CAMINO", "ENTREGADO", "CANCELADO"

    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumento; // e.g., "BOLETA", "FACTURA"

    @Column(name = "numero_documento", unique = true)
    private String numeroDocumento;
}