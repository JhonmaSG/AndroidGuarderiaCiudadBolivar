<?php
header('Content-Type: application/json');
require 'conexion.php';

$response = array();

try {
    $conexion = conectar();
    $consulta = "SELECT a.noMatricula, i.ingredienteId, i.nombreIngrediente, a.observaciones 
                 FROM Alergias a 
                 JOIN Ingredientes i ON a.ingredienteId = i.ingredienteId";
    $resultado = mysqli_query($conexion, $consulta);

    if ($resultado) {
        $datos = array();
        while ($fila = mysqli_fetch_assoc($resultado)) {
            $datos[] = array(
                'noMatricula' => $fila['noMatricula'],
                'ingredienteId' => $fila['nombreIngrediente'],
                'observaciones' => $fila['observaciones']
            );
        }

        $response['success'] = '1';
        $response['datos'] = $datos;
    } else {
        $response['success'] = '0';
        $response['mensaje'] = 'Error al obtener alergias';
    }
} catch (Exception $e) {
    $response['success'] = '0';
    $response['mensaje'] = 'Error: ' . $e->getMessage();
}

echo json_encode($response);
mysqli_close($conexion);
?>