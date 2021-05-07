package com.example.tfg_geofamily.fragments.maps

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tfg_geofamily.R
import com.example.tfg_geofamily.databinding.FragmentHomeMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.system.exitProcess


class HomeMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentHomeMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var mDatabase: DatabaseReference
    private val REQUEST_PERMISSION_REQUEST_CODE = 2020
    private var tmpRealTimeMarkers: ArrayList<Marker> = arrayListOf()
    private var realTimeMarkers: ArrayList<Marker> = arrayListOf()
    private var familyGroup: ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realTimeMarkers.clear()
        firestore()
        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Exit")
                builder.setMessage("Are You Sure?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    dialog.dismiss()
                    exitProcess(-1)
                }
                builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()

            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeMapBinding.inflate(inflater, container, false)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)

        mDatabase =
            FirebaseDatabase.getInstance("https://tfg-geofamily-default-rtdb.europe-west1.firebasedatabase.app/").reference


        subirLatLongFirebase()
        //firestore()

        return binding.root
    }

    private fun subirLatLongFirebase() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSION_REQUEST_CODE
            )
            return
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                       // firestore()
                        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
                        val currentUserEmail = FirebaseAuth.getInstance().currentUser!!.email
                        Log.e(
                            "LatLong",
                            "Latitud:" + location.latitude + " - " + "Longitud:" + location.longitude + " - UID" + currentUserEmail.toString()
                        )
                        val latLang = HashMap<String, Any>()
                        latLang["latitud"] = location.latitude
                        latLang["longitud"] = location.longitude
                        latLang["UID"] = currentUserID
                        latLang["email"] = currentUserEmail
                        mDatabase.child("usuarios").child(currentUserID).setValue(latLang)
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mDatabase.child("usuarios").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //firestore()
                for (marker in realTimeMarkers) {
                    marker.remove()
                }
                for (snap in snapshot.children) {
                    var mp = snap.getValue(MapsPojo::class.java)
                    var latitud: Double = mp!!.latitud
                    var longitud: Double = mp!!.longitud
                    var uid: String = mp!!.UID
                    var email: String = mp!!.email
                    var markerOptions: MarkerOptions = MarkerOptions().position(
                        LatLng(latitud, longitud)
                    )
                    //Ir comparando si esta en el grupo del Usuario
                    // Toast.makeText(context, email, Toast.LENGTH_SHORT).show()
                   Toast.makeText(context, familyGroup.size.toString(), Toast.LENGTH_SHORT).show()
                    for (person in familyGroup) {
                       // Log.d("Users", person)
                        if (email == person) {
                            //Log.d("Users", "true")
                            tmpRealTimeMarkers.add(mMap.addMarker(markerOptions))
                        }
                    }


                }
                realTimeMarkers.clear()
                realTimeMarkers.addAll(tmpRealTimeMarkers)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun firestore() {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("users").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    if (document.id == currentUserID) {
                        //Log.d("Users", document.id + " => " + document.data)
                        for ((k, v) in document.data) {
                            //Log.d("Users", "$v")
                            familyGroup.add(v.toString())

                        }
                    }

                }
                //Toast.makeText(context, familyGroup.size.toString(), Toast.LENGTH_SHORT).show()

            } else {
                Log.w("Users", "Error getting documents.", task.exception)
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.CerrarSesion -> {
                FirebaseAuth.getInstance().signOut()
                findNavController().navigate(R.id.action_homeMapFragment_to_loginFragment)
                true
            }
            R.id.VerTodo -> {
                firestore()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeMapFragment().apply {}
    }


}