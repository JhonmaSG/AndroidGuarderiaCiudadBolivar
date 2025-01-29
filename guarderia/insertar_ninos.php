<?php
include('./conexion.php');
$link = conectar();

$noMatricula = $_POST['noMatricula'];
$nombre = $_POST['nombre'];
$fechaNacimiento = $_POST['fechaNacimiento'];
$fechaIngreso = $_POST['fechaIngreso'];
$fechaFin = $_POST['fechaFin'];
$estado = $_POST['estado'];

$sql = "INSERT INTO Ninos (noMatricula, nombre, fechaNacimiento, fechaIngreso, fechaFin, estado)
        VALUES ('$noMatricula', '$nombre', '$fechaNacimiento', '$fechaIngreso', '$fechaFin', '$estado')";

if (mysqli_query($link, $sql)) {
    echo "Niño registrado exitosamente";
} else {
    echo "Error al registrar el niño: " . mysqli_error($link);
}
mysqli_close($link);
?>
