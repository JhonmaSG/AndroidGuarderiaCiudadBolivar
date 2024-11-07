<?php 
include './conexion.php';  
$link = conectar();

// Recibir los datos
$user = $_REQUEST['nombreUsuario'];
$pass = $_REQUEST['contrasena'];
$rolId = $_REQUEST['rolId'];

if(empty($user) || empty($pass) || empty($rolId)) {
    echo 'ERROR 1';      
    exit();
}

// Consulta SQL segura usando mysqli con `bind_param`
$sql = "SELECT * FROM usuarios WHERE nombreUsuario = ? AND contrasena = ? AND rolId = ?";
$stmt = mysqli_prepare($link, $sql);

if ($stmt) {
    // Bind de parámetros a la consulta
    mysqli_stmt_bind_param($stmt, "ssi", $user, $pass, $rolId);
    mysqli_stmt_execute($stmt);
    $res = mysqli_stmt_get_result($stmt);

    if (mysqli_num_rows($res) > 0) {
        $data = array();
        while($row = mysqli_fetch_assoc($res)) {
            $data[] = $row;
        }
        echo json_encode($data);
    } else {
        echo json_encode(['error' => 'Usuario no encontrado o rol no válido']);
    }

    // Cerrar la consulta preparada
    mysqli_stmt_close($stmt);
} else {
    echo "ERROR en la preparación de la consulta: " . mysqli_error($link);
}

// Cerrar la conexión a la base de datos
mysqli_close($link);
?>
