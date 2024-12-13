<?php
include('./conexion.php');
$link = conectar();

$sql = "SELECT * FROM Ninos";
$result = mysqli_query($link, $sql);

$datos = array();
while ($fila = mysqli_fetch_assoc($result)) {
    $datos[] = $fila;
}
echo json_encode($datos);
mysqli_close($link);
?>
