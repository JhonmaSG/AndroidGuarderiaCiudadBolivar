package com.example.guarderiaciudadbolivar

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity

import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    lateinit var navMenu: BottomNavigationView


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Establecer el diseño de la actividad
        try {
            setContentView(R.layout.activity_main)
            // Inicializar el menú de navegación
            navMenu = findViewById(R.id.navMenu)
            // Configurar el listener para los elementos del menú de navegación
            navMenu.setOnNavigationItemSelectedListener(mOnNavMenu)
            // Seleccionar el primer elemento del menú de navegación
            navMenu.selectedItemId = R.id.menu_principal

        } catch (e: Exception) {
            // Manejo de excepciones: registrar el error y establecer valores predeterminados
            Log.e("MainActivity", "Error al inicializar la actividad: ${e.message}")
            // Aquí podrías establecer valores por defecto o realizar otras acciones
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private val mOnNavMenu = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        try {
            when (item.itemId) {
                R.id.menu_principal -> {
                    // Reemplazar el fragmento con el menú principal
                    supportFragmentManager.commit {
                        replace<menu_principal_fragment>(R.id.fragment_conteiner)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                    return@OnNavigationItemSelectedListener true
                }

                R.id.inscripciones -> {
                    // Reemplazar el fragmento con el menú de comidas
                    supportFragmentManager.commit {
                        replace<menu_inscripciones_fragment>(R.id.fragment_conteiner)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                    return@OnNavigationItemSelectedListener true
                }

                R.id.pagos -> {
                    // Reemplazar el fragmento con el fragmento de pagos
                    supportFragmentManager.commit {
                        replace<pagos_fragment>(R.id.fragment_conteiner)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                    return@OnNavigationItemSelectedListener true
                }

                R.id.item_desplegable -> {
                    // Mostrar opciones del menú desplegable
                    showOpcionesDesplegable(this@MainActivity)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.menu_ninos -> {
                    // Aquí haces la redirección a la actividad del CRUD de niños
                    val intent = Intent(this@MainActivity, activity_list_ninos::class.java)
                    startActivity(intent)  // Inicia ActivityListNinos
                    return@OnNavigationItemSelectedListener true
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error al manejar el menú de navegación: ${e.message}")
        }
        false
    }



    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showOpcionesDesplegable(context: Context) {
        try {
            // Crear un PopupMenu utilizando el contexto y el estilo personalizado
            val popupMenu = PopupMenu(ContextThemeWrapper(context, R.style.CustomPopupMenu), findViewById(R.id.navMenu))
            popupMenu.menuInflater.inflate(R.menu.sub_menu, popupMenu.menu)

            // Configura el menú para que se muestre hacia la derecha
            popupMenu.gravity = Gravity.END
            popupMenu.setForceShowIcon(true)

            // Establece un listener para manejar los clics en los elementos del menú
            popupMenu.setOnMenuItemClickListener { menuItem ->
                try {
                    when (menuItem.itemId) {
                        R.id.menu_comidas -> {
                            // Reemplazar el fragmento con el fragmento de pagos
                            supportFragmentManager.commit {
                                replace<menu_comidas_fragment>(R.id.fragment_conteiner)
                                setReorderingAllowed(true)
                                addToBackStack("replacement")
                            }
                        }
                        R.id.historial -> {
                            // Reemplazar el fragmento con el menú de comidas
                            supportFragmentManager.commit {
                                replace<historial_fragment>(R.id.fragment_conteiner)
                                setReorderingAllowed(true)
                                addToBackStack("replacement")
                            }
                        }
                        R.id.creditos -> {
                            // Reemplazar el fragmento con el menú principal
                            supportFragmentManager.commit {
                                replace<fragment_creditos>(R.id.fragment_conteiner)
                                setReorderingAllowed(true)
                                addToBackStack("replacement")
                            }
                        }
                    }
                    // Cerrar el menú emergente después de seleccionar una opción
                    popupMenu.dismiss()
                    true
                } catch (e: Exception) {
                    // Manejo de excepciones para clics en elementos del menú
                    Log.e("MainActivity", "Error al manejar el clic en el menú: ${e.message}")
                    false
                }
            }

            // Mostrar el menú emergente
            popupMenu.show()
        } catch (e: Exception) {
            // Manejo de excepciones durante la creación del PopupMenu
            Log.e("MainActivity", "Error al mostrar el menú emergente: ${e.message}")
        }
    }


}



