<?php
// Cargar las variables de entorno desde el archivo .env
function cargarVariablesDeEntorno($filename = '.env') {
    if (!file_exists($filename)) {
        throw new Exception("El archivo $filename no existe");
    }
    
    $vars = parse_ini_file($filename);
    foreach ($vars as $key => $value) {
        putenv("$key=$value");
    }
}

// Llamada a la función para cargar las variables
cargarVariablesDeEntorno();

function conectar(){
    $host = getenv('DB_HOST');
    $port = (int) getenv('DB_PORT');
    $user = "root";
    $pass = "";
    $bd = getenv('DB_NAME');

    // Depuración de los parámetros
    // echo "Host: $host, User: $user, Password: $pass, Database: $bd, Port: $port</br>";

    // Conectar al servidor MySQL
    $link = mysqli_connect($host, $user, $pass, $bd, $port);

    // Verificar la conexión al servidor MySQL
    if (!$link) {
        die("Error de conexión al servidor MySQL: " . mysqli_connect_error());
    }

    // Seleccionar la base de datos
    if (!mysqli_select_db($link, $bd)) {
        die("Error al seleccionar la base de datos: " . mysqli_error($link));
    }
    return $link;
}

// Probar la conexión llamando a la función
/*
$link = conectar();
if ($link) {
    echo "Conexión exitosa a la base de datos.";
}*/
?>