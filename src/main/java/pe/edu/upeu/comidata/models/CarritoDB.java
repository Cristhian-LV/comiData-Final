package pe.edu.upeu.comidata.models;

import lombok.*;
import jakarta.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "carrito_db")
public class CarritoDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long idCarrito;

    @ManyToOne
    @JoinColumn(name = "id_personal", nullable = false)
    private PersonalDB personal; // El vendedor o cajero que está manejando el carrito

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductosDB producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}