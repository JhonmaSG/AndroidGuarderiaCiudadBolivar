<?php
header('Content-Type: application/json');

// Incluir la conexión a la base de datos
include './conexion.php';

// Conectar a la base de datos
$conn = conectar(); 

// Verificar la conexión
if ($conn->connect_error) {
    die(json_encode(["status" => "error", "message" => $conn->connect_error]));
}

// Obtener las fechas enviadas en la solicitud GET
$fechaInicio = isset($_GET['fechaInicio']) ? $_GET['fechaInicio'] : '';
$fechaFin = isset($_GET['fechaFin']) ? $_GET['fechaFin'] : '';

// Validar el formato de las fechas (YYYY-MM-DD)
if (!preg_match("/^\d{4}-\d{2}-\d{2}$/", $fechaInicio) || !preg_match("/^\d{4}-\d{2}-\d{2}$/", $fechaFin)) {
    echo json_encode(["status" => "error", "message" => "Formato de fechas incorrecto."]);
    exit();
}

// Verificar si las fechas son válidas
if (empty($fechaInicio) || empty($fechaFin)) {
    echo json_encode(["status" => "error", "message" => "Fechas inválidas."]);
    exit();
}

// Consultar las alergias en el rango de fechas
$sql = "
SELECT
    n.nombre AS NombreNiño,
    al.observaciones AS Observaciones,
    rc.fecha AS FechaConsumo,
    mc.nombreMenu AS NombreMenu,
    p.nombrePlato AS NombrePlato
FROM
    Alergias al
JOIN
    Ninos n ON al.noMatricula = n.noMatricula
JOIN
    Ingredientes i ON al.ingredienteId = i.ingredienteId
JOIN
    RegistroComidas rc ON n.noMatricula = rc.noMatricula
JOIN
    MenuComidas mc ON rc.noMenu = mc.noMenu
JOIN
    Platos p ON mc.noMenu = p.noMenu
WHERE
    rc.fecha BETWEEN '$fechaInicio' AND '$fechaFin'
ORDER BY
    rc.fecha, n.nombre;
";

// Ejecutar la consulta
$result = $conn->query($sql);

// Verificar si hay resultados
if ($result->num_rows > 0) {
    // Crear un array para almacenar los datos
    $data = [];
    while ($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
    // Retornar los datos en formato JSON con "status"
    echo json_encode(["status" => "success", "data" => $data]);
} else {
    echo json_encode(["status" => "error", "message" => "No se encontraron registros de alergias."]);
}

// Cerrar la conexión
$conn->close();
?>
