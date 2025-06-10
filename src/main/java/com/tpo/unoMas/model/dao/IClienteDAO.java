package com.tpo.unoMas.model.dao;

import java.util.List;

import com.tpo.unoMas.model.entity.Cliente;

public interface IClienteDAO {
	public List<Cliente> findAll();

	public Cliente findById(int id);

	public void save(Cliente cliente);

	public void deleteById(int id);
}
