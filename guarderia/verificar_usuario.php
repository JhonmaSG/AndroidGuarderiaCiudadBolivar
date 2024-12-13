<?php
include './conexion.php';
$conn = conectar();

header('Content-Type: application/json');

// Recibir datos
$nombreUsuario = $_POST['nombreUsuario'] ?? '';
$dni = $_POST['dni'] ?? '';

// Consulta para verificar si el usuario y el DNI existen
$sql = "SELECT * FROM usuarios WHERE nombreUsuario = ? AND dni = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("si", $nombreUsuario, $dni);
$stmt->execute();
$result = $stmt->get_result();

// Verifica si se encontró un resultado
if ($result->num_rows > 0) {
    echo json_encode(array("status" => "success"));
} else {
    echo json_encode(array("status" => "error", "message" => "Usuario o DNI incorrecto"));
}

// Cerrar la conexión a la base de datos
$conn->close();
?>
