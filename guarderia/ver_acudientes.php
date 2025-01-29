<?php
require_once 'conexion.php';

$link = conectar();

// Verificar conexión
if (!$link) {
    echo json_encode([
        "success" => "0",
        "message" => "Error al conectar con la base de datos."
    ]);
    exit();
}

// Consulta a la base de datos
$query = "SELECT * FROM acudiente";
$result = mysqli_query($link, $query);

if ($result) {
    if (mysqli_num_rows($result) > 0) {
        $acudientes = [];
        while ($row = mysqli_fetch_assoc($result)) {
            // Opcionalmente renombra las claves para consistencia
            $acudientes[] = [
                "cedula" => $row['cedula'],
                "nombre" => $row['nombre'],
                "direccion" => $row['direccion'],
                "parentesco" => $row['parentesco'],
                "numeroCuenta" => $row['numeroCuenta']
            ];
        }
        echo json_encode([
            "success" => "1",
            "datos" => $acudientes
        ]);
    } else {
        echo json_encode([
            "success" => "0",
            "message" => "No se encontraron acudientes."
        ]);
    }
} else {
    echo json_encode([
        "success" => "0",
        "message" => "Error en la consulta: " . mysqli_error($link)
    ]);
}

// Cerrar conexión
mysqli_close($link);

?>
