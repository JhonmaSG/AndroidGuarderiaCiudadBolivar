<?php
header('Content-Type: application/json');
require 'conexion.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $conexion = conectar();
    $noMatricula = mysqli_real_escape_string($conexion, $_POST['noMatricula']);
    $ingredienteId = mysqli_real_escape_string($conexion, $_POST['ingredienteId']);

    try {
        $consulta = "DELETE FROM Alergias 
                     WHERE noMatricula = '$noMatricula' 
                     AND ingredienteId = '$ingredienteId'";
        
        if (mysqli_query($conexion, $consulta)) {
            echo "datos eliminados";
        } else {
            echo "No se pudo eliminar la alergia: " . mysqli_error($conexion);
        }
    } catch (Exception $e) {
        echo "Error: " . $e->getMessage();
    }

    mysqli_close($conexion);
}
?>