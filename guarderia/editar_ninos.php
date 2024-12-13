<?php
include('./conexion.php');
$link = conectar();

$noMatricula = $_POST['noMatricula'];
$nombre = $_POST['nombre'];
$fechaNacimiento = $_POST['fechaNacimiento'];
$fechaIngreso = $_POST['fechaIngreso'];
$fechaFin = $_POST['fechaFin'];
$estado = $_POST['estado'];

$sql = "UPDATE Ninos SET 
        nombre='$nombre', 
        fechaNacimiento='$fechaNacimiento', 
        fechaIngreso='$fechaIngreso', 
        fechaFin='$fechaFin', 
        estado='$estado' 
        WHERE noMatricula='$noMatricula'";

if (mysqli_query($link, $sql)) {
    echo "Niño actualizado correctamente";
} else {
    echo "Error al actualizar el niño: " . mysqli_error($link);
}
mysqli_close($link);
?>
