<?php
// Incluir el archivo de conexión
require_once 'conexion.php';

// Verificar que la solicitud sea POST
if ($_SERVER['REQUEST_METHOD'] != 'POST') {
    echo "Método no permitido";
    exit;
}

// Obtener los datos POST
$noMatricula = isset($_POST['noMatricula']) ? (int)$_POST['noMatricula'] : 0;
$nombre = isset($_POST['nombre']) ? $_POST['nombre'] : '';
$fechaNacimiento = isset($_POST['fechaNacimiento']) ? $_POST['fechaNacimiento'] : '';
$fechaIngreso = isset($_POST['fechaIngreso']) ? $_POST['fechaIngreso'] : '';
$acudienteCedula = isset($_POST['acudienteCedula']) ? (int)$_POST['acudienteCedula'] : 0;
$estado = isset($_POST['estado']) ? $_POST['estado'] : '';

// Validar que todos los campos necesarios estén presentes y sean válidos
if ($noMatricula <= 0 || empty($nombre) || empty($fechaNacimiento) || 
    empty($fechaIngreso) || $acudienteCedula <= 0 || empty($estado)) {
    echo "Todos los campos son requeridos y deben ser válidos";
    exit;
}

try {
    // Obtener la conexión
    $conexion = conectar();
    
    // Verificar que el acudiente existe
    $queryVerificarAcudiente = "SELECT cedula FROM Acudiente WHERE cedula = ?";
    $stmtVerificar = mysqli_prepare($conexion, $queryVerificarAcudiente);
    mysqli_stmt_bind_param($stmtVerificar, "i", $acudienteCedula);
    mysqli_stmt_execute($stmtVerificar);
    mysqli_stmt_store_result($stmtVerificar);
    
    if (mysqli_stmt_num_rows($stmtVerificar) == 0) {
        echo "El acudiente con cédula $acudienteCedula no existe";
        mysqli_close($conexion);
        exit;
    }
    
    // Verificar que el número de matrícula no esté duplicado
    $queryVerificarMatricula = "SELECT noMatricula FROM Ninos WHERE noMatricula = ?";
    $stmtVerificarMatricula = mysqli_prepare($conexion, $queryVerificarMatricula);
    mysqli_stmt_bind_param($stmtVerificarMatricula, "i", $noMatricula);
    mysqli_stmt_execute($stmtVerificarMatricula);
    mysqli_stmt_store_result($stmtVerificarMatricula);
    
    if (mysqli_stmt_num_rows($stmtVerificarMatricula) > 0) {
        echo "El número de matrícula ya existe";
        mysqli_close($conexion);
        exit;
    }

    // Preparar la consulta SQL para insertar el niño
    $query = "INSERT INTO Ninos (noMatricula, nombre, acudienteCedula, fechaNacimiento, fechaIngreso, estado) 
              VALUES (?, ?, ?, ?, ?, ?)";
    
    // Preparar el statement
    $stmt = mysqli_prepare($conexion, $query);
    
    // Vincular los parámetros
    // Notar que usamos "i" para los campos numéricos (noMatricula y acudienteCedula)
    mysqli_stmt_bind_param($stmt, "isisss", 
        $noMatricula,
        $nombre,
        $acudienteCedula,
        $fechaNacimiento,
        $fechaIngreso,
        $estado
    );
    
    // Ejecutar la consulta
    if (mysqli_stmt_execute($stmt)) {
        echo "datos guardados";
    } else {
        echo "Error al guardar los datos: " . mysqli_error($conexion);
    }
    
    // Cerrar el statement y la conexión
    mysqli_stmt_close($stmt);
    mysqli_close($conexion);
    
} catch (Exception $e) {
    echo "Error: " . $e->getMessage();
}
?>