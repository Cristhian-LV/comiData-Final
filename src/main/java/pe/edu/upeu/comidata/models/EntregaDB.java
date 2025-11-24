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
@Table(name = "entrega_db")
public class EntregaDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrega")
    private Long idEntrega;

    @OneToOne
    @JoinColumn(name = "id_pedido", nullable = false, unique = true)
    private PedidosDB pedido;

    @ManyToOne
    @JoinColumn(name = "id_repartidor") // Puede ser nulo si el pedido es para "TIENDA"
    private PersonalDB repartidor;

    @Column(name = "costo_delivery", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoDelivery;

    @Column(name = "fecha_hora_salida")
    private LocalDateTime fechaHoraSalida;

    @Column(name = "fecha_hora_entrega")
    private LocalDateTime fechaHoraEntrega;

    @Column(name = "estado_entrega", nullable = false)
    private String estadoEntrega; // e.g., "PENDIENTE", "EN_RUTA", "ENTREGADO", "FALLIDO"
}