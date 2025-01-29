<?php
header('Content-Type: application/json');

include './conexion.php';

$conn = conectar(); 

if (!$conn) {
    echo json_encode(array("error" => "Error en la conexión: " . mysqli_connect_error()));
    exit;
}

$query = "SELECT * FROM ninos";
$result = $conn->query($query);

if (!$result) {
    echo json_encode(array("error" => "Error en la consulta: " . $conn->error));
    exit;
}

if ($result->num_rows > 0) {
    $ninos = array();
    while ($row = $result->fetch_assoc()) {
        $ninos[] = $row;
    }
    echo json_encode($ninos);
} else {
    echo json_encode(array("ninos" => array(), "mensaje" => "No hay niños registrados"));
}

$conn->close();
?>
