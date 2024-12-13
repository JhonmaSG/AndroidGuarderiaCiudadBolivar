package com.example.guarderiaciudadbolivar



import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ListarAlergiasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_alergias)

        val listView: ListView = findViewById(R.id.listViewAlergias)

        listarAlergias(listView)
    }

    private fun listarAlergias(listView: ListView) {
        val urlGlobal = getString(R.string.url)
        val url = "$urlGlobal/listar_alergias.php"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val alergiasList = ArrayList<String>()
                for (i in 0 until response.length()) {
                    val alergia: JSONObject = response.getJSONObject(i)
                    val noMatricula = alergia.getString("noMatricula")
                    val ingredienteId = alergia.getString("ingredienteId")
                    val observaciones = alergia.getString("observaciones")
                    alergiasList.add("Matrícula: $noMatricula\nIngrediente: $ingredienteId\nObservaciones: $observaciones")
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, alergiasList)
                listView.adapter = adapter
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })
        requestQueue.add(jsonArrayRequest)
    }
}
