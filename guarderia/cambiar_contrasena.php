<?php
include './conexion.php';
$conn = conectar();

header('Content-Type: application/json');

// Obtener los parámetros enviados desde Android
$nombreUsuario = $_POST['nombreUsuario'];
$dni = $_POST['dni'];
$nuevaContrasena = $_POST['nuevaContrasena'];

// Consultar si el usuario con ese nombre y DNI existe
$sql = "SELECT usuarioId FROM usuarios WHERE nombreUsuario = ? AND dni = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("si", $nombreUsuario, $dni);
$stmt->execute();
$result = $stmt->get_result();

// Verifica si se encontró el usuario
if ($result->num_rows > 0) {
    // El usuario existe, proceder con la actualización de la contraseña y último acceso
    $sql_update = "UPDATE usuarios SET contrasena = ?, ultimoCambio = NOW() WHERE nombreUsuario = ? AND dni = ?";
    $stmt_update = $conn->prepare($sql_update);
    $stmt_update->bind_param("ssi", $nuevaContrasena, $nombreUsuario, $dni);
    
    if ($stmt_update->execute()) {
        echo json_encode(array("status" => "success", "message" => "Contraseña modificada exitosamente"));
    } else {
        echo json_encode(array("status" => "error", "message" => "Error al modificar la contraseña"));
    }
} else {
    // El usuario no existe
    echo json_encode(array("status" => "error", "message" => "Usuario no encontrado"));
}

// Cerrar la conexión a la base de datos
$conn->close();
?>
