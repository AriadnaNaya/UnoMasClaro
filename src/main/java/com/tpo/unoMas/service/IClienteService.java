package com.tpo.unoMas.service;

import java.util.List;

import com.tpo.unoMas.model.entity.Cliente;

public interface IClienteService {
	public List<Cliente> findAll();

	public Cliente findById(int id);

	public void save(Cliente cliente);
	
	public void update(int clienteId, Cliente cliente);

	public void deleteById(int id);
}
