package com.example.biblioteca.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.biblioteca.interfaceService.ILibroService;
import com.example.biblioteca.model.Libro;

@RestController
@RequestMapping("/api/v1/libro")
@CrossOrigin
public class LibroController {

	@Autowired

	private ILibroService LibroService;


	@PostMapping("/")

	public ResponseEntity<Object> save (@RequestBody Libro Libro){

		List<Libro> libros = LibroService.filtroIngresoLibro(Libro.getTitulo());
		if (!libros.isEmpty()) {
			return new ResponseEntity<>("El título del libro ya se encuentra registrado", HttpStatus.BAD_REQUEST);
		}

		if (Libro.getTitulo().isEmpty()) {
			return new ResponseEntity<>("El título del libro es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if (Libro.getAutor().isEmpty()) {
			return new ResponseEntity<>("El autor del libro es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if (Libro.getIsbn().isEmpty() || !Libro.getIsbn().matches("\\d{13}")) {
			return new ResponseEntity<>("El ISBN debe tener exactamente 13 dígitos numéricos", HttpStatus.BAD_REQUEST);
		}
		if (Libro.getNumero_de_ejemplares_disponibles().isEmpty()) {
			return new ResponseEntity<>("El número de ejemplares disponibles es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if (Libro.getNumero_de_ejemplares_ocupados().isEmpty()) {
			return new ResponseEntity<>("El número de ejemplares ocupados es obligatorio", HttpStatus.BAD_REQUEST);
		}


		// Guardar el libro si pasa todas las validaciones
		LibroService.save(Libro);
		return new ResponseEntity<>(Libro, HttpStatus.OK);
	}



	@GetMapping("/")
	public ResponseEntity<Object>findAll(){
		var ListaLibro = LibroService.findAll();
		return new ResponseEntity<>(ListaLibro, HttpStatus.OK);
	}

	//filtro
	@GetMapping("/busquedafiltro/{filtro}")
	public ResponseEntity<Object>findFiltro(@PathVariable String filtro){
		var ListaLibro = LibroService.filtroLibro(filtro);
		return new ResponseEntity<>(ListaLibro, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> findOne ( @PathVariable String id ){
		var Libro= LibroService.findOne(id);
		return new ResponseEntity<>(Libro, HttpStatus.OK);
	}


	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@PathVariable String id, @ModelAttribute("Libro") Libro LibroUpdate) {

		if (LibroUpdate.contieneCamposVacios()) {
			return new ResponseEntity<>("Todos los campos son obligatorios", HttpStatus.BAD_REQUEST);
		}

		var libro = LibroService.findOne(id).get();
		if (libro != null) {
			if (!libro.getTitulo().equals(LibroUpdate.getTitulo())) {
				List<Libro> librosConMismoTitulo = LibroService.filtroIngresoLibro(LibroUpdate.getTitulo());
				if (!librosConMismoTitulo.isEmpty()) {
					return new ResponseEntity<>("El título del libro ya se encuentra registrado", HttpStatus.BAD_REQUEST);
				}
			}

			libro.setTitulo(LibroUpdate.getTitulo());
			libro.setAutor(LibroUpdate.getAutor());
			libro.setIsbn(LibroUpdate.getIsbn());
			libro.setGenero(LibroUpdate.getGenero());
			libro.setNumero_de_ejemplares_disponibles(LibroUpdate.getNumero_de_ejemplares_disponibles());
			libro.setNumero_de_ejemplares_ocupados(LibroUpdate.getNumero_de_ejemplares_ocupados());

			LibroService.save(libro);
			return new ResponseEntity<>("Guardado", HttpStatus.OK);

		} else {
			return new ResponseEntity<>("Error libro no encontrado", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable("id") String id) {
		LibroService.delete(id);
		return new ResponseEntity<>("Libro eliminado", HttpStatus.OK);
	}
}