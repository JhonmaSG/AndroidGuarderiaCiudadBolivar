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

class alergias_fragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var adapter: AlergiasAdapter
    private val alergiasArrayList = ArrayList<Alergias>()

    fun getAlergiasList(): List<Alergias> {
        return alergiasArrayList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alergias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val floatingButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
            floatingButton.setOnClickListener {
                agregarAlergia()
            }

            listView = view.findViewById(R.id.listAlergias)
            adapter = AlergiasAdapter(requireContext(), alergiasArrayList)
            listView.adapter = adapter

            listView.setOnItemClickListener { _, _, position, _ ->
                val builder = AlertDialog.Builder(requireContext())
                val dialogItems = arrayOf("Ver Datos", "Editar Alergia", "Eliminar Alergia")
                builder.setTitle("Alergia de ${alergiasArrayList[position].noMatricula}")
                builder.setItems(dialogItems) { _, i ->
                    when (i) {
                        0 -> {
                            val intent = Intent(requireContext(), DetallesAlergias::class.java)
                            intent.putExtra("noMatricula", alergiasArrayList[position].noMatricula)
                            intent.putExtra("ingredienteId", alergiasArrayList[position].ingredienteId)
                            intent.putExtra("observaciones", alergiasArrayList[position].observaciones)
                            startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(requireContext(), EditarAlergias::class.java)
                            intent.putExtra("noMatricula", alergiasArrayList[position].noMatricula)
                            intent.putExtra("ingredienteId", alergiasArrayList[position].ingredienteId)
                            intent.putExtra("observaciones", alergiasArrayList[position].observaciones)
                            startActivity(intent)
                        }
                        2 -> eliminarAlergia(
                            alergiasArrayList[position].noMatricula,
                            alergiasArrayList[position].ingredienteId
                        )
                    }
                }
                builder.create().show()
            }

            cargarAlergias()
        } catch (e: Exception) {
            Log.e("alergias_fragment", "Error al inicializar el fragmento: ${e.message}")
        }
    }

    private fun eliminarAlergia(noMatricula: String, ingredienteId: String) {
        val urlGlobal = getString(R.string.url)
        val urlEliminar = "$urlGlobal/eliminar_alergias.php"

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Eliminando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val stringRequest = object : StringRequest(Request.Method.POST, urlEliminar, { response ->
            progressDialog.dismiss()
            val responseTrimmed = response.trim()

            if (responseTrimmed.equals("datos eliminados", ignoreCase = true)) {
                Toast.makeText(requireContext(), "Alergia Eliminada", Toast.LENGTH_SHORT).show()
                cargarAlergias()
            } else {
                Toast.makeText(requireContext(), "No se pudo eliminar la alergia", Toast.LENGTH_SHORT).show()
            }
        }, { error ->
            progressDialog.dismiss()
            Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["noMatricula"] = noMatricula
                params["ingredienteId"] = ingredienteId
                return params
            }
        }

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    private fun cargarAlergias() {
        val url = getString(R.string.url) + "/ver_alergias.php"
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Cargando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val stringRequest = StringRequest(Request.Method.POST, url, { response ->
            progressDialog.dismiss()
            try {
                alergiasArrayList.clear()
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")

                if (success == "1") {
                    val jsonArray: JSONArray = jsonObject.getJSONArray("datos")
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val noMatricula = obj.getString("noMatricula")
                        val ingredienteId = obj.getString("ingredienteId")
                        val observaciones = obj.getString("observaciones")

                        val alergia = Alergias(noMatricula, ingredienteId, observaciones)
                        alergiasArrayList.add(alergia)
                    }
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("alergias_fragment", "Error al procesar los datos: ${e.message}")
            }
        }, { error ->
            progressDialog.dismiss()
            Log.e("alergias_fragment", "Error de red: ${error.message}")
        })

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    fun agregarAlergia() {
        startActivity(Intent(requireContext(), AgregarAlergias::class.java))
    }
}