package com.example.tfg_geofamily.ui.AddFamiliar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tfg_geofamily.databinding.FragmentAddFamiliarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class AddFamiliarFragment : Fragment() {

    private lateinit var binding: FragmentAddFamiliarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddFamiliarBinding.inflate(inflater, container, false)

        binding.button2.setOnClickListener {
            var database = FirebaseFirestore.getInstance()
            var userUID = FirebaseAuth.getInstance().uid
            var addEmail = binding.editTextTextEmailAddress2.text.toString()
            val userEmail = addEmail.split("@").toTypedArray()

            val user: MutableMap<String, Any> = HashMap()
            user[userEmail[0]] = addEmail

            database.collection("users").document(userUID!!).update(user).addOnSuccessListener {
                Toast.makeText(context, "Todo ok", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show()
            }

        }


        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFamiliarFragment().apply {

            }
    }
}