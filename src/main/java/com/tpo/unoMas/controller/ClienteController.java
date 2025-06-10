package com.tpo.unoMas.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tpo.unoMas.model.entity.Cliente;
import com.tpo.unoMas.model.entity.ClienteDTO;
import com.tpo.unoMas.service.IClienteService;

@RestController
@RequestMapping("/api")
public class ClienteController {
	@Autowired
	private IClienteService clienteService;

	// @RequestMapping(value = "/clientes", method = RequestMethod.GET)
	@GetMapping("/clientes")
	public List<ClienteDTO> findAll() {
		List<Cliente> clientes = clienteService.findAll();
        List<ClienteDTO> clienteDTOs = new ArrayList<>();

        for (Cliente cliente : clientes) {
            ClienteDTO clienteDTO = convertToDTO(cliente);
            clienteDTOs.add(clienteDTO);
        }

        return clienteDTOs;
	}

	@GetMapping("/clientes/{clienteId}")
	public ResponseEntity<?> getCliente(@PathVariable int clienteId) {
		Cliente cliente = clienteService.findById(clienteId);		

		if (cliente == null) {
			String mensaje = "Cliente no encontrado con ID: " + clienteId;
			return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
		}

		ClienteDTO clienteDTO = convertToDTO(cliente);
		return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
	}

	@GetMapping("/clientesParam")
	public ResponseEntity<?> getClienteParam(@RequestParam("clienteId") int clienteId) {
		Cliente cliente = clienteService.findById(clienteId);

		if (cliente == null) {
			String mensaje = "Cliente no encontrado con ID: " + clienteId;
			return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
		}

		ClienteDTO clienteDTO = convertToDTO(cliente);
		return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
	}

	@PostMapping("/clientes")
	public ResponseEntity<ClienteDTO> addCliente(@RequestBody ClienteDTO clienteDTO) {
		Cliente cliente = convertToEntity(clienteDTO);
		
		clienteService.save(cliente);
		
		ClienteDTO nuevoClienteDTO = convertToDTO(cliente);

		return new ResponseEntity<>(nuevoClienteDTO, HttpStatus.CREATED);
	}

	@PutMapping("/clientes/{clienteId}")
	public ResponseEntity<?> updateCliente(@PathVariable int clienteId, @RequestBody ClienteDTO clienteDTO) {
		Cliente clienteOld = clienteService.findById(clienteId);

		if (clienteOld == null) {
			String mensaje = "Cliente no encontrado con ID: " + clienteId;
			return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
		}

		Cliente clienteToUpdate = convertToEntity(clienteDTO);
		clienteService.update(clienteId, clienteToUpdate);
		
        ClienteDTO clienteUpdatedDTO = convertToDTO(clienteToUpdate);
		return new ResponseEntity<>(clienteUpdatedDTO, HttpStatus.OK);
	}

	@DeleteMapping("clientes/{clienteId}")
	public ResponseEntity<String> deleteCliente(@PathVariable int clienteId) {
		Cliente cliente = clienteService.findById(clienteId);

		if (cliente == null) {
			String mensaje = "Cliente no encontrado con ID: " + clienteId;
			return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
		}

		clienteService.deleteById(clienteId);

		String mensaje = "Cliente eliminado [clienteID: " + clienteId + "]";
		return new ResponseEntity<>(mensaje, HttpStatus.OK);
	}

	/**
	 * Método auxiliar para convertir a ClienteDTO
	 * @param cliente
	 * @return
	 */
	private ClienteDTO convertToDTO(Cliente cliente) {
		ClienteDTO clienteDTO = new ClienteDTO(cliente.getNombre(), cliente.getApellido(), cliente.getEmail());
		return clienteDTO;
	}

	/**
	 * Método auxiliar para convertir a Cliente
	 * @param clienteDTO
	 * @return
	 */
	private Cliente convertToEntity(ClienteDTO clienteDTO) {
		Cliente cliente = new Cliente();
		cliente.setNombre(clienteDTO.getNombre());
		cliente.setApellido(clienteDTO.getApellido());
		cliente.setEmail(clienteDTO.getEmail());
		return cliente;
	}
}
