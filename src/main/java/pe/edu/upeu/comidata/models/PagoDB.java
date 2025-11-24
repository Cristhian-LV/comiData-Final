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
@Table(name = "pago_db")
public class PagoDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @OneToOne // Un pedido puede tener un solo registro de pago principal
    @JoinColumn(name = "id_pedido", nullable = false, unique = true)
    private PedidosDB pedido;

    @Column(name = "monto_pagado", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPagado;

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago; // e.g., "EFECTIVO", "TARJETA", "YAPE"

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "estado_pago", nullable = false)
    private String estadoPago; // e.g., "PAGADO", "NO_PAGADO", "REEMBOLSADO"
}