<?php

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    include("./conexion.php");
    $link = conectar();
    $id = $_REQUEST['usuarioId'];
    $nombre = $_REQUEST['nombreUsuario'];
    $contrasena = $_REQUEST['contrasena'];
    $rol = $_REQUEST['rolId'];

    $sql = "UPDATE Usuarios SET nombreUsuario='$nombre', contrasena='$contrasena', rolId='$rol' WHERE usuarioId='$id'";
    $res = mysqli_query($link, $sql);
    if($res){
        echo "Se Actualizo el Usuario";
    }else{
        echo "No existe el Usuario";
    }
    mysqli_close($link);
}

?>