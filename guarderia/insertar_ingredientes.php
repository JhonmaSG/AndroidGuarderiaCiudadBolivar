<?php

include('./conexion.php');
$link = conectar();
$nombre = $_REQUEST['nombre'];

$sql = "INSERT INTO Ingredientes (nombreIngrediente) VALUES ('$nombre')";
$res=mysqli_query($link, $sql);
if($res){
    echo "Ingrediente Registrado";
}else{
    echo "ERROR al Reegistrar el Ingrediente";
}
mysqli_close($link);

?>