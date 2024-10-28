package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.guarderiaciudadbolivar.Menu_fragment.Companion.ADDRESS
import com.example.guarderiaciudadbolivar.Menu_fragment.Companion.NAME
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var navMenu: BottomNavigationView
    private val mOnNavMenu = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.item1 -> {
                    supportFragmentManager.commit{
                        replace<Menu_fragment>(R.id.fragment_conteiner)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.item2 -> {
                supportFragmentManager.commit{
                    replace<MenuComidas>(R.id.fragment_conteiner)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.item3 -> {
                supportFragmentManager.commit{
                    replace<pagos>(R.id.fragment_conteiner)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
            }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
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
}