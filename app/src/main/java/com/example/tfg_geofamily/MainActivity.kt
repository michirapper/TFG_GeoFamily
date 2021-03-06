package com.example.tfg_geofamily

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_geofamily.databinding.ActivityMainBinding

//private const val TAG = "MainActivity"
//private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

//    private var foregroundOnlyLocationServiceBound = false
//
//    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null
//
//    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver
//
//    private lateinit var sharedPreferences: SharedPreferences
//
//    lateinit var mDatabase: DatabaseReference
//
//    private var permission: Boolean = false
//
//    private val foregroundOnlyServiceConnection = object : ServiceConnection {
//
//        override fun onServiceConnected(name: ComponentName, service: IBinder) {
//            val binder = service as ForegroundOnlyLocationService.LocalBinder
//            foregroundOnlyLocationService = binder.service
//            foregroundOnlyLocationServiceBound = true
//        }
//
//        override fun onServiceDisconnected(name: ComponentName) {
//            foregroundOnlyLocationService = null
//            foregroundOnlyLocationServiceBound = false
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//
//        mDatabase = FirebaseDatabase.getInstance("https://tfg-geofamily-default-rtdb.europe-west1.firebasedatabase.app/").reference
//
//        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
//
//        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
//
//        val enabled = sharedPreferences.getBoolean(
//            SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
//        )
//
//        if (enabled) {
//            foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
//        } else {
//            if (foregroundPermissionApproved()) {
//                foregroundOnlyLocationService?.subscribeToLocationUpdates()
//                    ?: Log.d(TAG, "Service Not Bound")
//            } else {
//                requestForegroundPermissions()
//            }
//        }


    }

//    override fun onStart() {
//        super.onStart()
//        if (permission || foregroundPermissionApproved()) {
//            foregroundOnlyLocationService?.subscribeToLocationUpdates()
//        }
//        updateButtonState(
//            sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)
//        )
//        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
//
//        val serviceIntent = Intent(this, ForegroundOnlyLocationService::class.java)
//        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
//    }

//    override fun onResume() {
//        super.onResume()
//        if (permission || foregroundPermissionApproved()) {
//            foregroundOnlyLocationService?.subscribeToLocationUpdates()
//        }
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//            foregroundOnlyBroadcastReceiver,
//            IntentFilter(
//                ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
//            )
//        )
//    }

//    override fun onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(
//            foregroundOnlyBroadcastReceiver
//        )
//        super.onPause()
//    }
//
//    override fun onStop() {
//        if (foregroundOnlyLocationServiceBound) {
//            unbindService(foregroundOnlyServiceConnection)
//            foregroundOnlyLocationServiceBound = false
//        }
//        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
//
//        super.onStop()
//    }
//
//    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
//        if (key == SharedPreferenceUtil.KEY_FOREGROUND_ENABLED) {
//            updateButtonState(
//                sharedPreferences.getBoolean(
//                    SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
//                )
//            )
//        }
//    }
//
//    private fun foregroundPermissionApproved(): Boolean {
//        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//    }
//
//    private fun requestForegroundPermissions() {
//        val provideRationale = foregroundPermissionApproved()
//
//        // If the user denied a previous request, but didn't check "Don't ask again", provide
//        // additional rationale.
//        if (provideRationale) {
//            Snackbar.make(
//                findViewById(R.id.activity_main),
//                R.string.permission_rationale,
//                Snackbar.LENGTH_LONG
//            )
//                .setAction(R.string.ok) {
//                    // Request permission
//                    ActivityCompat.requestPermissions(
//                        this@MainActivity,
//                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
//                    )
//                }
//                .show()
//        } else {
//            Log.d(TAG, "Request foreground only permission")
//            ActivityCompat.requestPermissions(
//                this@MainActivity,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        Log.d(TAG, "onRequestPermissionResult")
//
//        when (requestCode) {
//            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
//                grantResults.isEmpty() ->
//                    Log.d(TAG, "User interaction was cancelled.")
//                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
//                    // Permission was granted.
//                    foregroundOnlyLocationService?.subscribeToLocationUpdates()
//                else -> {
//                    // Permission denied.
//                    updateButtonState(false)
//
//                    Snackbar.make(
//                        findViewById(R.id.activity_main),
//                        R.string.permission_denied_explanation,
//                        Snackbar.LENGTH_LONG
//                    )
//                        .setAction(R.string.settings) {
//                            // Build intent that displays the App settings screen.
//                            val intent = Intent()
//                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                            val uri = Uri.fromParts(
//                                "package",
//                                BuildConfig.APPLICATION_ID,
//                                null
//                            )
//                            intent.data = uri
//                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                            startActivity(intent)
//                        }
//                        .show()
//                }
//            }
//        }
//    }

//    private fun updateButtonState(trackingLocation: Boolean) {
//        if (trackingLocation) {
//            Toast.makeText(this, "Geolocation Activated", Toast.LENGTH_SHORT).show()
//            permission = true
//        } else {
//            Toast.makeText(this, "Geolocation Desactivated", Toast.LENGTH_SHORT).show()
//            permission = false
//        }
//    }
//
//    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {
//
//        override fun onReceive(context: Context, intent: Intent) {
//            val location = intent.getParcelableExtra<Location>(
//                ForegroundOnlyLocationService.EXTRA_LOCATION
//            )
//
//            if (FirebaseAuth.getInstance().currentUser != null) {
//                if (location != null) {
//                    // firestore()
//                    val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
//                    val currentUserEmail = FirebaseAuth.getInstance().currentUser!!.email
//                    Log.e(
//                        "LatLong",
//                        "Latitud:" + location.latitude + " - " + "Longitud:" + location.longitude + " - email: " + currentUserEmail.toString()
//                    )
//                    val latLang = HashMap<String, Any>()
//                    latLang["latitud"] = location.latitude
//                    latLang["longitud"] = location.longitude
//                    latLang["UID"] = currentUserID
//                    latLang["email"] = currentUserEmail
//                    val userEmail = currentUserEmail.split("@").toTypedArray()
//                    mDatabase.child("usuarios").child(userEmail[0]).setValue(latLang)
//                } else {
//                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        }
//    }
}