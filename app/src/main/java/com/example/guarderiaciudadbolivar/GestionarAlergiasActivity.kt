package com.example.guarderiaciudadbolivar

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class GestionarAlergiasActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: AlergiasAdapter
    private val alergiasArrayList = ArrayList<Alergia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestionar_alergia)

        val noMatricula = intent.getIntExtra("noMatricula", -1)
        listView = findViewById(R.id.listAlergia)
        adapter = AlergiasAdapter(this, alergiasArrayList)
        listView.adapter = adapter

        cargarAlergias(noMatricula)
    }

    private fun cargarAlergias(noMatricula: Int) {
        val url = getString(R.string.url) + "/ver_alergias.php"
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Cargando Alergias...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val stringRequest = object : StringRequest(Request.Method.POST, url, { response ->
            progressDialog.dismiss()
            try {
                alergiasArrayList.clear()
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                if (success == "1") {
                    val jsonArray: JSONArray = jsonObject.getJSONArray("datos")
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val ingredienteId = obj.getInt("ingredienteId")
                        val observaciones = obj.getString("observaciones")
                        val alergia = Alergia(ingredienteId, observaciones)
                        alergiasArrayList.add(alergia)
                    }
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
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

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}
