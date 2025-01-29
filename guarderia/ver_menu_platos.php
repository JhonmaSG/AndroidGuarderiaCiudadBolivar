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

// Consulta para obtener los menús
$query = "SELECT * FROM MenuComidas"; // Asegúrate de que la tabla sea correcta
$result = $conn->query($query);

// Verificar si hubo un error en la consulta
if (!$result) {
    echo json_encode(array("error" => "Error en la consulta: " . $conn->error));
    exit;
}

// Verificar si hay resultados
if ($result->num_rows > 0) {
    $menus = array();
    
    // Recorrer los resultados y agregar cada menú al array
    while ($row = $result->fetch_assoc()) {
        $menus[] = $row;
    }

    // Devolver los menús en formato JSON
    echo json_encode($menus);
} else {
    echo json_encode(array("menus" => array(), "mensaje" => "No hay menús registrados"));
}

// Cerrar la conexión
$conn->close();
?>
