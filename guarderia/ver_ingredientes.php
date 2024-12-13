<?php

include("./conexion.php");

$link = conectar();
$res = array();
$res['datos'] = array();
$sql = "SELECT * FROM Ingredientes";
$resu = mysqli_query($link, $sql);
while($row=mysqli_fetch_array($resu)){
    $i['ingredienteId'] = $row[0];
    $i['nombreIngrediente'] = $row[1];

    array_push($res['datos'], $i);
}
$res['success'] = "1";
echo json_encode($res);
mysqli_close($link);

?>