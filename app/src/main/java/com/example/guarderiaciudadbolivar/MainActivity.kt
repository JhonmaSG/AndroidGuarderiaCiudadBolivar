package com.example.guarderiaciudadbolivar

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
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
        setContentView(R.layout.activity_main)

        navMenu = findViewById(R.id.navMenu)
        navMenu.setOnNavigationItemSelectedListener(mOnNavMenu)
        // Seleccionar el primer item del menú de navegación
        navMenu.selectedItemId = R.id.item1



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private val mOnNavMenu = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.item1 -> {
                supportFragmentManager.commit {
                    replace<Menu_fragment>(R.id.fragment_conteiner)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.item2 -> {
                supportFragmentManager.commit {
                    replace<MenuComidas>(R.id.fragment_conteiner)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.item3 -> {
                supportFragmentManager.commit {
                    replace<pagos>(R.id.fragment_conteiner)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.item_desplegable -> {
                showOpcionesDesplegable(this@MainActivity)
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showOpcionesDesplegable(context: Context) {
        val popupMenu = PopupMenu(ContextThemeWrapper(context, R.style.CustomPopupMenu), findViewById(R.id.navMenu))
        popupMenu.menuInflater.inflate(R.menu.sub_menu, popupMenu.menu)

        // Configura el menú para que se muestre hacia la derecha
        popupMenu.gravity = Gravity.END
        popupMenu.setForceShowIcon(true)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.subitem1 -> {
                    supportFragmentManager.commit {
                        replace<pagos>(R.id.fragment_conteiner)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                }
                R.id.subitem2 -> {
                    supportFragmentManager.commit {
                        replace<MenuComidas>(R.id.fragment_conteiner)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                }
                R.id.subitem3 -> {
                    supportFragmentManager.commit {
                        replace<Menu_fragment>(R.id.fragment_conteiner)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                }

            }
            popupMenu.dismiss()
            true
        }


        popupMenu.show()
    }

}



