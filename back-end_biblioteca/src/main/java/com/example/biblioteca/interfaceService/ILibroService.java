package com.example.biblioteca.interfaceService;

import java.util.List;
import java.util.Optional;

import com.example.biblioteca.model.Libro;

public interface ILibroService {

	String save(Libro libro);
	List<Libro> findAll();
	List<Libro> filtroLibro(String filtro);
	Optional<Libro> findOne(String id);
	int delete(String id);
	List<Libro> filtroIngresoLibro(String isbn); 
}