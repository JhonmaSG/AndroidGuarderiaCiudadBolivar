<?php
header('Content-Type: application/json');

include './conexion.php';

// Conectar a la base de datos
$conn = conectar(); 

// Validar conexión
if ($conn->connect_error) {
    die(json_encode(["status" => "error", "message" => $conn->connect_error]));
}

// Obtener parámetros de la solicitud
$fechaInicio = $_GET['fechaInicio'] ?? null;
$fechaFin = $_GET['fechaFin'] ?? null;
$acudiente = $_GET['acudiente'] ?? null;

// Crear la consulta con parámetros
$query = "SELECT p.fechaPago, p.valorPago, a.nombre AS nombreAcudiente, n.nombre AS nombreNino
          FROM PagosCotizantes p
          INNER JOIN Acudiente a ON p.cedula = a.cedula
          INNER JOIN Ninos n ON p.noMatricula = n.noMatricula
          WHERE 1=1";

if ($fechaInicio && $fechaFin) {
    $query .= " AND p.fechaPago BETWEEN '$fechaInicio' AND '$fechaFin'";
}
if ($acudiente) {
    $query .= " AND a.nombre LIKE '%$acudiente%'";
}

$result = $conn->query($query);

// Generar la respuesta
if ($result->num_rows > 0) {
    $data = [];
    while ($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
    echo json_encode(["status" => "success", "data" => $data]);
} else {
    echo json_encode(["status" => "error", "message" => "No se encontraron datos"]);
}

$conn->close();
?>
