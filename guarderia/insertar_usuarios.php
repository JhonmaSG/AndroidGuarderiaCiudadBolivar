<?php

include('./conexion.php');
$link = conectar();
$nombre = $_REQUEST['nombre'];
$contrasena = $_REQUEST['contrasena'];
$rol = $_REQUEST['rol'];

$sql = "INSERT INTO Usuarios (nombreUsuario, contrasena, rolId) VALUES ('$nombre', '$contrasena', '$rol')";
$res=mysqli_query($link, $sql);
if($res){
    echo "Usuario Registrado";
}else{
    echo "ERROR al Registrar el Usuario";
}
mysqli_close($link);

?>