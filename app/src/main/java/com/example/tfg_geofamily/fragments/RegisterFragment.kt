package com.example.tfg_geofamily.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tfg_geofamily.R
import com.example.tfg_geofamily.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        mDatabase = FirebaseDatabase.getInstance("https://tfg-geofamily-default-rtdb.europe-west1.firebasedatabase.app/").reference

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment2_to_loginFragment2)
        }
        binding.signUp.setOnClickListener {
            createAccount(binding)
        }

        return binding.root
    }

    private fun createAccount(binding: FragmentRegisterBinding) {
        var email = binding.email.text.toString()
        var password = binding.password.text.toString()
        var confirmPassword = binding.ConfirmPassword.text.toString()
        when {
            TextUtils.isEmpty(email) -> Toast.makeText(
                context,
                "El email es obligatorio",
                Toast.LENGTH_SHORT
            ).show()
            TextUtils.isEmpty(password) -> Toast.makeText(
                context,
                "La contraseña es obligatoria",
                Toast.LENGTH_SHORT
            ).show()
            TextUtils.isEmpty(confirmPassword) -> Toast.makeText(
                context,
                "Tiene que confirmar la contraseña",
                Toast.LENGTH_SHORT
            ).show()
            !TextUtils.equals(password, confirmPassword) -> Toast.makeText(
                context,
                "La contraseña tiene que coincidir",
                Toast.LENGTH_SHORT
            ).show()

            else -> {
                var progressDialog = ProgressDialog(context)
                progressDialog.setTitle("SignUp")
                progressDialog.setMessage("Por favor espera, esto puede tomar unos minutos...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            registrarUsuarioMapa()
                            registrarRTD()
                            Toast.makeText(context,"Account has been created successfully.",Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_registerFragment2_to_drawerActivity)

                        } else {
                            val messeage = task.exception!!.toString()
                            Toast.makeText(context, "Error: $messeage", Toast.LENGTH_SHORT).show()
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }

    }

    private fun registrarRTD() {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val currentUserEmail = FirebaseAuth.getInstance().currentUser!!.email

        val latLang = HashMap<String, Any>()
        latLang["latitud"] = 0.0
        latLang["longitud"] = 0.0
        latLang["avatar"] = "https://firebasestorage.googleapis.com/v0/b/tfg-geofamily.appspot.com/o/fotos%2Fdefault.png?alt=media&token=4c88e987-e2cc-4d16-9533-e412f495533d"
        latLang["UID"] = currentUserID
        latLang["email"] = currentUserEmail!!
        val userEmail = currentUserEmail.split("@").toTypedArray()
        mDatabase.child("usuarios").child(userEmail[0]).setValue(latLang)
    }

    private fun registrarUsuarioMapa() {
        var database = FirebaseFirestore.getInstance()
        var userUID = FirebaseAuth.getInstance().uid
        var addEmail = binding.email.text.toString()
        val userEmail = addEmail.split("@").toTypedArray()

        val user: MutableMap<String, Any> = HashMap()
        user[userEmail[0]] = addEmail

        database.collection("users").document(userUID!!).update(user).addOnSuccessListener {

            Toast.makeText(context, "Todo ok", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {

            database.collection("users").document(userUID!!)
                .set(user)
                .addOnSuccessListener { Toast.makeText(context, "Todo ok", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show() }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {}
    }
}