package com.tpo.unoMas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tpo.unoMas.model.dao.IUsuarioDAO;
import com.tpo.unoMas.model.entity.Usuario;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private IUsuarioDAO usuariosDAO;

	@Override
	public Usuario findUser(String username, String password) {
		Usuario usuario = usuariosDAO.findUser(username, password);
		return usuario;
	}

}
