package com.tpo.unoMas.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tpo.unoMas.model.dao.IClienteDAO;
import com.tpo.unoMas.model.entity.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDAO clientesDAO;

	@Override
	public List<Cliente> findAll() {
		List<Cliente> clientes = clientesDAO.findAll();
		return clientes;
	}

	@Override
	public Cliente findById(int id) {
		Cliente cliente = clientesDAO.findById(id);
		return cliente;
	}

	@Override
	public void save(Cliente cliente) {
		cliente.setCreateAt(new Date());
		clientesDAO.save(cliente);

	}

	@Override
	public void update(int clienteId, Cliente cliente) {
		Cliente clienteExistente = clientesDAO.findById(clienteId);

		if (clienteExistente != null) {
			clienteExistente.setNombre(cliente.getNombre());
			clienteExistente.setApellido(cliente.getApellido());
			clienteExistente.setEmail(cliente.getEmail());
			clienteExistente.setCreateAt(new Date());
			
			clientesDAO.save(clienteExistente);
		}

	}

	@Override
	public void deleteById(int id) {
		clientesDAO.deleteById(id);

	}

}
