<?php
include('./conexion.php');
$link = conectar();

$noMatricula = $_POST['noMatricula'];
$ingredienteId = $_POST['ingredienteId'];
$observaciones = $_POST['observaciones'];

$sql = "INSERT INTO Alergias (noMatricula, ingredienteId, observaciones)
        VALUES ('$noMatricula', '$ingredienteId', '$observaciones')";

if (mysqli_query($link, $sql)) {
    echo "Alergia registrada exitosamente";
} else {
    echo "Error al registrar la alergia: " . mysqli_error($link);
}
mysqli_close($link);
?>
