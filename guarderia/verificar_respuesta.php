<?php
include './conexion.php';
$conn = conectar();

// Obtener los datos enviados
$nombreUsuario = $_POST['nombreUsuario'];
$dni = $_POST['dni'];
$respuesta = $_POST['respuesta'];

// Consultar la pregunta de seguridad y verificar la respuesta
$sql = "SELECT respuesta FROM usuarios WHERE nombreUsuario = ? AND dni = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("si", $nombreUsuario, $dni);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $respuesta_almacenada = $row['respuesta']; // Respuesta almacenada en la base de datos
    
    // Verificar la respuesta
    if ($respuesta_almacenada === $respuesta) {
        error_log("Respuesta correcta");
        echo "correcta";
    } else {
        error_log("Respuesta incorrecta");
        echo "incorrecta";
    }
} else {
    error_log("Usuario no encontrado");
    echo "Usuario no encontrado";
}
?>