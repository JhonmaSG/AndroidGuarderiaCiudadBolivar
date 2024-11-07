<?php
include './conexion.php';
$conn = conectar();

header('Content-Type: application/json');

// Obtener los parámetros enviados desde Android
$nombreUsuario = $_POST['nombreUsuario'];
$dni = $_POST['dni'];

// Consultar la pregunta de seguridad
$sql = "SELECT pregunta FROM usuarios WHERE nombreUsuario = ? AND dni = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("si", $nombreUsuario, $dni);
$stmt->execute();
$result = $stmt->get_result();

// Verifica si se encontró un resultado
if ($result->num_rows > 0) {
    // Obtener la pregunta de seguridad
    $row = $result->fetch_assoc();
    $pregunta = $row['pregunta'];
    
    // Retornar la respuesta con la pregunta
    echo json_encode(array("status" => "success", "pregunta" => $pregunta));
} else {
    // Si no se encuentra el usuario
    echo json_encode(array("status" => "error", "message" => "Error en la pregunta"));
}

// Cerrar la conexión a la base de datos
$conn->close();
?>
