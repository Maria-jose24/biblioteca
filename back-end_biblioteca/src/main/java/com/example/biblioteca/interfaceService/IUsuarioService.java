package com.example.biblioteca.interfaceService;

import java.util.List;
import java.util.Optional;

import com.example.biblioteca.model.Usuario;

public interface IUsuarioService {

	String save(Usuario Usuario);
    List<Usuario> findAll();
    List<Usuario> filtroUsuario(String filtro);
    List<Usuario> filtroIngresoUsuario(String correo);
    Optional<Usuario> findOne(String id_usuario);
    void delete(String id_usuario);
}
