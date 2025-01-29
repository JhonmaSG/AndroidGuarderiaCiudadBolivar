<?php
include './conexion.php'; 
$link = conectar();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Recibir los datos del formulario
    $nombreUsuario = $_POST['nombreUsuario'];
    $contrasena = $_POST['contrasena'];
    $dni = $_POST['dni'];
    $rol = $_POST['rol'];
    $nombres = $_POST['nombre'];
    $apellidos = $_POST['apellido'];
    $pregunta = $_POST['pregunta'];
    $respuesta = $_POST['respuesta'];

    // Encriptar la contraseña (puedes agregar una contraseña por defecto por ahora)
    //$contrasena = password_hash('contraseña_default', PASSWORD_DEFAULT);

    if ($rol == 'Profesor') {
        $rolId = 1; // ID correspondiente al rol Profesor
    } else if ($rol == 'Administrativo') {
        $rolId = 2; // ID correspondiente al rol Administrativo
    } else {
        $rolId = null; // O algún valor predeterminado si el rol no se seleccionó correctamente
    }

    error_log("Datos recibidos: nombreUsuario=$nombreUsuario, dni=$dni, rol=$rol, nombres=$nombres, apellidos=$apellidos");


    // Insertar los datos en la base de datos
    $query = "INSERT INTO usuarios (nombreUsuario, contrasena, rolId, dni, nombres, apellidos, pregunta, respuesta, ultimoCambio) 
              VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURDATE())";
    
    // Preparar la consulta
    if ($stmt = mysqli_prepare($link, $query)) {
        // Bind los parámetros
        mysqli_stmt_bind_param($stmt, "ssiissss", $nombreUsuario, $contrasena, $rolId, $dni, $nombres, $apellidos, $pregunta, $respuesta);
        
        // Ejecutar la consulta
        if (mysqli_stmt_execute($stmt)) {
            echo "Registro exitoso";
        } else {
            echo "Error en el registro: " . mysqli_error($link);    
        }

        // Cerrar la declaración
        mysqli_stmt_close($stmt);
    }

    // Cerrar la conexión
    mysqli_close($link);
}
?>
