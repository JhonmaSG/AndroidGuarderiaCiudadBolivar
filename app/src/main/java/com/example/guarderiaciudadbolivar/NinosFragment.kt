
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
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class NinosFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: NinosAdapter
    private val ninosArrayList = ArrayList<Nino>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout correspondiente al fragmento
        return inflater.inflate(R.layout.fragment_ninos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.listNinos)
        adapter = NinosAdapter(requireContext(), ninosArrayList)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val builder = AlertDialog.Builder(requireContext())
            val dialogItems = arrayOf("Ver Datos", "Editar Niño", "Eliminar Niño", "Gestionar Alergias")
            builder.setTitle(ninosArrayList[position].nombre)
            builder.setItems(dialogItems) { _, i ->
                when (i) {
                    0 -> verDatosNino(position)
                    1 -> editarNino(position)
                    2 -> eliminarNino(ninosArrayList[position].noMatricula)
                    3 -> gestionarAlergias(ninosArrayList[position].noMatricula)
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
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val noMatricula = obj.getInt("noMatricula")
                        val nombre = obj.getString("nombre")
                        val fechaNacimiento = obj.getString("fechaNacimiento")
                        val fechaIngreso = obj.getString("fechaIngreso")
                        val estado = obj.getString("estado")
                        val nino = Nino(noMatricula, nombre, fechaNacimiento, fechaIngreso, estado)
                        ninosArrayList.add(nino)
                    }
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, { error ->
            progressDialog.dismiss()
            error.printStackTrace()
        })

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    private fun verDatosNino(position: Int) {
        val intent = Intent(requireContext(), DetallesNinoActivity::class.java)
        intent.putExtra("noMatricula", ninosArrayList[position].noMatricula)
        startActivity(intent)
    }

    private fun editarNino(position: Int) {
        val intent = Intent(requireContext(), EditarNinoActivity::class.java)
        intent.putExtra("noMatricula", ninosArrayList[position].noMatricula)
        intent.putExtra("nombre", ninosArrayList[position].nombre)
        startActivity(intent)
    }

    private fun eliminarNino(noMatricula: Int) {
        val url = getString(R.string.url) + "/eliminar_nino.php"
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Eliminando Niño...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val stringRequest = object : StringRequest(Request.Method.POST, url, { response ->
            progressDialog.dismiss()
            if (response.equals("Niño Eliminado", ignoreCase = true)) {
                Toast.makeText(requireContext(), "Niño Eliminado", Toast.LENGTH_SHORT).show()
                cargarNinos()
            } else {
                Toast.makeText(requireContext(), "No se pudo eliminar el niño", Toast.LENGTH_SHORT).show()
            }
        }, { error ->
            progressDialog.dismiss()
            error.printStackTrace()
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

    private fun gestionarAlergias(noMatricula: Int) {
        val intent = Intent(requireContext(), GestionarAlergiasActivity::class.java)
        intent.putExtra("noMatricula", noMatricula)
        startActivity(intent)
    }
}


// Modelo de datos para Niño
