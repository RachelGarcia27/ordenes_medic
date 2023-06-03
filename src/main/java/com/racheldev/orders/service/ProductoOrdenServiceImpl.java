package com.racheldev.orders.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.racheldev.orders.entity.Orden;
import com.racheldev.orders.entity.Producto;
import com.racheldev.orders.entity.ProductoOrden;
import com.racheldev.orders.entity.ProductoOrdenDTO;
import com.racheldev.orders.repository.OrdenRepository;
import com.racheldev.orders.repository.ProductoOrdenRepository;
import com.racheldev.orders.repository.ProductoRepository;
import com.racheldev.orders.type.exception.ModelException;

@Service
public class ProductoOrdenServiceImpl implements ProductoOrdenService{

    /**
     * Variable que apunta a la bitácora de la clase.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ProductoServiceImpl.class);

    @Resource
    private ProductoOrdenRepository productoOrdenRepository;
    
    @Resource
    private ProductoRepository productoRepository;
    
    @Resource
    private OrdenRepository ordenRepository;
    
    @Transactional
    @Override
    public ProductoOrden create(ProductoOrden dto) {
        LOGGER.debug("Creando un ProductoOrden con información: " + dto);
        ProductoOrden dao = productoOrdenRepository.save(dto);
        actualizaTotalesOrden(dto.getNumeroOrden());
        
        return dao;
    }

    @Transactional(rollbackFor = ModelException.class)
    @Override
    public ProductoOrden delete(Long id) throws ModelException {
        LOGGER.debug("Borrando el ProductoOrden con id: " + id);

        Optional<ProductoOrden> dao = productoOrdenRepository.findById(id);
        if (dao.get() == null) {
            LOGGER.debug("No se encuentra a el Producto con id: " + id);
            throw new ModelException(ModelException.ERROR_RECORD_NOT_FOUND);
        }

        Long numeroOrden = dao.get().getNumeroOrden();
        
        productoOrdenRepository.delete(dao.get());
        
        actualizaTotalesOrden(numeroOrden);
        
        return dao.get();
    }

    @Transactional(rollbackFor = ModelException.class)
    @Override
    public ProductoOrden update(ProductoOrden dto) throws ModelException {
        LOGGER.debug("Acualizando ProductoOrden con información: " + dto);

        Optional<ProductoOrden> dao = productoOrdenRepository.findById(dto.getId());

        if (!dao.isPresent()) {
            LOGGER.debug("No se encontró Orden: " + dto.getId());
            throw new ModelException(ModelException.ERROR_RECORD_NOT_FOUND);
        }
        
        ProductoOrden actualizado = new ProductoOrden();
        actualizado.setCodProducto(dto.getCodProducto());
        actualizado.setNumeroOrden(dto.getNumeroOrden());
        actualizado.setCantidad(dto.getCantidad());

        dao.get().update(dto);

        actualizaTotalesOrden(dao.get().getNumeroOrden());
        
        return dao.get();
    }
    
    @Transactional(readOnly = true)
    @Override
    public ProductoOrden findById(Long id) {
        LOGGER.debug("Buscando a ProductoOrden con id: " + id);
        return productoOrdenRepository.findById(id).get();
    }
    
    
    @Transactional(readOnly = true)
	@Override
	public List<ProductoOrden> findAll() {
		LOGGER.debug("Buscando a todas los ProductoOrden");
        List<ProductoOrden> listaProductoes
        = productoOrdenRepository.findAll();
        return listaProductoes;
	}


	@Override
	public List<ProductoOrdenDTO> findByNumeroOrden(Long numeroOrden) {
		LOGGER.debug("Buscando a todas los Producto");
		
		List<ProductoOrdenDTO> listaFinal = new ArrayList<>();
        List<ProductoOrden> listaProductoOrden
        = productoOrdenRepository.findByNumeroOrden(numeroOrden);
        
        for(ProductoOrden productoOrden : listaProductoOrden) {
        	Producto productoObj = productoRepository.findByCodProducto(productoOrden.getCodProducto());
        	
        	Long id = productoOrden.getId();
        	String producto = productoObj.getCodProducto() + " - " + productoObj.getNombre();
        	String codProducto = productoObj.getCodProducto();
        	Integer cantidad = productoOrden.getCantidad();
        	Integer existencia = productoObj.getExistencia();
        	BigDecimal precioUnitario = productoObj.getPrecioVenta();
        	BigDecimal subTotal = productoObj.getPrecioVenta().multiply(new BigDecimal(productoOrden.getCantidad()));
        	BigDecimal iva = subTotal.multiply(new BigDecimal(0.16));
        	BigDecimal total = subTotal.add(iva);
        	
        	ProductoOrdenDTO productoOrdenDTO = 
        			new ProductoOrdenDTO(id,  codProducto, producto, cantidad, precioUnitario, 
        					subTotal, iva, total, existencia);
        	
        	listaFinal.add(productoOrdenDTO);
        }
        
    	return listaFinal;
	}
	
	public void actualizaTotalesOrden(Long numeroOrden) {
		Orden orden = ordenRepository.findByNumeroOrden(numeroOrden);
		
        List<ProductoOrden> listaProductoOrden
        = productoOrdenRepository.findByNumeroOrden(numeroOrden);
        
        Double subTotal = 0.0;
        Double iva = 0.0;
        Double total = 0.0;
        for(ProductoOrden productoOrden : listaProductoOrden) {
        	Producto productoObj = productoRepository.findByCodProducto(productoOrden.getCodProducto());
        	
        	subTotal += productoObj.getPrecioVenta().doubleValue() * productoOrden.getCantidad().doubleValue();
        	iva += (productoObj.getPrecioVenta().doubleValue() * productoOrden.getCantidad().doubleValue()) * 0.16;
        	total += (productoObj.getPrecioVenta().doubleValue() * productoOrden.getCantidad().doubleValue()) * 1.16;
        	
        }
        
        orden.setSubTotal(new BigDecimal(subTotal));
        orden.setIva(new BigDecimal(iva));
        orden.setTotal(new BigDecimal(total));
        
        ordenRepository.save(orden);
	}

}

