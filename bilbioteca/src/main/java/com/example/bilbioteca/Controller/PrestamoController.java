package com.example.bilbioteca.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bilbioteca.interfaceService.IPrestamoService;
import com.example.bilbioteca.model.Prestamo;

@RequestMapping("/api/v1/prestamo")
@RestController
@CrossOrigin
public class PrestamoController {

	@Autowired
	private IPrestamoService PrestamoService;

	@PostMapping("/")
	public ResponseEntity<Object> save(@RequestBody Prestamo prestamo) {
		try {
			LocalDate fechaIngreso = LocalDate.parse(prestamo.getFecha_prestamo());
			LocalDate fechaSalida = LocalDate.parse(prestamo.getFecha_devolucion());

			if (fechaSalida.compareTo(fechaIngreso) < 0) {
				return ResponseEntity.badRequest().body("La fecha de salida no puede ser anterior a la fecha de ingreso");
			}

			if (prestamo.getUsuario() == null) {
				return ResponseEntity.badRequest().body("El campo usuario es obligatorio");
			}

			if (prestamo.getLibro() == null) {
				return ResponseEntity.badRequest().body("El campo libro es obligatorio");
			}

			if (prestamo.getFecha_prestamo() == null || prestamo.getFecha_prestamo().isEmpty()) {
				return ResponseEntity.badRequest().body("El campo fecha ingreso es obligatorio");
			}

			if (prestamo.getFecha_devolucion() == null || prestamo.getFecha_devolucion().isEmpty()) {
				return ResponseEntity.badRequest().body("El campo fecha salida es obligatorio");
			}

			if (prestamo.getEstado_prestamo() == null || prestamo.getEstado_prestamo().isEmpty()) {
				return ResponseEntity.badRequest().body("El campo estado es obligatorio");
			}

			PrestamoService.save(prestamo);

			return ResponseEntity.ok(prestamo);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
		}
	}

	@GetMapping("/")
	public ResponseEntity<Object> findAll() {
		List<Prestamo> listaPrestamo = PrestamoService.findAll();
		return ResponseEntity.ok(listaPrestamo);
	}

	@GetMapping("/{id_prestamo}")
	public ResponseEntity<Object> findOne(@PathVariable String id_prestamo) {
		Optional<Prestamo> prestamo = PrestamoService.findOne(id_prestamo);
		return prestamo.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().body("Prestamo no encontrado"));
	}

	@PutMapping("/{id_prestamo}")
	public ResponseEntity<Object> update(@PathVariable String id_prestamo, @RequestBody Prestamo prestamoUpdate) {
		try {
			Optional<Prestamo> prestamoOptional = PrestamoService.findOne(id_prestamo);
			if (prestamoOptional.isPresent()) {
				Prestamo prestamo = prestamoOptional.get();
				LocalDate fechaPrestamo = LocalDate.parse(prestamoUpdate.getFecha_prestamo());
				LocalDate fechaSalida = LocalDate.parse(prestamoUpdate.getFecha_devolucion());

				if (fechaSalida.compareTo(fechaPrestamo) < 0) {
					return ResponseEntity.badRequest().body("La fecha de salida no puede ser anterior a la fecha de ingreso");
				}

				prestamo.setUsuario(prestamoUpdate.getUsuario());
				prestamo.setLibro(prestamoUpdate.getLibro());
				prestamo.setFecha_prestamo(prestamoUpdate.getFecha_prestamo());
				prestamo.setFecha_devolucion(prestamoUpdate.getFecha_devolucion());
				prestamo.setEstado_prestamo(prestamoUpdate.getEstado_prestamo());

				PrestamoService.save(prestamo);

				return ResponseEntity.ok("Prestamo actualizado");
			} else {
				return ResponseEntity.badRequest().body("Error: prestamo no encontrado");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
		}
	}

	@DeleteMapping("/{id_prestamo}")
	public ResponseEntity<Object> delete(@PathVariable("id_prestamo") String id_prestamo) {
		try {
			PrestamoService.delete(id_prestamo);
			return ResponseEntity.ok("Prestamo eliminado");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
		}
	}
}