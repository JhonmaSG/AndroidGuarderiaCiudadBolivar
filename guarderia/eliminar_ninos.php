<?php
include('./conexion.php');
$link = conectar();

$noMatricula = $_POST['noMatricula'];

$sql = "DELETE FROM Ninos WHERE noMatricula='$noMatricula'";

if (mysqli_query($link, $sql)) {
    echo "Niño eliminado correctamente";
} else {
    echo "Error al eliminar el niño: " . mysqli_error($link);
}
mysqli_close($link);
?>
