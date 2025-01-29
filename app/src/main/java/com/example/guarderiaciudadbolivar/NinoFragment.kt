package com.example.guarderiaciudadbolivar

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat

class NinoFragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var adapter: NinoAdapter
    private val ninosArrayList = ArrayList<Nino>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nino, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val floatingButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingButton.setOnClickListener {
            agregarNino()
        }

        listView = view.findViewById(R.id.listNino)
        adapter = NinoAdapter(requireContext(), ninosArrayList)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val builder = AlertDialog.Builder(requireContext())
            val dialogItems = arrayOf("Ver Datos", "Editar Niño", "Eliminar Niño")
            builder.setTitle(ninosArrayList[position].nombre)
            builder.setItems(dialogItems) { _, i ->
                when (i) {
                    0 -> {
                        val intent = Intent(requireContext(), DetallesNino::class.java)
                        intent.putExtra("noMatricula", ninosArrayList[position].noMatricula)
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(requireContext(), EditarNino::class.java)
                        intent.putExtra("noMatricula", ninosArrayList[position].noMatricula)
                        startActivity(intent)
                    }
                    2 -> eliminarNino(ninosArrayList[position].noMatricula)
                }
            }
            builder.create().show()
        }

        cargarNinos()
    }

    private fun cargarNinos() {
        val url = getString(R.string.url) + "/ver_ninos.php"
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Cargando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val stringRequest = StringRequest(Request.Method.POST, url, { response ->
            progressDialog.dismiss()
            try {
                ninosArrayList.clear()
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                if (success == "1") {
                    val jsonArray: JSONArray = jsonObject.getJSONArray("datos")
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val nino = Nino(
                            obj.getInt("noMatricula"),
                            obj.getString("nombre"),
                            obj.getInt("acudienteCedula"),
                            if (obj.has("fechaNacimiento") && !obj.isNull("fechaNacimiento"))
                                dateFormat.parse(obj.getString("fechaNacimiento")) else null,
                            if (obj.has("fechaIngreso") && !obj.isNull("fechaIngreso"))
                                dateFormat.parse(obj.getString("fechaIngreso")) else null,
                            if (obj.has("fechaFin") && !obj.isNull("fechaFin"))
                                dateFormat.parse(obj.getString("fechaFin")) else null,
                            obj.getString("estado")
                        )
                        ninosArrayList.add(nino)
                    }
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("NinoFragment", "Error al procesar los datos: ${e.message}")
            }
        }, { error ->
            progressDialog.dismiss()
            Log.e("NinoFragment", "Error de red: ${error.message}")
        })

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    private fun eliminarNino(noMatricula: Int) {
        val urlGlobal = getString(R.string.url)
        val urlEliminar = "$urlGlobal/eliminar_nino.php"

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Eliminando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val stringRequest = object : StringRequest(Request.Method.POST, urlEliminar, { response ->
            progressDialog.dismiss()
            val responseTrimmed = response.trim()
            if (responseTrimmed.equals("datos eliminados", ignoreCase = true)) {
                Toast.makeText(requireContext(), "Niño Eliminado", Toast.LENGTH_SHORT).show()
                cargarNinos()
            } else {
                Toast.makeText(requireContext(), "No se pudo eliminar el niño", Toast.LENGTH_SHORT).show()
            }
        }, { error ->
            progressDialog.dismiss()
            Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["noMatricula"] = noMatricula.toString()
                return params
            }
        }

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    private fun agregarNino() {
        startActivity(Intent(requireContext(), AgregarNino::class.java))
    }
}