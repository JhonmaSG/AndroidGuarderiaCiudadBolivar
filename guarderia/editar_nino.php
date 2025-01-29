<?php
include 'conexion.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $conn = conectar();
    
    $noMatricula = $_POST['noMatricula'];
    $nombre = $_POST['nombre'];
    $fechaNacimiento = $_POST['fechaNacimiento'];
    $fechaIngreso = $_POST['fechaIngreso'];
    $estado = $_POST['estado'];

    $sql = "UPDATE ninos SET 
                nombre = '$nombre', 
                fechaNacimiento = '$fechaNacimiento', 
                fechaIngreso = '$fechaIngreso', 
                estado = '$estado'
            WHERE noMatricula = '$noMatricula'";

    if (mysqli_query($conn, $sql)) {
        echo "datos actualizados";
    } else {
        echo "error";
    }
    mysqli_close($conn);
}
?>
