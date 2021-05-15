package com.example.tfg_geofamily.ui.Familiares

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_geofamily.R
import com.example.tfg_geofamily.databinding.FragmentListBinding
import com.example.tfg_geofamily.fragments.model.FamiliaresViewModel
import com.example.tfg_geofamily.ui.Familiares.adapters.FamiliarAdapter
import com.example.tfg_geofamily.ui.model.Familiares
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    lateinit var recyclerviewlista: RecyclerView
    lateinit var adapter: FamiliarAdapter
    lateinit var familyGroup: MutableList<Familiares>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binding = FragmentListBinding.inflate(inflater, container, false)

        recyclerviewlista = binding.recyclerviewlista

        familiares()

        return binding.root
    }

    private fun listaFamiliares(familyGroup: MutableList<Familiares>) {

        var viewModel = ViewModelProvider(requireActivity()).get(FamiliaresViewModel::class.java)

        Log.e("Users", familyGroup.size.toString())

        adapter = FamiliarAdapter(familyGroup, viewModel)
        recyclerviewlista.adapter = adapter
        recyclerviewlista.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun familiares() {
        var familyGroup = mutableListOf<Familiares>()
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val currentEmailID = FirebaseAuth.getInstance().currentUser!!.email
        val db = FirebaseFirestore.getInstance()
        db.collection("users").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    if (document.id == currentUserID) {
                        for ((k, v) in document.data) {
                            if (v.toString() != currentEmailID) {
                                familyGroup.add(Familiares(0.0, 0.0, "", v.toString()))
                            }
                        }
                    }
                }
            } else {
                Log.w("Users", "Error getting documents.", task.exception)
            }
        }.addOnSuccessListener {
            listaFamiliares(familyGroup)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
        var menuItemSearch = menu.findItem(R.id.app_bar_search)
        menuItemSearch.setVisible(true)

        var searchView = menuItemSearch.actionView as SearchView
        searchView.queryHint = "Buscar familiar"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {}
    }
}