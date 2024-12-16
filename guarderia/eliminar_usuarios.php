<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    include("./conexion.php");
    $link = conectar();
    $id = $_REQUEST['id'];
    $sql = "DELETE FROM Usuarios WHERE usuarioId='$id'";

    // Debug: Log de la consulta SQL
    error_log("SQL: " . $sql);

    $res = mysqli_query($link, $sql);

    if ($res) {
        echo "datos eliminados";
    } else {
        // Debug: Log si hay un error en la consulta
        error_log("Error al eliminar: " . mysqli_error($link));
        echo "No se puedo Eliminar el Usuario";
    }

    mysqli_close($link);
}


?>