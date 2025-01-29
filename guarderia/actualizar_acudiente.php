<?php
require_once 'conexion.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $cedula = $_POST['cedula'];
    $nombre = $_POST['nombre'];
    $direccion = $_POST['direccion'];
    $parentesco = $_POST['parentesco'];
    $numeroCuenta = $_POST['numeroCuenta'];

    $link = conectar();

    $query = "UPDATE Acudiente 
              SET nombre='$nombre', direccion='$direccion', parentesco='$parentesco', numeroCuenta='$numeroCuenta' 
              WHERE cedula=$cedula";

    if (mysqli_query($link, $query)) {
        echo "Registro actualizado exitosamente.";
    } else {
        echo "Error al actualizar el registro: " . mysqli_error($link);
    }

    mysqli_close($link);
}
?>
