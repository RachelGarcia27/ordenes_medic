package com.racheldev.orders.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.racheldev.orders.entity.Producto;
import com.racheldev.orders.repository.ProductoRepository;
import com.racheldev.orders.type.exception.ModelException;

@Service
public class ProductoServiceImpl implements ProductoService {

    /**
     * Variable que apunta a la bitácora de la clase.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ProductoServiceImpl.class);

    @Resource
    private ProductoRepository productoRepository;

    @Transactional
    @Override
    public Producto create(Producto dto) {
        LOGGER.debug("Creando un Producto con información: " + dto);
        /*
        Producto dao = new Producto();
        dao.setCodProducto(dto.getCodProducto());
        dao.setNombre(dto.getNombre());
        dao.setExistencia(dto.getExistencia());
        dao.setPrecioCompra(dto.getPrecioCompra());
        dao.setPrecioVenta(dto.getPrecioVenta());
        dao.setCveProveedor(dto.getCveProveedor());
		*/
        return productoRepository.save(dto);
    }

    @Transactional(rollbackFor = ModelException.class)
    @Override
    public Producto delete(Long id) throws ModelException {
        LOGGER.debug("Borrando el Producto con id: " + id);

        Optional<Producto> dao = productoRepository.findById(id);
        if (dao.get() == null) {
            LOGGER.debug("No se encuentra a el Producto con id: " + id);
            throw new ModelException(ModelException.ERROR_RECORD_NOT_FOUND);
        }

        productoRepository.delete(dao.get());

        return dao.get();
    }

    @Transactional(rollbackFor = ModelException.class)
    @Override
    public Producto update(Producto dto) throws ModelException {
        LOGGER.debug("Acualizando Producto con información: " + dto);

        Optional<Producto> dao = productoRepository.findById(dto.getId());

        if (!dao.isPresent()) {
            LOGGER.debug("No se encontró Orden: " + dto.getId());
            throw new ModelException(ModelException.ERROR_RECORD_NOT_FOUND);
        }
        
        Producto actualizado = new Producto();
        actualizado.setCodProducto(dto.getCodProducto());
        actualizado.setNombre(dto.getNombre());
        actualizado.setExistencia(dto.getExistencia());
        actualizado.setPrecioCompra(dto.getPrecioCompra());
        actualizado.setPrecioVenta(dto.getPrecioVenta());
        actualizado.setCveProveedor(dto.getCveProveedor());

        dao.get().update(dto);

        return dao.get();
    }
    
    @Transactional(readOnly = true)
    @Override
    public Producto findById(Long id) {
        LOGGER.debug("Buscando a Producto con id: " + id);
        return productoRepository.findById(id).get();
    }
    
    
    @Transactional(readOnly = true)
	@Override
	public List<Producto> findAll() {
		LOGGER.debug("Buscando a todas los Producto");
        List<Producto> listaProductoes
        = productoRepository.findAll();
        return listaProductoes;
	}


}
