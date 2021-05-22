package com.example.tfg_geofamily.ui.Familiares

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tfg_geofamily.databinding.FragmentMapResultBinding
import com.example.tfg_geofamily.fragments.model.FamiliaresViewModel
import com.example.tfg_geofamily.pojo.MapsPojo
import com.example.tfg_geofamily.ui.map.adapters.CustomInfoWindowForGoogleMap
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import java.lang.Exception

class MapResultFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapResultBinding
    private lateinit var mMap: GoogleMap
    lateinit var mDatabase: DatabaseReference
    lateinit var familiar: String
    private var tmpRealTimeMarkers: ArrayList<Marker> = arrayListOf()
    private var realTimeMarkers: ArrayList<Marker> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realTimeMarkers.clear()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapResultBinding.inflate(inflater, container, false)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)
        mDatabase =
            FirebaseDatabase.getInstance("https://tfg-geofamily-default-rtdb.europe-west1.firebasedatabase.app/").reference

        var viewModel = ViewModelProvider(requireActivity()).get(FamiliaresViewModel::class.java)
        familiar = viewModel.getFamiliarSeleccionada().email.toString()


        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map

        val userEmail = familiar.split("@").toTypedArray()


        mDatabase.child("usuarios").child(userEmail[0])
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        var mp = snapshot.getValue(MapsPojo::class.java)
                        var latitud: Double = mp!!.latitud
                        var longitud: Double = mp!!.longitud
                        var uid: String = mp!!.UID
                        var email: String = mp!!.email
                        var markerOptions: MarkerOptions = MarkerOptions().title(email).snippet("($latitud , $longitud)").position(
                            LatLng(latitud, longitud)
                        )
                        tmpRealTimeMarkers.add(mMap.addMarker(markerOptions)!!)
                        realTimeMarkers.clear()
                        realTimeMarkers.addAll(tmpRealTimeMarkers)
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "No hay datos sobre este usuario",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        context,
                        "No hay datos sobre este usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(requireContext()))

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapResultFragment().apply {}
    }


}