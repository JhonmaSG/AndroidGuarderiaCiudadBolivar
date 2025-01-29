<?php
header('Content-Type: application/json');

// Incluir la conexión a la base de datos
include './conexion.php';

// Conectar a la base de datos
$conn = conectar(); 

// Verificar si la conexión fue exitosa
if (!$conn) {
    echo json_encode(array("error" => "Error en la conexión: " . mysqli_connect_error()));
    exit;
}

// Obtener el noMenu del parámetro de la solicitud
$menuId = isset($_GET['menuId']) ? $_GET['menuId'] : '';

// Verificar que se haya recibido un ID de menú
if (empty($menuId)) {
    echo json_encode(array("error" => "ID de menú no proporcionado"));
    exit;
}

// Consulta para obtener los platos de un menú específico
$query = "SELECT * FROM Platos WHERE noMenu = '$menuId'";  // Asegúrate de que la tabla sea correcta
$result = $conn->query($query);

// Verificar si hubo un error en la consulta
if (!$result) {
    echo json_encode(array("error" => "Error en la consulta: " . $conn->error));
    exit;
}

// Verificar si hay resultados
if ($result->num_rows > 0) {
    $platos = array();
    
    // Recorrer los resultados y agregar cada plato al array
    while ($row = $result->fetch_assoc()) {
        $platos[] = $row;
    }

    // Devolver los platos en formato JSON
    echo json_encode($platos);
} else {
    echo json_encode(array("platos" => array(), "mensaje" => "No hay platos registrados"));
}

// Cerrar la conexión
$conn->close();
?>
