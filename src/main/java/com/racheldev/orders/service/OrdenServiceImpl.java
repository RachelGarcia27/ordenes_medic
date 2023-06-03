package com.racheldev.orders.service;

import com.racheldev.orders.entity.Orden;
import com.racheldev.orders.entity.Producto;
import com.racheldev.orders.entity.ProductoOrden;
import com.racheldev.orders.repository.OrdenRepository;
import com.racheldev.orders.repository.ProductoOrdenRepository;
import com.racheldev.orders.repository.ProductoRepository;
import com.racheldev.orders.type.exception.ModelException;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdenServiceImpl implements OrdenService {

    /**
     * Variable que apunta a la bitácora de la clase.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(OrdenServiceImpl.class);

    @Resource
    private OrdenRepository ordenRepository;
    
    @Resource
    private ProductoOrdenRepository productoOrdenRepository;
    
    @Resource
    private ProductoRepository productoRepository;
    
    @Resource
    private ProductoService productoService;

    @Transactional
    @Override
    public Orden create(Orden dto) {
        LOGGER.debug("Creando un Orden con información: " + dto);
        return ordenRepository.save(dto);
    }

    @Transactional(rollbackFor = ModelException.class)
    @Override
    public Orden delete(Long id) throws ModelException {
        LOGGER.debug("Borrando el Orden con id: " + id);

        Optional<Orden> dao = ordenRepository.findById(id);
        if (dao.get() == null) {
            LOGGER.debug("No se encuentra a el Orden con id: " + id);
            throw new ModelException(ModelException.ERROR_RECORD_NOT_FOUND);
        }

        ordenRepository.delete(dao.get());

        return dao.get();
    }

    @Transactional(rollbackFor = ModelException.class)
    @Override
    public Orden update(Orden dto) throws ModelException {
        LOGGER.debug("Acualizando Orden con información: " + dto);

        Orden dao = ordenRepository.findByNumeroOrden(dto.getNumeroOrden());

//        if (!dao.isPresent()) {
//            LOGGER.debug("No se encontró Orden: " + dto.getNumeroOrden());
//            throw new ModelException(ModelException.ERROR_RECORD_NOT_FOUND);
//        }
        
        Orden actualizado = new Orden();
        actualizado.setNumeroOrden(dao.getNumeroOrden());
        actualizado.setCveCliente(dao.getCveCliente());
        actualizado.setFecha(dao.getFecha());
        actualizado.setSubTotal(dao.getSubTotal());
        actualizado.setIva(dao.getIva());
        actualizado.setTotal(dao.getTotal());
        actualizado.setEstatus(dao.getEstatus());
        
        dao.update(actualizado);

        return dao;
    }
    
    @Transactional(readOnly = true)
    @Override
    public Orden findById(Long id) {
        LOGGER.debug("Buscando a Orden con id: " + id);
        return ordenRepository.findById(id).get();
    }
    
    
    @Transactional(readOnly = true)
	@Override
	public List<Orden> findAll() {
		LOGGER.debug("Buscando a todas los Orden");
        List<Orden> listaOrdenes
        = ordenRepository.findByEstatus("Por Enviar");
        return listaOrdenes;
	}
    
    @Transactional(readOnly = true)
	@Override
    public Long findMaxByNumeroOrden() {
    	return ordenRepository.findMaxByNumeroOrden();
    }

    @Transactional(rollbackFor = ModelException.class)
	@Override
	public Orden enviarOrden(Long numeroOrden) throws ModelException {
    	
        List<ProductoOrden> listaProductoOrden
        = productoOrdenRepository.findByNumeroOrden(numeroOrden);
        
        for(ProductoOrden productoOrden : listaProductoOrden) {
        	Producto producto = productoRepository.findByCodProducto(productoOrden.getCodProducto());
        	producto.setExistencia(producto.getExistencia() - productoOrden.getCantidad());
        	productoService.update(producto);
        }
        
        Orden orden = ordenRepository.findByNumeroOrden(numeroOrden);
        orden.setEstatus("Enviado");
        
        this.update(orden);
        
		return orden;
	}


}
