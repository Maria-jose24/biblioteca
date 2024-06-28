package com.example.bilbioteca.Controller;

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

import com.example.bilbioteca.interfaceService.ILibroService;
import com.example.bilbioteca.model.Libro;

@RestController
@RequestMapping("/api/v1/libro")
@CrossOrigin
public class LibroController {

	@Autowired
	private ILibroService LibroService;

	@PostMapping("/")
	public ResponseEntity<Object> save(@RequestBody Libro libro) {
		// Verificar si el ISBN ya está registrado
		Optional<Libro> existingLibro = LibroService.findByIsbn(libro.getIsbn());
		if (existingLibro.isPresent()) {
			return new ResponseEntity<>("El ISBN ya se encuentra registrado", HttpStatus.BAD_REQUEST);
		}
	

		// Validar los campos del libro
		if (libro.getTitulo().isEmpty()) {
			return new ResponseEntity<>("El título del libro es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if (libro.getAutor().isEmpty()) {
			return new ResponseEntity<>("El autor del libro es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if (libro.getIsbn().length() != 13) {
			return new ResponseEntity<>("El ISBN debe tener exactamente 13 dígitos numéricos", HttpStatus.BAD_REQUEST);
		}

		if (libro.getGenero().isEmpty()) {
			return new ResponseEntity<>("El género del libro es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if (libro.getNumero_de_ejemplares_disponibles().isEmpty()) {
			return new ResponseEntity<>("El número de ejemplares disponibles es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if (libro.getNumero_de_ejemplares_ocupados().isEmpty()) {
			return new ResponseEntity<>("El número de ejemplares ocupados es obligatorio", HttpStatus.BAD_REQUEST);
		}

		// Guardar el libro si pasa todas las validaciones
		LibroService.save(libro);
		return new ResponseEntity<>(libro, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<Object> findAll() {
		List<Libro> listaLibro = LibroService.findAll();
		return new ResponseEntity<>(listaLibro, HttpStatus.OK);
	}

	@GetMapping("/busquedafiltro/{filtro}")
	public ResponseEntity<Object> findFiltro(@PathVariable String filtro) {
		List<Libro> listaLibro = LibroService.filtroLibro(filtro);
		return new ResponseEntity<>(listaLibro, HttpStatus.OK);
	}

	@GetMapping("/buscarISBN/{isbn}")
	public ResponseEntity<Object> findByIsbn(@PathVariable String isbn) {
		Optional<Libro> libro = LibroService.findByIsbn(isbn);
		if (libro.isPresent()) {
			return new ResponseEntity<>(libro.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Libro no encontrado por ISBN", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> findOne(@PathVariable String id) {
		Optional<Libro> optionalLibro = LibroService.findOne(id);
		if (optionalLibro.isPresent()) {
			return new ResponseEntity<>(optionalLibro.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Libro no encontrado", HttpStatus.NOT_FOUND);
		}
	}

	 @PutMapping("/{id}")
	    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody Libro libroUpdate) {
	        if (libroUpdate.contieneCamposVacios()) {
	            return new ResponseEntity<>("Todos los campos son obligatorios", HttpStatus.BAD_REQUEST);
	        }

	        Optional<Libro> optionalLibro = LibroService.findOne(id);
	        if (optionalLibro.isPresent()) {
	            Libro libro = optionalLibro.get();

	            if (!libro.getIsbn().equals(libroUpdate.getIsbn())) {
	                // Verificar si el nuevo ISBN ya está registrado
	                Optional<Libro> existingLibro = LibroService.findByIsbn(libroUpdate.getIsbn());
	                if (existingLibro.isPresent()) {
	                    return new ResponseEntity<>("Ya existe un libro con el mismo ISBN", HttpStatus.BAD_REQUEST);
	                }
	            }

			libro.setTitulo(libroUpdate.getTitulo());
			libro.setAutor(libroUpdate.getAutor());
			libro.setIsbn(libroUpdate.getIsbn());
			libro.setGenero(libroUpdate.getGenero());
			libro.setNumero_de_ejemplares_disponibles(libroUpdate.getNumero_de_ejemplares_disponibles());
			libro.setNumero_de_ejemplares_ocupados(libroUpdate.getNumero_de_ejemplares_ocupados());

			LibroService.save(libro);
			return new ResponseEntity<>("Libro actualizado correctamente", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Libro no encontrado", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable("id") String id) {
		int result = LibroService.delete(id);
		if (result == 1) {
			return new ResponseEntity<>("Libro eliminado correctamente", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Libro no encontrado", HttpStatus.NOT_FOUND);
		}
	}
}