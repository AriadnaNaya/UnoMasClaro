package com.tpo.unoMas.service;

import com.tpo.unoMas.model.entity.Usuario;

public interface IUsuarioService {
	public Usuario findUser(String username, String password);
}
