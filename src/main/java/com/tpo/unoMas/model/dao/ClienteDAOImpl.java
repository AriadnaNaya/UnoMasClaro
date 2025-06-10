package com.tpo.unoMas.model.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tpo.unoMas.model.entity.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ClienteDAOImpl implements IClienteDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		Session currentSession = entityManager.unwrap(Session.class);

		Query<Cliente> getQuery = currentSession.createQuery("from Cliente", Cliente.class);
		List<Cliente> clientes = getQuery.getResultList();

		return clientes;
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(int id) {
		Session currentSession = entityManager.unwrap(Session.class);

		Cliente cliente = currentSession.get(Cliente.class, id);

		return cliente;
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		Session currentSession = entityManager.unwrap(Session.class);
		currentSession.persist(cliente);
	}

	@Override
	@Transactional
	public void deleteById(int id) {
		Session currentSession = entityManager.unwrap(Session.class);

		Query theQuery = currentSession.createQuery("delete from Cliente where id=:idCliente");
		theQuery.setParameter("idCliente", id);
		theQuery.executeUpdate();
	}

}
