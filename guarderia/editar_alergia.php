<?php
include('./conexion.php');
$link = conectar();

$noMatricula = $_POST['noMatricula'];
$ingredienteId = $_POST['ingredienteId'];
$observaciones = $_POST['observaciones'];

$sql = "UPDATE Alergias SET 
        observaciones='$observaciones'
        WHERE noMatricula='$noMatricula' AND ingredienteId='$ingredienteId'";

if (mysqli_query($link, $sql)) {
    echo "Alergia actualizada correctamente";
} else {
    echo "Error al actualizar la alergia: " . mysqli_error($link);
}
mysqli_close($link);
?>
