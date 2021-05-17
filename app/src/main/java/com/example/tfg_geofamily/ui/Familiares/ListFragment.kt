package com.example.tfg_geofamily.ui.Familiares

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
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
        recyclerviewlista.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val touchHelperCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                private val deleteIcon =
                    ContextCompat.getDrawable(context!!, android.R.drawable.ic_delete)
                private val background = ColorDrawable(Color.RED)
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val entity = adapter!!.getEntity(viewHolder.adapterPosition)
                    adapter!!.removeItem(entity)
                    Toast.makeText(context, entity, Toast.LENGTH_SHORT).show()
                    getFragmentManager()?.beginTransaction()?.
                    detach(this@ListFragment)?.attach(this@ListFragment)
                        ?.commit();
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    val itemView = viewHolder.itemView
                    val iconMargin = (itemView.height - deleteIcon!!.intrinsicHeight) / 2
                    val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                    val iconBottom = iconTop + deleteIcon.intrinsicHeight
                    if (dX > 0) {
                        val iconLeft = itemView.left + iconMargin + deleteIcon.intrinsicWidth
                        val iconRight = itemView.left + iconMargin
                        deleteIcon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        background.setBounds(
                            itemView.left,
                            itemView.top,
                            itemView.left + dX.toInt(),
                            itemView.bottom
                        )
                    } else if (dX < 0) {
                        val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        background.setBounds(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                    } else {
                        background.setBounds(0, 0, 0, 0)
                    }
                    background.draw(c)
                    deleteIcon.draw(c)
                }
            }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerviewlista)
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