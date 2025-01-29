<?php
header('Content-Type: application/json');

include './conexion.php';

// Conectar a la base de datos
$conn = conectar(); 

// Verificar conexión
if ($conn->connect_error) {
    die("Conexión fallida: " . $conn->connect_error);
}

// Recuperar parámetros
$tipoReporte = isset($_POST['tipoReporte']) ? $_POST['tipoReporte'] : null;
$fechaInicio = isset($_POST['fechaInicio']) ? $_POST['fechaInicio'] : null;
$fechaFin = isset($_POST['fechaFin']) ? $_POST['fechaFin'] : null;
$acudienteId = isset($_POST['acudienteId']) ? intval($_POST['acudienteId']) : null; // ID opcional para filtrar por acudiente

// Validar parámetros mínimos
if (!$tipoReporte) {
    echo json_encode(["error" => "El parámetro 'tipoReporte' es obligatorio"]);
    $conn->close();
    exit;
}

// Construir consulta dinámica
$sql = "";
switch ($tipoReporte) {
    case "pagos":
        $sql = "SELECT p.idPago, p.fechaPago, p.monto, a.nombre AS acudiente, n.nombre AS nino 
                FROM PagosCotizantes p
                JOIN Acudiente a ON p.acudienteId = a.idAcudiente
                JOIN Ninos n ON p.ninoId = n.idNino";
        if ($fechaInicio && $fechaFin) {
            $sql .= " WHERE p.fechaPago BETWEEN '$fechaInicio' AND '$fechaFin'";
        }
        if ($acudienteId) {
            $sql .= $fechaInicio || $fechaFin ? " AND" : " WHERE";
            $sql .= " p.acudienteId = $acudienteId";
        }
        break;

    case "consumos":
        $sql = "SELECT c.idConsumo, c.fechaConsumo, n.nombre AS nino, m.nombreMenu AS menu 
                FROM Consumos c
                JOIN Ninos n ON c.ninoId = n.idNino
                JOIN MenuComidas m ON c.menuId = m.idMenu";
        if ($fechaInicio && $fechaFin) {
            $sql .= " WHERE c.fechaConsumo BETWEEN '$fechaInicio' AND '$fechaFin'";
        }
        break;

    case "alergias":
        $sql = "SELECT al.idAlergia, n.nombre AS nino, i.nombre AS ingrediente 
                FROM Alergias al
                JOIN Ninos n ON al.ninoId = n.idNino
                JOIN Ingredientes i ON al.ingredienteId = i.idIngrediente";
        if ($acudienteId) {
            $sql .= " WHERE n.acudienteId = $acudienteId";
        }
        break;

    default:
        echo json_encode(["error" => "El tipo de reporte no es válido"]);
        $conn->close();
        exit;
}

// Ejecutar consulta
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $response = [];
    while ($row = $result->fetch_assoc()) {
        $response[] = $row;
    }
    echo json_encode($response);
} else {
    echo json_encode(["message" => "No se encontraron resultados"]);
}

// Cerrar conexión
$conn->close();
?>