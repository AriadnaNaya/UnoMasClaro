package com.tpo.unoMas.model.entity;

public class ClienteDTO {
	private String nombre;
	private String apellido;
	private String email;

	public ClienteDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * ClienteDTO Constructor
	 * @param nombre
	 * @param apellido
	 * @param email
	 */
	public ClienteDTO(String nombre, String apellido, String email) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
