<?php

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    include("./conexion.php");
    $link = conectar();
    $id = $_REQUEST['ingredienteId'];
    $nombre = $_REQUEST['nombreIngrediente'];

    $sql = "UPDATE Ingredientes SET nombreIngrediente='$nombre' WHERE ingredienteId='$id'";
    $res = mysqli_query($link, $sql);
    if($res){
        echo "Se Actualizo el Ingrediente";
    }else{
        echo "No existe el Ingrediente";
    }
    mysqli_close($link);
}

?>