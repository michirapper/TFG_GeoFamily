package com.example.tfg_geofamily

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.tfg_geofamily.databinding.ActivityDrawerBinding
import com.example.tfg_geofamily.pojo.MapsPojo
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

private const val TAG = "DrawerActivity"
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34


class DrawerActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var foregroundOnlyLocationServiceBound = false

    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null

    private lateinit var foregroundOnlyBroadcastReceiver: DrawerActivity.ForegroundOnlyBroadcastReceiver

    private lateinit var sharedPreferences: SharedPreferences

    lateinit var mDatabase: DatabaseReference

    private var permission: Boolean = false

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityDrawerBinding

    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabase = FirebaseDatabase.getInstance("https://tfg-geofamily-default-rtdb.europe-west1.firebasedatabase.app/").reference
        val currentUserEmail = FirebaseAuth.getInstance().currentUser!!.email
        val userEmail = currentUserEmail?.split("@")!!.toTypedArray()
        var icono = ""






        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setSupportActionBar(binding.appBarDrawer.toolbar)
        setContentView(binding.root)



        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val enabled = sharedPreferences.getBoolean(
            SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
        )

        if (enabled) {
            foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
        } else {
            if (foregroundPermissionApproved()) {
                foregroundOnlyLocationService?.subscribeToLocationUpdates()
                    ?: Log.d(TAG, "Service Not Bound")
            } else {
                requestForegroundPermissions()
            }
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val logoutItem = navView.menu.findItem(R.id.nav_mainActivity)
        logoutItem.setOnMenuItemClickListener {
            FirebaseAuth.getInstance().signOut()
            val navController = findNavController(R.id.nav_host_fragment_content_drawer)
            it.onNavDestinationSelected(navController)
            true
        }
        val header = navView.getHeaderView(0)
        val cabecera = header.findViewById<TextView>(R.id.textView)
        cabecera.text = currentUserEmail
        val iconoHeader = header.findViewById<ImageView>(R.id.imageView)



        mDatabase.child("usuarios").child(userEmail[0])
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    try {

                        var mp = snapshot.getValue(MapsPojo::class.java)
                        icono = mp!!.avatar

                        var myUri: Uri = Uri.parse(icono)
                        Log.e("icono", myUri.toString())
                        Glide.with(this@DrawerActivity)
                            .asBitmap()
                            .load(myUri)
                            .fitCenter()
                            .centerCrop()
                            .into(BitmapImageViewTarget(iconoHeader)
                            )

                    } catch (e: Exception) {
                        icono = "https://firebasestorage.googleapis.com/v0/b/tfg-geofamily.appspot.com/o/fotos%2Fdefault.png?alt=media&token=4c88e987-e2cc-4d16-9533-e412f495533d"
                        var myUri: Uri = Uri.parse(icono)
                        Glide.with(this@DrawerActivity)
                            .asBitmap()
                            .load(myUri)
                            .fitCenter()
                            .centerCrop()
                            .into(BitmapImageViewTarget(iconoHeader)
                            )
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        val navController = findNavController(R.id.nav_host_fragment_content_drawer)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_homeMapFragment2, R.id.nav_listFragment2, R.id.nav_addFamiliarFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onStart() {
        super.onStart()
        if (permission || foregroundPermissionApproved()) {
            foregroundOnlyLocationService?.subscribeToLocationUpdates()
        }
        updateButtonState(
            sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)
        )
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val serviceIntent = Intent(this, ForegroundOnlyLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        if (permission || foregroundPermissionApproved()) {
            foregroundOnlyLocationService?.subscribeToLocationUpdates()
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
            )
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            foregroundOnlyBroadcastReceiver
        )
        super.onPause()
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

        super.onStop()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == SharedPreferenceUtil.KEY_FOREGROUND_ENABLED) {
            updateButtonState(
                sharedPreferences.getBoolean(
                    SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
                )
            )
        }
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                findViewById(R.id.activity_main),
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this@DrawerActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                this@DrawerActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    Log.d(TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()
                else -> {
                    // Permission denied.
                    updateButtonState(false)

                    Snackbar.make(
                        findViewById(R.id.activity_main),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }
    }

    private fun updateButtonState(trackingLocation: Boolean) {
        if (trackingLocation) {
            Toast.makeText(this, "Geolocation Activated", Toast.LENGTH_SHORT).show()
            permission = true
        } else {
            Toast.makeText(this, "Geolocation Desactivated", Toast.LENGTH_SHORT).show()
            permission = false
        }
    }

    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                ForegroundOnlyLocationService.EXTRA_LOCATION
            )




                if (FirebaseAuth.getInstance().currentUser != null) {
                    if (location != null) {
                        // firestore()
                        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
                        val currentUserEmail = FirebaseAuth.getInstance().currentUser!!.email
                        Log.e(
                            "LatLong",
                            "Latitud:" + location.latitude + " - " + "Longitud:" + location.longitude + " - email: " + currentUserEmail.toString()
                        )


                        val latLang = HashMap<String, Any>()
                        latLang["latitud"] = location.latitude
                        latLang["longitud"] = location.longitude
                        val userEmail = currentUserEmail?.split("@")!!.toTypedArray()
                        mDatabase.child("usuarios").child(userEmail[0]).updateChildren(latLang)
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }


        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.nav_mainActivity -> {
//                FirebaseAuth.getInstance().signOut()
//                val navController = findNavController(R.id.nav_host_fragment_content_drawer)
//                item.onNavDestinationSelected(navController)
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}