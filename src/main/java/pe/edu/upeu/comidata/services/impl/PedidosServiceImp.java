package pe.edu.upeu.comidata.services.impl;

// pe.edu.upeu.comidata.services.impl.PedidosServiceImp

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pe.edu.upeu.comidata.models.*;
import pe.edu.upeu.comidata.repository.*;
import pe.edu.upeu.comidata.services.IPedidosService;
import pe.edu.upeu.comidata.services.ProductoService; // Necesario para actualizar stock

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidosServiceImp extends CrudGenericoServiceImp<PedidosDB, Long> implements IPedidosService {

    private final PedidosRepository pedidosRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoService productosService; // Asumo que tienes un servicio de Productos

    @Override
    protected JpaRepository<PedidosDB, Long> getRepo() {
        return pedidosRepository;
    }

    @Transactional
    @Override
    public PedidosDB registrarPedido(Long idPersonal, Long idCliente, String tipoPedido, String tipoDocumento) {
        // 1. Obtener los ítems del carrito del Personal
        List<CarritoDB> itemsCarrito = carritoRepository.findByPersonalIdPersonal(idPersonal);

        if (itemsCarrito.isEmpty()) {
            throw new RuntimeException("El carrito del personal está vacío. No se puede registrar el pedido.");
        }

        // 2. Crear el Pedido (Venta)
        PersonalDB personal = PersonalDB.builder().idPersonal(idPersonal).build();
        ClientesDB cliente = ClientesDB.builder().idCliente(idCliente).build();

        // Calcular Totales (Base Imponible, IGV, Total)
        BigDecimal total = BigDecimal.ZERO;

        // Lógica de cálculo (ej. IGV fijo al 18%)
        final BigDecimal TASA_IGV = new BigDecimal("0.1800"); // [cite: 391]

        for (CarritoDB item : itemsCarrito) {
            BigDecimal subtotal = item.getProducto().getPrecio()
                    .multiply(new BigDecimal(item.getCantidad()));
            total = total.add(subtotal);
        }

        // Nota: La lógica para obtener el número de documento y calcular impuestos complejos
        // debería ir aquí, pero lo simplificamos por el momento.

        PedidosDB pedido = PedidosDB.builder()
                .personal(personal)
                .cliente(cliente)
                .fechaHoraPedido(LocalDateTime.now())
                .tipoPedido(tipoPedido)
                .estadoPedido("ENTREGADO") // Se marca como entregado al registrarse
                .tipoDocumento(tipoDocumento)
        //.numeroDocumento(generarNumeroDocumento())
                .build();

        pedido = pedidosRepository.save(pedido);

        // 3. Crear los DetallePedidoDB y actualizar el Stock
        for (CarritoDB item : itemsCarrito) {
            ProductosDB producto = item.getProducto();
            Integer cantidad = item.getCantidad();
            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotalItem = precioUnitario.multiply(new BigDecimal(cantidad));

            // Crear Detalle
            DetallePedidoDB detalle = DetallePedidoDB.builder()
                    .pedido(pedido)
                    .producto(producto)
                    .cantidad(cantidad)
                    .precio(precioUnitario)
                    .subtotal(subtotalItem)
                    .tasaIgv(TASA_IGV)
                    .descuento(BigDecimal.ZERO)
                    .build();

            detallePedidoRepository.save(detalle);

            // 4. Actualizar Stock (Restar la cantidad vendida)
            int nuevoStock = producto.getStock() - cantidad;
            if (nuevoStock < 0) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }
            producto.setStock(nuevoStock);
            productosService.save(producto);
        }

        // 5. Vaciar el Carrito
        carritoRepository.deleteByPersonalIdPersonal(idPersonal);

        return pedido;
    }
}
