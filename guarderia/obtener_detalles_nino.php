<?php
header('Content-Type: application/json');
include './conexion.php';

$conn = conectar();

if (isset($_GET['noMatricula'])) {
    $noMatricula = $_GET['noMatricula'];

    $query = "SELECT * FROM Ninos WHERE noMatricula = ?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("i", $noMatricula);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $nino = $result->fetch_assoc();
        echo json_encode($nino);
    } else {
        echo json_encode(array("mensaje" => "Niño no encontrado"));
    }

    $stmt->close();
} else {
    echo json_encode(array("mensaje" => "Matrícula no proporcionada"));
}

$conn->close();
?>
