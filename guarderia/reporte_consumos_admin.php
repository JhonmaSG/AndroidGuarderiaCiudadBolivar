<?php
header('Content-Type: application/json');

// Incluir la conexión a la base de datos
include './conexion.php';

try {
    // Conectar a la base de datos
    $conn = conectar(); 

    // Verificar la conexión
    if ($conn->connect_error) {
        throw new Exception("Conexión fallida: " . $conn->connect_error);
    }

    // Obtener las fechas enviadas en la solicitud GET
    $fechaInicio = isset($_GET['fechaInicio']) ? $_GET['fechaInicio'] : '';
    $fechaFin = isset($_GET['fechaFin']) ? $_GET['fechaFin'] : '';

    // Verificar si las fechas están vacías
    if (empty($fechaInicio) || empty($fechaFin)) {
        echo json_encode(["status" => "error", "message" => "Fechas inválidas."]);
        exit();
    }

    // Validar el formato de las fechas (YYYY-MM-DD)
    if (!preg_match("/^\d{4}-\d{2}-\d{2}$/", $fechaInicio) || !preg_match("/^\d{4}-\d{2}-\d{2}$/", $fechaFin)) {
        echo json_encode(["status" => "error", "message" => "Formato de fechas incorrecto."]);
        exit();
    }

    // Preparar la consulta SQL con sentencias preparadas (prevención de inyecciones SQL)
    $sql = "
    SELECT 
        c.fechaConsumo,
        c.CostoPorComida,
        n.nombre AS nombreNino,
        a.nombre AS nombreAcudiente
    FROM Consumos c
    JOIN Ninos n ON c.noMatricula = n.noMatricula
    JOIN Acudiente a ON n.acudienteCedula = a.cedula
    WHERE c.fechaConsumo BETWEEN ? AND ?
    ORDER BY c.fechaConsumo;
    ";

    // Preparar la sentencia SQL
    $stmt = $conn->prepare($sql);
    if ($stmt === false) {
        throw new Exception("Error al preparar la consulta SQL: " . $conn->error);
    }

    // Enlazar los parámetros
    $stmt->bind_param("ss", $fechaInicio, $fechaFin);

    // Ejecutar la consulta
    $stmt->execute();

    // Obtener el resultado
    $result = $stmt->get_result();

    // Verificar si hay resultados
    if ($result->num_rows > 0) {
        // Crear un array para almacenar los datos
        $data = [];
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
        // Retornar los datos en formato JSON
        echo json_encode(["status" => "success", "data" => $data]);
    } else {
        echo json_encode(["status" => "error", "message" => "No se encontraron consumos en este rango de fechas."]);
    }

    // Cerrar la sentencia
    $stmt->close();
} catch (Exception $e) {
    // Capturar cualquier excepción y devolver un mensaje de error
    echo json_encode(["status" => "error", "message" => $e->getMessage()]);
} finally {
    // Cerrar la conexión
    if (isset($conn) && $conn->ping()) {
        $conn->close();
    }
}
?>
