package pe.edu.upeu.comidata.services;


import pe.edu.upeu.comidata.models.PedidosDB;

public interface IPedidosService extends CrudGenericoService<PedidosDB, Long> {

    PedidosDB registrarPedido(Long idPersonal, Long idCliente, String tipoPedido, String tipoDocumento);
}