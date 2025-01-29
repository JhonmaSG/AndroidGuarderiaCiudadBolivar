<?php
header('Content-Type: application/json');
require 'conexion.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $conexion = conectar();
    $noMatriculaOriginal = mysqli_real_escape_string($conexion, $_POST['noMatriculaOriginal']);
    $ingredienteIdOriginal = mysqli_real_escape_string($conexion, $_POST['ingredienteIdOriginal']);
    $noMatricula = mysqli_real_escape_string($conexion, $_POST['noMatricula']);
    $nombreIngrediente = mysqli_real_escape_string($conexion, $_POST['nombreIngrediente']);
    $observaciones = mysqli_real_escape_string($conexion, $_POST['observaciones']);

    try {
        // Obtener el ingredienteId basado en el nombreIngrediente
        $consultaIngrediente = "SELECT ingredienteId FROM Ingredientes WHERE nombreIngrediente = '$nombreIngrediente'";
        $resultadoIngrediente = mysqli_query($conexion, $consultaIngrediente);
        
        if ($resultadoIngrediente && mysqli_num_rows($resultadoIngrediente) > 0) {
            $filaIngrediente = mysqli_fetch_assoc($resultadoIngrediente);
            $ingredienteId = $filaIngrediente['ingredienteId'];

            // Actualizar la alergia
            $consulta = "UPDATE Alergias 
                         SET noMatricula = '$noMatricula', 
                             ingredienteId = '$ingredienteId', 
                             observaciones = '$observaciones' 
                         WHERE noMatricula = '$noMatriculaOriginal' 
                         AND ingredienteId = '$ingredienteIdOriginal'";
            
            if (mysqli_query($conexion, $consulta)) {
                echo "Alergia Actualizada";
            } else {
                echo "Error al actualizar alergia: " . mysqli_error($conexion);
            }
        } else {
            echo "Ingrediente no encontrado";
        }
    } catch (Exception $e) {
        echo "Error: " . $e->getMessage();
    }

    mysqli_close($conexion);
}
?>