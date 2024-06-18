var url = "http://localhost:8081/api/v1/libro/";

// Función para listar libros con o sin filtro
function listarLibro() {
  var capturarFiltro = document.getElementById("inputSearch").value;
  var urlLocal = url;
  if (capturarFiltro !== "") {
    urlLocal += "/busquedafiltro/" + capturarFiltro;
  }

  $.ajax({
    url: urlLocal,
    type: "GET",
    success: function(result) {
      console.log(result);

      var cuerpoTabla = document.getElementById("cuerpoTabla");
      cuerpoTabla.innerHTML = "";

      for (var i = 0; i < result.length; i++) {
        var trResgistro = document.createElement("tr");

        var celdaId = document.createElement("td");
        celdaId.innerText = result[i]["id"];
        trResgistro.appendChild(celdaId);

        var celdaTitulo = document.createElement("td");
        celdaTitulo.innerText = result[i]["titulo"];
        trResgistro.appendChild(celdaTitulo);

        var celdaAutor = document.createElement("td");
        celdaAutor.innerText = result[i]["autor"];
        trResgistro.appendChild(celdaAutor);

        var celdaIsbn = document.createElement("td");
        celdaIsbn.innerText = result[i]["isbn"];
        trResgistro.appendChild(celdaIsbn);

        var celdaGenero = document.createElement("td");
        celdaGenero.innerText = result[i]["genero"];
        trResgistro.appendChild(celdaGenero);

        var celdaEjemplaresDisponibles = document.createElement("td");
        celdaEjemplaresDisponibles.innerText = result[i]["numero_de_ejemplares_disponibles"];
        trResgistro.appendChild(celdaEjemplaresDisponibles);

        var celdaEjemplaresOcupados = document.createElement("td");
        celdaEjemplaresOcupados.innerText = result[i]["numero_de_ejemplares_ocupados"];
        trResgistro.appendChild(celdaEjemplaresOcupados);

        var celdaOpcionEditar = document.createElement("td");
        var botonEditarLibro = document.createElement("button");
        botonEditarLibro.value = result[i]["id"];
        botonEditarLibro.innerHTML = "Editar";
        botonEditarLibro.onclick = function(e) {
          $('#exampleModal').modal('show');
          consultarLibroID(this.value);
        };
        botonEditarLibro.className = "btn btn-warning editar-libro";
        celdaOpcionEditar.appendChild(botonEditarLibro);
        trResgistro.appendChild(celdaOpcionEditar);

        var celdaOpcionEliminar = document.createElement("td");
        var botonEliminarLibro = document.createElement("button");
        botonEliminarLibro.value = result[i]["id"];
        botonEliminarLibro.innerHTML = "Eliminar";
        botonEliminarLibro.onclick = function(e) {
          eliminarLibro(this.value);
        };
        botonEliminarLibro.className = "btn btn-danger eliminar-libro";
        celdaOpcionEliminar.appendChild(botonEliminarLibro);
        trResgistro.appendChild(celdaOpcionEliminar);

        cuerpoTabla.appendChild(trResgistro);
      }
    },
    error: function(error) {
      alert("Error en la petición " + error.responseText);
    }
  });
}

// Función para eliminar un libro
function eliminarLibro(id) {
  Swal.fire({
    title: "¿Estás seguro?",
    text: "¿Deseas eliminar este libro?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: "Eliminar",
    cancelButtonText: "Cancelar"
  }).then((result) => {
    if (result.isConfirmed) {
      $.ajax({
        url: url + "/" + id,
        type: "DELETE",
        success: function(response) {
          Swal.fire({
            title: "¡Eliminado!",
            text: "El libro ha sido eliminado correctamente.",
            icon: "success"
          });
          listarLibro();
        },
        error: function(error) {
          Swal.fire("Error", "Error al eliminar el libro. " + error.responseText, "error");
        }
      });
    }
  });
}

// Función para consultar un libro por su ID
function consultarLibroID(id) {
  $.ajax({
    url: url + "/" + id,
    type: "GET",
    success: function(result) {
      document.getElementById("id").value = result["id"];
      document.getElementById("titulo").value = result["titulo"];
      document.getElementById("autor").value = result["autor"];
      document.getElementById("isbn").value = result["isbn"];
      document.getElementById("genero").value = result["genero"];
      document.getElementById("numero_de_ejemplares_disponibles").value = result["numero_de_ejemplares_disponibles"];
      document.getElementById("numero_de_ejemplares_ocupados").value = result["numero_de_ejemplares_ocupados"];
    },
    error: function(error) {
      alert("Error al consultar el libro: " + error.responseText);
    }
  });
}

// Función para actualizar un libro
function actualizarLibro() {
  var id = document.getElementById("id").value;
  let formData = {
    "titulo": document.getElementById("titulo").value,
    "autor": document.getElementById("autor").value,
    "isbn": document.getElementById("isbn").value,
    "genero": document.getElementById("genero").value,
    "numero_de_ejemplares_disponibles": document.getElementById("numero_de_ejemplares_disponibles").value,
    "numero_de_ejemplares_ocupados": document.getElementById("numero_de_ejemplares_ocupados").value
  };

  if (validarCampos()) {
    $.ajax({
      url: url + "/" + id,
      type: "PUT",
      contentType: "application/json",
      data: JSON.stringify(formData),
      success: function(result) {
        Swal.fire({
          title: "¡Excelente!",
          text: "Se guardó correctamente",
          icon: "success"
        });
        listarLibro();
      },
      error: function(error) {
        Swal.fire("Error", "Error al guardar, " + error.responseText, "error");
      }
    });
  } else {
    Swal.fire({
      title: "¡Error!",
      text: "Llene todos los campos correctamente",
      icon: "error"
    });
  }
}

// Función para registrar un libro
function registrarLibro() {
  let formData = {
    "titulo": document.getElementById("titulo").value,
    "autor": document.getElementById("autor").value,
    "isbn": document.getElementById("isbn").value,
    "genero": document.getElementById("genero").value,
    "numero_de_ejemplares_disponibles": document.getElementById("numero_de_ejemplares_disponibles").value,
    "numero_de_ejemplares_ocupados": document.getElementById("numero_de_ejemplares_ocupados").value
  };

  if (validarCampos()) {
    $.ajax({
      url: url,
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(formData),
      success: function(result) {
        Swal.fire({
          title: "¡Excelente!",
          text: "Se guardó correctamente su registro",
          icon: "success"
        });
        limpiarLibro();
        listarLibro();
      },
      error: function(error) {
        Swal.fire("Error", "Error al guardar, " + error.responseText, "error");
      }
    });
  } else {
    Swal.fire({
      title: "¡Error!",
      text: "Llene todos los campos correctamente",
      icon: "error"
    });
  }
}
function validarCampos() {
  var titulo = document.getElementById("titulo").value;
  var autor = document.getElementById("autor").value;
  var isbn = document.getElementById("isbn").value;
  var genero = document.getElementById("genero").value;
  var numero_de_ejemplares_disponibles = document.getElementById("numero_de_ejemplares_disponibles").value;
  var numero_de_ejemplares_ocupados = document.getElementById("numero_de_ejemplares_ocupados").value;

  if (titulo === '' || autor === '' || isbn === '' || genero === '' || numero_de_ejemplares_disponibles === '' || numero_de_ejemplares_ocupados === '') {
    return false;
  } else {
    return true;
  }
}

function validarTitulo(input) {
  if (input.value.trim() === "") {
    alert("El título es obligatorio.");
    input.focus();
  }
}

function validarAutor(input) {
  if (input.value.trim() === "") {
    alert("El autor es obligatorio.");
    input.focus();
  }
}

function validarIsbn(input) {
  if (!/^\d{13}$/.test(input.value)) {
    alert("El ISBN debe tener exactamente 13 dígitos.");
    input.focus();
  }
}

function validarGenero(input) {
  if (input.value.trim() === "") {
    alert("El género es obligatorio.");
    input.focus();
  }
}

function validarNumero_de_ejemplares_disponibles(input) {
  if (input.value.trim() === "") {
    alert("El número de ejemplares disponibles es obligatorio.");
    input.focus();
  }
}

function validarNumero_de_ejemplares_ocupados(input) {
  if (input.value.trim() === "") {
    alert("El número de ejemplares ocupados es obligatorio.");
    input.focus();
  }
}

function limpiarLibro() {
  document.getElementById("titulo").className = "form-control";
  document.getElementById("autor").className = "form-control";
  document.getElementById("isbn").className = "form-control";
  document.getElementById("genero").className = "form-control";
  document.getElementById("numero_de_ejemplares_disponibles").className = "form-control";
  document.getElementById("numero_de_ejemplares_ocupados").className = "form-control";

  document.getElementById("titulo").value = "";
  document.getElementById("autor").value = "";
  document.getElementById("isbn").value = "";
  document.getElementById("genero").value = "";
  document.getElementById("numero_de_ejemplares_disponibles").value = "";
  document.getElementById("numero_de_ejemplares_ocupados").value = "";
}
