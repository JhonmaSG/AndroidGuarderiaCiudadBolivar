<?php

include("./conexion.php");

$link = conectar();
$res = array();
$res['datos'] = array();
$sql = "SELECT * FROM Usuarios";
$resu = mysqli_query($link, $sql);
while($row=mysqli_fetch_array($resu)){
    $i['usuarioId'] = $row[0];
    $i['nombreUsuario'] = $row[1];
    $i['contrasena'] = $row[2];
    $i['rolId'] = $row[3];

    array_push($res['datos'], $i);
}
$res['success'] = "1";
echo json_encode($res);
mysqli_close($link);

?>