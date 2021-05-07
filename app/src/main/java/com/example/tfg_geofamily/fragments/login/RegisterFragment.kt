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
import com.example.tfg_geofamily.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.login.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.signUp.setOnClickListener{
        createAccount(binding)
        }

        return binding.root
    }

    private fun createAccount(binding: FragmentRegisterBinding) {
        var email = binding.email.text.toString()
        var password = binding.password.text.toString()
        var confirmPassword = binding.ConfirmPassword.text.toString()
        when {
            TextUtils.isEmpty(email) -> Toast.makeText(context,"El email es obligatorio",Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(password) -> Toast.makeText(context,"La contraseña es obligatoria",Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(confirmPassword) -> Toast.makeText(context,"Tiene que confirmar la contraseña",Toast.LENGTH_SHORT).show()
            !TextUtils.equals(password, confirmPassword) -> Toast.makeText(context,"La contraseña tiene que coincidir",Toast.LENGTH_SHORT).show()

            else -> {
                var progressDialog = ProgressDialog(context)
                progressDialog.setTitle("SignUp")
                progressDialog.setMessage("Por favor espera, esto puede tomar unos minutos...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        progressDialog.dismiss()
                        Toast.makeText(context, "Account has been created successfully.", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_homeMapFragment)
                    }else{
                        val messeage = task.exception!!.toString()
                        Toast.makeText(context, "Error: $messeage", Toast.LENGTH_SHORT).show()
                        mAuth.signOut()
                        progressDialog.dismiss()
                    }
                }
            }
        }

    }

//    private fun saveUserInfo(email: String, password: String, progressDialog: ProgressDialog) {
//        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
//        val userRef : DatabaseReference = FirebaseDatabase.getInstance("https://tfg-geofamily-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("Users")
//
//        val userMap = HashMap<String, Any>()
//        userMap["uid"] = currentUserID
//        userMap["email"] = email
//        userMap["password"] = password
//
//
//
//        userRef.child(currentUserID).setValue(userMap).addOnCompleteListener { task ->
//            if(task.isSuccessful){
//                progressDialog.dismiss()
//                Toast.makeText(context, "Account has been created successfully.", Toast.LENGTH_SHORT).show()
//
//                findNavController().navigate(R.id.action_registerFragment_to_homeMapFragment)
//
//            }else{
//                var messeage = task.exception!!.toString()
//                Toast.makeText(context, "Error: $messeage", Toast.LENGTH_SHORT).show()
//                FirebaseAuth.getInstance().signOut()
//                progressDialog.dismiss()
//            }
//        }.addOnFailureListener{
//            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
//        }
//
//    }


//    private fun saveUserInfo() {
//
//        val currentUserId="yliItPphfVfFrnIizIyU0V92zz93"
//        val userRef : DatabaseReference=FirebaseDatabase.getInstance().reference.child("Users")
////using hashmap to store values
//        val userMap=HashMap<String,Any>()
//        userMap["uid"]= currentUserId
//        userMap["email"]="email@email.com"
//        userMap["password"]="email@email.com"
//
//
////pasting data in database
//        userRef.push().child(currentUserId).setValue(userMap)
//                .addOnCompleteListener {task ->
//                    if(task.isSuccessful)
//                    {
//                        Toast.makeText(context,"Account has been created",Toast.LENGTH_SHORT).show()
//
//                        //to follow own account by default
//                        FirebaseDatabase.getInstance().reference
//                                .child("Follow").child(currentUserId)
//                                .child("Following").child(currentUserId)
//                                .setValue(true)
//
//                        //forwarding to home page
//                    }
//                    else
//                    {
//                        val message=task.exception!!.toString()
//                        Toast.makeText(context,"Error : $message", Toast.LENGTH_LONG).show()
//                        FirebaseAuth.getInstance().signOut()
//
//                    }
//                }
//    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {}
    }
}