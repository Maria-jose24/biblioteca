package com.example.biblioteca.Controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.biblioteca.interfaceService.IUsuarioService;
import com.example.biblioteca.model.Usuario;

@RestController
@RequestMapping("/api/v1/usuario")
@CrossOrigin
public class UsuarioController {

	 @Autowired
	    private IUsuarioService usuarioService;

	    @PostMapping("/registrarUsuario")
	    public ResponseEntity<Object> registrarUsuario(@ModelAttribute Usuario usuario) {
	        if (usuario.getNombre_completo().isEmpty()) {
	            return new ResponseEntity<>("El nombre es obligatorio", HttpStatus.BAD_REQUEST);
	        }
	        if (usuario.getDireccion().isEmpty()) {
	            return new ResponseEntity<>("La dirección es obligatoria", HttpStatus.BAD_REQUEST);
	        }
	        if (usuario.getCorreo().isEmpty()) {
	            return new ResponseEntity<>("El correo electrónico es obligatorio", HttpStatus.BAD_REQUEST);
	        }
	        if (usuario.getTipo_usuario().isEmpty()) {
	            return new ResponseEntity<>("El tipo de usuario es obligatorio", HttpStatus.BAD_REQUEST);
	        }

	        List<Usuario> usuarios = usuarioService.filtroIngresoUsuario(usuario.getCorreo());
	        if (!usuarios.isEmpty()) {
	            return new ResponseEntity<>("El usuario ya está registrado", HttpStatus.BAD_REQUEST);
	        }

	        usuarioService.save(usuario);
	        return new ResponseEntity<>(usuario, HttpStatus.OK);
	    }

	    @GetMapping("/ListaUsuario")
	    public ResponseEntity<Object> listaUsuarios() {
	        List<Usuario> listaUsuarios = usuarioService.findAll();
	        return new ResponseEntity<>(listaUsuarios, HttpStatus.OK);
	    }

	    @GetMapping("/ListaUsuario/buscar/{filtro}")
	    public ResponseEntity<Object> buscarUsuario(@PathVariable String filtro) {
	        List<Usuario> listaUsuarios = usuarioService.filtroUsuario(filtro);
	        return new ResponseEntity<>(listaUsuarios, HttpStatus.OK);
	    }

	    @GetMapping("/registrarUsuario/{id}")
	    public ResponseEntity<Object> obtenerUsuario(@PathVariable String id) {
	        Optional<Usuario> usuario = usuarioService.findOne(id);
	        if (usuario.isPresent()) {
	            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
	        }
	    }

	    @PutMapping("/registrarUsuario/{id}")
	    public ResponseEntity<Object> actualizarUsuario(@PathVariable String id, @ModelAttribute Usuario usuarioUpdate) {
	        if (usuarioUpdate.getNombre_completo().isEmpty() || usuarioUpdate.getDireccion().isEmpty() || usuarioUpdate.getCorreo().isEmpty() || usuarioUpdate.getTipo_usuario().isEmpty()) {
	            return new ResponseEntity<>("Todos los campos son obligatorios", HttpStatus.BAD_REQUEST);
	        }

	        Optional<Usuario> usuarioOpt = usuarioService.findOne(id);
	        if (usuarioOpt.isPresent()) {
	            Usuario usuario = usuarioOpt.get();
	            if (!usuario.getCorreo().equals(usuarioUpdate.getCorreo())) {
	                List<Usuario> usuariosConMismoCorreo = usuarioService.filtroIngresoUsuario(usuarioUpdate.getCorreo());
	                if (!usuariosConMismoCorreo.isEmpty()) {
	                    return new ResponseEntity<>("El correo electrónico ya está en uso", HttpStatus.BAD_REQUEST);
	                }
	            }

	            usuario.setNombre_completo(usuarioUpdate.getNombre_completo());
	            usuario.setDireccion(usuarioUpdate.getDireccion());
	            usuario.setCorreo(usuarioUpdate.getCorreo());
	            usuario.setTipo_usuario(usuarioUpdate.getTipo_usuario());

	            usuarioService.save(usuario);
	            return new ResponseEntity<>("Usuario actualizado correctamente", HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
	        }
	    }

	    @DeleteMapping("/registrarUsuario/{id}")
	    public ResponseEntity<Object> eliminarUsuario(@PathVariable String id) {
	        usuarioService.delete(id);
	        return new ResponseEntity<>("Usuario eliminado", HttpStatus.OK);
	    }
	}