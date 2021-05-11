package com.example.tfg_geofamily.fragments.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_geofamily.R
import com.example.tfg_geofamily.fragments.model.Familiares
import com.example.tfg_geofamily.fragments.model.FamiliaresViewModel

class FamiliarAdapter(var familiares: List<Familiares>, var familiaresViewModel: FamiliaresViewModel): RecyclerView.Adapter<FamiliarAdapter.ViewHolder>(), Filterable {

    var FilterList = ArrayList<Familiares>()

    init{
        FilterList = familiares as ArrayList<Familiares>
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.familiar_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(FilterList[position])
        holder.itemView.setOnClickListener {

            familiaresViewModel.setFamiliarSeleccionada(FilterList[position])
            Log.e("familiar", FilterList[position].email.toString())

//            val action = ListFragmentDirections.actionListFragmentToFichaFragment()
//            it.findNavController().navigate(action)

        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return FilterList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(familiares: Familiares) {
            val textViewNombre = itemView.findViewById<TextView>(R.id.textViewTitulo)
            textViewNombre.text = familiares.email
        }
    }

    override fun getFilter(): Filter {
       return object :Filter(){
           override fun performFiltering(constraint: CharSequence?): FilterResults {
               val charSequence = constraint.toString()
               if (charSequence.isEmpty()){
                   FilterList = familiares as ArrayList<Familiares>
               }
               else{
                   val resultList = ArrayList<Familiares>()
                   for (row in familiares){
                       if (row.email?.toLowerCase()?.contains(charSequence.toLowerCase())!!){
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