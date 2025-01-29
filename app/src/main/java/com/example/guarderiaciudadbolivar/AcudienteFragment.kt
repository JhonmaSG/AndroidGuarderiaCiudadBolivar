package com.example.guarderiaciudadbolivar

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject

class AcudienteFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: AcudienteAdapter
    private val acudientesArrayList = ArrayList<Acudiente>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_acudiente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.listAcudiente)
        adapter = AcudienteAdapter(requireContext(), acudientesArrayList)
        listView.adapter = adapter
        val floatingButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)

        floatingButton.setOnClickListener {
            val intent = Intent(requireContext(), AgregarAcudiente::class.java)
            startActivity(intent)
        }
        listView.setOnItemClickListener { _, _, position, _ ->
            val acudiente = acudientesArrayList[position]
            val builder = AlertDialog.Builder(requireContext())
            val dialogItems = arrayOf("Ver Datos", "Editar Acudiente", "Eliminar Acudiente")
            builder.setTitle(acudiente.nombre)
            builder.setItems(dialogItems) { _, i ->
                when (i) {
                    0 -> {
                        val intent = Intent(requireContext(), DetallesAcudiente::class.java).apply {
                            putExtra("cedula", acudiente.cedula)
                            putExtra("nombre", acudiente.nombre)
                            putExtra("direccion", acudiente.direccion)
                            putExtra("parentesco", acudiente.parentesco)
                            putExtra("numeroCuenta", acudiente.numeroCuenta)
                        }
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(requireContext(), EditarAcudiente::class.java).apply {
                            putExtra("cedula", acudiente.cedula)
                            putExtra("nombre", acudiente.nombre)
                            putExtra("direccion", acudiente.direccion)
                            putExtra("parentesco", acudiente.parentesco)
                            putExtra("numeroCuenta", acudiente.numeroCuenta)
                        }
                        startActivity(intent)
                    }
                    2 -> eliminarAcudiente(acudiente.cedula)
                }
            }
            builder.create().show()
        }


        // Cargar los datos de los acudientes
        cargarAcudientes()
    }

    private fun cargarAcudientes() {
        val url = "${getString(R.string.url)}/ver_acudientes.php"
        val progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Cargando...")
            setCancelable(false)
            show()
        }

        val request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                progressDialog.dismiss()
                try {
                    acudientesArrayList.clear()
                    val jsonArray = JSONObject(response).getJSONArray("datos")
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val acudiente = Acudiente(
                            obj.getInt("cedula"),
                            obj.getString("nombre"),
                            obj.getString("direccion"),
                            obj.getString("parentesco"),
                            obj.getString("numeroCuenta")
                        )
                        acudientesArrayList.add(acudiente)
                    }
                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Error de red: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(requireContext()).add(request)
    }

    private fun verDetalles(cedula: Int) {
        val intent = Intent(requireContext(), DetallesAcudiente::class.java)
        intent.putExtra("cedula", cedula)
        startActivity(intent)
    }

    private fun editarAcudiente(cedula: Int) {
        val intent = Intent(requireContext(), EditarAcudiente::class.java)
        intent.putExtra("cedula", cedula)
        startActivity(intent)
    }

    private fun eliminarAcudiente(cedula: Int) {
        val url = "${getString(R.string.url)}/eliminar_acudiente.php"
        val progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Eliminando...")
            setCancelable(false)
            show()
        }

        val request = object : StringRequest(
            Method.POST,
            url,
            { response ->
                progressDialog.dismiss()
                if (response.trim() == "datos eliminados") {
                    Toast.makeText(requireContext(), "Acudiente eliminado", Toast.LENGTH_SHORT).show()
                    cargarAcudientes()
                } else {
                    Toast.makeText(requireContext(), "No se pudo eliminar", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                return hashMapOf("cedula" to cedula.toString())
            }
        }

        Volley.newRequestQueue(requireContext()).add(request)
    }

}
