package com.tpo.unoMas.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tpo.unoMas.model.entity.Cliente;

public interface IClienteRepository extends JpaRepository<Cliente, Long> {

}
