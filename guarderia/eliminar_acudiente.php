<?php
require_once 'conexion.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $cedula = $_POST['cedula'] ?? null;

    if ($cedula) {
        $link = conectar();
        $query = "DELETE FROM acudiente WHERE cedula='$cedula'";

        if (mysqli_query($link, $query)) {
            echo "datos eliminados";
        } else {
            echo "Error al eliminar el acudiente: " . mysqli_error($link);
        }
        mysqli_close($link);
    } else {
        echo "La cédula es requerida.";
    }
} else {
    echo "Método no permitido.";
}
?>
