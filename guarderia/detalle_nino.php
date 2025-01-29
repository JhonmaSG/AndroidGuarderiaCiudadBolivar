<?php
include 'conexion.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $conn = conectar();
    $noMatricula = $_POST['noMatricula'];

    $sql = "SELECT * FROM ninos WHERE noMatricula = '$noMatricula'";
    $result = mysqli_query($conn, $sql);

    if (mysqli_num_rows($result) > 0) {
        $row = mysqli_fetch_assoc($result);
        $response = array(
            "success" => "1",
            "noMatricula" => $row["noMatricula"],
            "nombre" => $row["nombre"],
            "acudienteCedula" => $row["acudienteCedula"],
            "fechaNacimiento" => $row["fechaNacimiento"],
            "fechaIngreso" => $row["fechaIngreso"],
            "estado" => $row["estado"]
        );
    } else {
        $response["success"] = "0";
        $response["message"] = "No se encontraron datos.";
    }
    echo json_encode($response);
    mysqli_close($conn);
}
?>
