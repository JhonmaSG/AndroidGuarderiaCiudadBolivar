<?php
header('Content-Type: application/json');

// Incluir la conexión a la base de datos
include './conexion.php';

// Conectar a la base de datos
$conn = conectar(); 

// Verificar la conexión
if ($conn->connect_error) {
    die("Conexión fallida: " . $conn->connect_error);
}

// Obtener los datos enviados en la solicitud POST
$numeroMatricula = isset($_POST['numeroMatricula']) ? $_POST['numeroMatricula'] : '';
$nombreEstudiante = isset($_POST['nombreEstudiante']) ? $_POST['nombreEstudiante'] : '';

// Consultar la base de datos
$sql = "
SELECT 
    n.noMatricula,
    n.nombre AS nombreNino,
    n.fechaNacimiento,
    n.fechaIngreso,
    n.fechaFin,
    n.estado AS estadoNino,
    a.nombre AS nombreAcudiente,
    a.direccion,
    a.parentesco,
    ta.telefono,
    al.observaciones AS alergiasObservaciones,
    i.nombreIngrediente AS alergiaIngrediente
FROM Ninos n
JOIN Acudiente a ON n.acudienteCedula = a.cedula
JOIN TelefonosAcudiente ta ON a.cedula = ta.cedula
LEFT JOIN Alergias al ON n.noMatricula = al.noMatricula
LEFT JOIN Ingredientes i ON al.ingredienteId = i.ingredienteId
WHERE 
    ('$numeroMatricula' = '' OR n.noMatricula = '$numeroMatricula') 
    AND ('$nombreEstudiante' = '' OR n.nombre LIKE '%$nombreEstudiante%')
ORDER BY n.noMatricula;
";

// Ejecutar la consulta
$result = $conn->query($sql);

// Verificar si hay resultados
if ($result->num_rows > 0) {
    // Crear un array para almacenar los datos
    $data = [];
    while($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
    // Retornar los datos en formato JSON
    echo json_encode(["success" => true, "data" => $data]);
} else {
    echo json_encode(["success" => false, "message" => "No se encontraron datos"]);
}

// Cerrar la conexión
$conn->close();
?>
