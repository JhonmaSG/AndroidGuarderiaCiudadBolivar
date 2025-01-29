<?php

include './conexion.php';

// Conectar a la base de datos
$conn = conectar(); 

if ($conn->connect_error) {
    die("Error de conexión: " . $conn->connect_error);
}

$mensaje = "";
$valorPago = 0;
$mostrarFormularioPago = false;
$script = ""; // Variable para almacenar scripts JS

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['buscar'])) {
        $noMatricula = intval($_POST['noMatricula']);
        $sql = "SELECT SUM(costoPorComida) AS totalPago FROM Consumos WHERE noMatricula = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("i", $noMatricula);
        $stmt->execute();
        $result = $stmt->get_result();
        $data = $result->fetch_assoc();

        if ($data['totalPago'] !== null) {
            $valorPago = $data['totalPago'];
            $mostrarFormularioPago = true;
        } else {
            $mensaje = "No hay consumos registrados para este número de matrícula.";
        }
    }

    if (isset($_POST['pagar'])) {
        $noMatricula = intval($_POST['noMatricula']);
        $cedula = intval($_POST['cedula']);
        $valorPago = floatval($_POST['valorPago']);
        $fechaPago = date('Y-m-d');
        $estado = 'Aceptado';

        $sql = "INSERT INTO PagosCotizantes (cedula, noMatricula, valorPago, fechaPago, estado) 
                VALUES (?, ?, ?, ?, ?)";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("iidss", $cedula, $noMatricula, $valorPago, $fechaPago, $estado);

        if ($stmt->execute()) {
            // Script de SweetAlert almacenado en una variable
            $script = "<script>
                        Swal.fire({
                            title: 'Pago Exitoso',
                            text: 'El pago ha sido registrado con éxito.',
                            icon: 'success',
                            confirmButtonText: 'Aceptar'
                        });
                       </script>";
            $valorPago = 0;
            $mostrarFormularioPago = false;
        } else {
            $mensaje = "Error al registrar el pago: " . $conn->error;
        }
    }
}
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Módulo de Pagos</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            margin-top: 50px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="text-center mb-4">Módulo de Pagos</h1>
        
        <!-- Formulario para buscar el valor a pagar -->
        <div class="card">
            <div class="card-header bg-primary text-white">Buscar Valor a Pagar</div>
            <div class="card-body">
                <form method="POST">
                    <div class="form-group">
                        <label for="noMatricula">Número de Matrícula del Niño:</label>
                        <input type="number" name="noMatricula" id="noMatricula" class="form-control" required>
                    </div>
                    <button type="submit" name="buscar" class="btn btn-primary btn-block">Buscar</button>
                </form>
            </div>
        </div>

        <?php if ($mostrarFormularioPago): ?>
        <div class="card mt-4">
            <div class="card-header bg-success text-white">Registrar Pago</div>
            <div class="card-body">
                <p><strong>Valor a Pagar:</strong> $<?php echo number_format($valorPago, 2); ?></p>
                <form method="POST">
                    <input type="hidden" name="noMatricula" value="<?php echo htmlspecialchars($_POST['noMatricula']); ?>">
                    <input type="hidden" name="valorPago" value="<?php echo $valorPago; ?>">

                    <div class="form-group">
                        <label for="cedula">Cédula del Acudiente:</label>
                        <input type="number" name="cedula" id="cedula" class="form-control" required>
                    </div>

                    <!-- Método de Pago -->
                    <div class="form-group">
                        <label for="metodoPago">Método de Pago:</label>
                        <select id="metodoPago" class="form-control" onchange="mostrarOpcionesPago()">
                            <option value="">Seleccione una opción</option>
                            <option value="efectivo">Efectivo</option>
                            <option value="tarjeta">Tarjeta</option>
                        </select>
                    </div>

                    <!-- Opciones de Pago Decorativas -->
                    <div id="opcionEfectivo" style="display: none;">
                        <label for="factura">Subir Factura (Simulación):</label>
                        <input type="file" class="form-control">
                    </div>
                    <div id="opcionTarjeta" style="display: none;">
                        <label for="numeroTarjeta">Número de Tarjeta (Simulación):</label>
                        <input type="text" id="numeroTarjeta" class="form-control" placeholder="XXXX-XXXX-XXXX-XXXX">
                    </div>

                    <button type="submit" name="pagar" class="btn btn-success btn-block mt-3">Registrar Pago</button>
                </form>
            </div>
        </div>
        <?php endif; ?>
        
        <?php if ($mensaje): ?>
            <div class="alert alert-info mt-4"> <?php echo $mensaje; ?> </div>
        <?php endif; ?>
    </div>

    <!-- Scripts -->
    <script>
        function mostrarOpcionesPago() {
            const metodo = document.getElementById('metodoPago').value;
            document.getElementById('opcionEfectivo').style.display = (metodo === 'efectivo') ? 'block' : 'none';
            document.getElementById('opcionTarjeta').style.display = (metodo === 'tarjeta') ? 'block' : 'none';
        }
    </script>
    <?php
        // Imprimir el script de SweetAlert al final del HTML
        echo $script;
    ?>
</body>
</html>
