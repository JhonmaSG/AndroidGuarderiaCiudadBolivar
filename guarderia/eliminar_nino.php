<?php
include 'conexion.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $conn = conectar();
    $noMatricula = $_POST['noMatricula'];

    $sql = "DELETE FROM ninos WHERE noMatricula = '$noMatricula'";

    if (mysqli_query($conn, $sql)) {
        echo "datos eliminados";
    } else {
        echo "error";
    }
    mysqli_close($conn);
}
?>
