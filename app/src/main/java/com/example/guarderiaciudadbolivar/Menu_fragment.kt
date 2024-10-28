package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView

class Menu_fragment : Fragment() {
    private var name: String? = null
    private var address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(NAME)
            address = it.getString(ADDRESS)
            Log.i("aris",name.orEmpty())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_fragment, container, false)
    }

    companion object {
        const val NAME = "param1"
        const val ADDRESS = "param2"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Menu_fragment().apply {
                arguments = Bundle().apply {
                    putString(NAME, param1)
                    putString(ADDRESS, param2)
                }
            }
    }
}