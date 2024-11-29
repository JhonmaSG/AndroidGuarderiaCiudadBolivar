<?php
include './conexion.php';  
$link = conectar();

// Recibir los datos
$user = $_REQUEST['nombreUsuario'];
$pass = $_REQUEST['contrasena'];
$rolId = $_REQUEST['rolId'];

if (empty($user) || empty($pass) || empty($rolId)) {
    echo json_encode([['error' => 'Datos incompletos']]);  // Enviar mensaje de error como un array
    exit();
}

// Consulta SQL segura usando mysqli con `bind_param`
$sql = "SELECT * FROM usuarios WHERE nombreUsuario = ? AND contrasena = ? AND rolId = ?";
$stmt = mysqli_prepare($link, $sql);

if ($stmt) {
    // Bind de parámetros a la consulta
    mysqli_stmt_bind_param($stmt, "ssi", $user, $pass, $rolId);
    mysqli_stmt_execute($stmt);
    $res = mysqli_stmt_get_result($stmt);

    if (mysqli_num_rows($res) > 0) {
        $row = mysqli_fetch_assoc($res);

        // Verificar si el último acceso es mayor a 6 meses
        $ultimoCambio = $row['ultimoCambio'];
        $fechaActual = date("Y-m-d");
        $diferenciaMeses = (strtotime($fechaActual) - strtotime($ultimoCambio)) / (60 * 60 * 24 * 30);

        if ($diferenciaMeses > 6) {
            // Devolver un array con el error de cambio de contraseña
            echo json_encode([['error' => 'Debe cambiar la contrasena para poder ingresar']]);
        } else {
            // Devolver los datos del usuario dentro de un array
            echo json_encode([ $row ]);
        }
    } else {
        // Devolver un array con el error de usuario no encontrado
        echo json_encode([['error' => 'No coinciden los datos']]);
    }

    // Cerrar la consulta preparada
    mysqli_stmt_close($stmt);
} else {
    echo json_encode([['error' => 'ERROR en la preparación de la consulta: ' . mysqli_error($link)]]);
}

// Cerrar la conexión a la base de datos
mysqli_close($link);
?>
