<?php
require_once 'conexion.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $cedula = $_POST['cedula'] ?? null;
    $nombre = $_POST['nombre'] ?? null;
    $direccion = $_POST['direccion'] ?? null;
    $parentesco = $_POST['parentesco'] ?? null;
    $numeroCuenta = $_POST['numeroCuenta'] ?? null;

    if ($cedula && $nombre && $direccion && $parentesco && $numeroCuenta) {
        $link = conectar();
        $query = "INSERT INTO acudiente (cedula, nombre, direccion, parentesco, numeroCuenta) 
                  VALUES ('$cedula', '$nombre', '$direccion', '$parentesco', '$numeroCuenta')";

        if (mysqli_query($link, $query)) {
            echo json_encode(["success" => "1", "message" => "Acudiente insertado correctamente."]);
        } else {
            echo json_encode(["success" => "0", "message" => "Error al insertar el acudiente: " . mysqli_error($link)]);
        }
        mysqli_close($link);
    } else {
        echo json_encode(["success" => "0", "message" => "Todos los campos son obligatorios."]);
    }
} else {
    echo json_encode(["success" => "0", "message" => "Método no permitido."]);
}
?>
