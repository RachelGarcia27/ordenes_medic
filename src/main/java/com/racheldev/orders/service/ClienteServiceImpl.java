package com.racheldev.orders.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.racheldev.orders.entity.Cliente;
import com.racheldev.orders.repository.ClienteRepository;
import com.racheldev.orders.type.exception.ModelException;

@Service
public class ClienteServiceImpl implements ClienteService {

    /**
     * Variable que apunta a la bitácora de la clase.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ClienteServiceImpl.class);

    @Resource
    private ClienteRepository clienteRepository;

    @Transactional
    @Override
    public Cliente create(Cliente dto) {
        LOGGER.debug("Creando un Cliente con información: " + dto);

        return clienteRepository.save(dto);
    }

    @Transactional(rollbackFor = ModelException.class)
    @Override
    public Cliente delete(Long id) throws ModelException {
        LOGGER.debug("Borrando el Cliente con id: " + id);

        Optional<Cliente> dao = clienteRepository.findById(id);
        if (dao.get() == null) {
            LOGGER.debug("No se encuentra a el Cliente con id: " + id);
            throw new ModelException(ModelException.ERROR_RECORD_NOT_FOUND);
        }

        clienteRepository.delete(dao.get());

        return dao.get();
    }

    @Transactional(rollbackFor = ModelException.class)
    @Override
    public Cliente update(Cliente dto) throws ModelException {
        LOGGER.debug("Acualizando Cliente con información: " + dto);

        Optional<Cliente> dao = clienteRepository.findById(dto.getId());

        if (!dao.isPresent()) {
            LOGGER.debug("No se encontró Orden: " + dto.getId());
            throw new ModelException(ModelException.ERROR_RECORD_NOT_FOUND);
        }
        
        Cliente actualizado = new Cliente();
        actualizado.setCveCliente(dto.getCveCliente());
        actualizado.setApellidoPaterno(dto.getApellidoPaterno());
        actualizado.setApellidoMaterno(dto.getApellidoMaterno());
        actualizado.setNombre(dto.getNombre());
        actualizado.setTelefono(dto.getTelefono());
        actualizado.setDireccion(dto.getDireccion());

        dao.get().update(dto);

        return dao.get();
    }
    
    @Transactional(readOnly = true)
    @Override
    public Cliente findById(Long id) {
        LOGGER.debug("Buscando a Cliente con id: " + id);
        return clienteRepository.findById(id).get();
    }
    
    
    @Transactional(readOnly = true)
	@Override
	public List<Cliente> findAll() {
		LOGGER.debug("Buscando a todas los Cliente");
        List<Cliente> listaClientes
        = clienteRepository.findAll();
        return listaClientes;
	}


}