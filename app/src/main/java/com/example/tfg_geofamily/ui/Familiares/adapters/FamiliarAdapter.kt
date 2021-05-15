package com.example.tfg_geofamily.ui.Familiares.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_geofamily.R
import com.example.tfg_geofamily.fragments.model.FamiliaresViewModel
import com.example.tfg_geofamily.ui.Familiares.ListFragmentDirections
import com.example.tfg_geofamily.ui.model.Familiares

class FamiliarAdapter(
    var familiares: List<Familiares>,
    var familiaresViewModel: FamiliaresViewModel
) : RecyclerView.Adapter<FamiliarAdapter.ViewHolder>(), Filterable {

    var FilterList = ArrayList<Familiares>()

    init {
        FilterList = familiares as ArrayList<Familiares>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.familiar_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(FilterList[position])
        holder.itemView.setOnClickListener {

            familiaresViewModel.setFamiliarSeleccionada(FilterList[position])
            Log.e("familiar", FilterList[position].email.toString())

            val action = ListFragmentDirections.actionListFragment2ToMapResultFragment2()
            it.findNavController().navigate(action)

        }
    }

    override fun getItemCount(): Int {
        return FilterList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(familiares: Familiares) {
            val textViewNombre = itemView.findViewById<TextView>(R.id.textViewTitulo)
            textViewNombre.text = familiares.email
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSequence = constraint.toString()
                if (charSequence.isEmpty()) {
                    FilterList = familiares as ArrayList<Familiares>
                } else {
                    val resultList = ArrayList<Familiares>()
                    for (row in familiares) {
                        if (row.email?.toLowerCase()?.contains(charSequence.toLowerCase())!!) {
                            resultList.add(row)
                        }
                    }
                    FilterList = resultList
                }
                var filterResult = FilterResults()
                filterResult.values = FilterList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                FilterList = results?.values as ArrayList<Familiares>
                notifyDataSetChanged()
            }
        }
    }
}