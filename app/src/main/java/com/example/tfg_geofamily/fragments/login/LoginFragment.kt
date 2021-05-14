package com.example.tfg_geofamily.fragments.login

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
import com.example.tfg_geofamily.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.login.setOnClickListener {
            var email = binding.email.text.toString()
            var password = binding.password.text.toString()
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
                else -> {
                    var progressDialog = ProgressDialog(context)
                    progressDialog.setTitle("Login")
                    progressDialog.setMessage("Por favor espera, esto puede tomar unos minutos...")
                    progressDialog.setCanceledOnTouchOutside(false)
                    progressDialog.show()
                    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            progressDialog.dismiss()
                            findNavController().navigate(R.id.action_loginFragment_to_homeMapFragment)
                        } else {
                            var messeage = it.exception!!.toString()
                            Toast.makeText(context, "Error: $messeage", Toast.LENGTH_SHORT).show()
                            FirebaseAuth.getInstance().signOut()
                            progressDialog.dismiss()
                        }
                    }
                }
            }


        }
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_homeMapFragment)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {}
    }
}