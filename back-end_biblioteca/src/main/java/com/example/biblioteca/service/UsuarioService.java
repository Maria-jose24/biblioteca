package com.example.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.biblioteca.interfaceService.IUsuarioService;
import com.example.biblioteca.interfaces.IUsuario;
import com.example.biblioteca.model.Usuario;

@Service
public class UsuarioService implements IUsuarioService{
		
	@Autowired 
    private IUsuario data;

    @Override
    public String save(Usuario Usuario) {
        data.save(Usuario);
        return Usuario.getId_usuario();
    }

    @Override
    public List<Usuario> findAll() {
        return (List<Usuario>) data.findAll();
    }

    @Override
    public List<Usuario> filtroUsuario(String filtro) {
        return data.filtroUsuario(filtro);
    }

    @Override
    public List<Usuario> filtroIngresoUsuario(String correo) {
        return data.filtroIngresoUsuario(correo);
    }

    @Override
    public Optional<Usuario> findOne(String id_usuario) {
        return data.findById(id_usuario);
    }

    @Override
    public void delete(String id_usuario) {
        data.deleteById(id_usuario);
    }
}