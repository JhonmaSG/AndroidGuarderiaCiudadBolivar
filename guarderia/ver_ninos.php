<?php
include 'conexion.php';

$conn = conectar();
$response = array();

$sql = "SELECT * FROM ninos";
$result = mysqli_query($conn, $sql);

if (mysqli_num_rows($result) > 0) {
    $response["success"] = "1";
    $response["datos"] = array();
    
    while ($row = mysqli_fetch_assoc($result)) {
        $nino = array(
            "noMatricula" => $row["noMatricula"],
            "nombre" => $row["nombre"],
            "acudienteCedula" => $row["acudienteCedula"],
            "fechaNacimiento" => $row["fechaNacimiento"],
            "fechaIngreso" => $row["fechaIngreso"],
            "fechaFin" => $row["fechaFin"],
            "estado" => $row["estado"]
        );
        array_push($response["datos"], $nino);
    }
} else {
    $response["success"] = "0";
    $response["message"] = "No hay datos disponibles.";
}

echo json_encode($response);
mysqli_close($conn);
?>
