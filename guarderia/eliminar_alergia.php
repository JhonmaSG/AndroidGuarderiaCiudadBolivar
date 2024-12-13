<?php
include('./conexion.php');
$link = conectar();

$noMatricula = $_POST['noMatricula'];
$ingredienteId = $_POST['ingredienteId'];

$sql = "DELETE FROM Alergias WHERE noMatricula='$noMatricula' AND ingredienteId='$ingredienteId'";

if (mysqli_query($link, $sql)) {
    echo "Alergia eliminada correctamente";
} else {
    echo "Error al eliminar la alergia: " . mysqli_error($link);
}
mysqli_close($link);
?>
