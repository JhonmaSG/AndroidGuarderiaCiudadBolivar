<?php
// Incluir el archivo de conexión
include 'conexion.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['noMatricula'], $_POST['nombreIngrediente'], $_POST['observaciones'])) {
        
        $noMatricula = $_POST['noMatricula'];
        $nombreIngrediente = $_POST['nombreIngrediente'];
        $observaciones = $_POST['observaciones'];

        // Conectar a la base de datos
        $conexion = conectar();

        // Verificar si el ingrediente existe
        $sqlIngrediente = "SELECT ingredienteId FROM Ingredientes WHERE nombreIngrediente = ?";
        $stmt = $conexion->prepare($sqlIngrediente);
        $stmt->bind_param("s", $nombreIngrediente);
        $stmt->execute();
        $stmt->bind_result($ingredienteId);
        $stmt->fetch();
        $stmt->close();

        if (!$ingredienteId) {
            echo json_encode(["success" => 0, "message" => "El ingrediente no existe"]);
            exit;
        }

        // Insertar alergia
        $sqlInsert = "INSERT INTO Alergias (noMatricula, ingredienteId, observaciones) VALUES (?, ?, ?)";
        $stmtInsert = $conexion->prepare($sqlInsert);
        $stmtInsert->bind_param("iis", $noMatricula, $ingredienteId, $observaciones);

        if ($stmtInsert->execute()) {
            echo json_encode(["success" => 1, "message" => "Alergia registrada con éxito"]);
        } else {
            echo json_encode(["success" => 0, "message" => "Error al registrar la alergia"]);
        }

        $stmtInsert->close();
        $conexion->close();
    } else {
        echo json_encode(["success" => 0, "message" => "Faltan datos obligatorios"]);
    }
} else {
    echo json_encode(["success" => 0, "message" => "Método no permitido"]);
}
?>
