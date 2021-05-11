package com.example.tfg_geofamily.fragments.maps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_geofamily.R
import com.example.tfg_geofamily.databinding.FragmentHomeMapBinding
import com.example.tfg_geofamily.databinding.FragmentListBinding
import com.example.tfg_geofamily.fragments.adapters.FamiliarAdapter
import com.example.tfg_geofamily.fragments.model.Familiares
import com.example.tfg_geofamily.fragments.model.FamiliaresViewModel

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    lateinit var recyclerviewlista: RecyclerView
    lateinit var adapter: FamiliarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)

        recyclerviewlista = binding.recyclerviewlista

        listaFamiliares()

        return binding.root
    }
    private fun listaFamiliares(){

        //rellenarlo con un array de los RealTimeDatabase

        var dummyList = mutableListOf<Familiares>()
        dummyList.add(Familiares(0.0,0.0,"15665165", "michirapper@gmail.com"))
        dummyList.add(Familiares(0.0,0.0,"15665165", "michirapper2@gmail.com"))

        var familiar = dummyList

        var viewModel = ViewModelProvider(requireActivity()).get(FamiliaresViewModel::class.java)

        adapter = FamiliarAdapter(familiar, viewModel)
        recyclerviewlista.adapter = adapter
        recyclerviewlista.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {}
    }
}