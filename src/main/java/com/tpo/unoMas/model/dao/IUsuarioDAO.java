package com.tpo.unoMas.model.dao;

import com.tpo.unoMas.model.entity.Usuario;

public interface IUsuarioDAO {
	public Usuario findUser(String username, String password);
}
