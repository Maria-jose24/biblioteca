package com.example.biblioteca.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.biblioteca.model.Usuario;

@Repository
public interface IUsuario extends CrudRepository <Usuario,String> {

	@Query("SELECT u FROM Usuario u WHERE u.nombre_completo LIKE %?1% OR u.correo LIKE %?1%")
	List<Usuario> filtroUsuario(String filtro);

	@Query("SELECT u FROM Usuario u WHERE u.correo = ?1")
	List<Usuario> filtroIngresoUsuario(String correo);
}

